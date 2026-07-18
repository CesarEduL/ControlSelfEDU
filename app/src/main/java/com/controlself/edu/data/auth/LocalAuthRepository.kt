package com.controlself.edu.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.model.Session
import com.controlself.edu.domain.model.UserRole
import com.controlself.edu.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

/**
 * Auth local con DataStore (PRP-03). Mock: contraseñas en claro — no usar en producción.
 */
class LocalAuthRepository(
    private val context: Context
) : AuthRepository {

    private val dataStore get() = context.authDataStore

    private val _session = MutableStateFlow<Session?>(null)
    override val session: Flow<Session?> = _session.asStateFlow()

    private val _isRestored = MutableStateFlow(false)
    override val isRestored: Flow<Boolean> = _isRestored.asStateFlow()

    suspend fun restore() {
        val prefs = dataStore.data.first()
        seedDemoUsersIfNeeded(prefs)
        val remember = prefs[KEY_REMEMBER] == true
        if (remember) {
            val id = prefs[KEY_SESSION_ID]
            val name = prefs[KEY_SESSION_NAME]
            val roleName = prefs[KEY_SESSION_ROLE]
            if (id != null && name != null && roleName != null) {
                runCatching { UserRole.valueOf(roleName) }.getOrNull()?.let { role ->
                    _session.value = Session(userId = id, displayName = name, role = role)
                }
            }
        }
        _isRestored.value = true
    }

    override suspend fun login(
        usernameOrEmail: String,
        password: String,
        role: UserRole,
        remember: Boolean
    ): Result<Session> {
        val user = usernameOrEmail.trim()
        if (user.isEmpty() || password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Usuario y contraseña requeridos"))
        }
        val users = readUsers()
        val match = users.find {
            it.username.equals(user, ignoreCase = true) &&
                it.password == password &&
                it.role == role
        } ?: return Result.failure(
            IllegalArgumentException("Credenciales o rol incorrectos")
        )
        val session = Session(
            userId = match.username.lowercase(),
            displayName = match.displayName,
            role = match.role
        )
        persistSession(session, remember)
        _session.value = session
        return Result.success(session)
    }

    override suspend fun register(
        displayName: String,
        usernameOrEmail: String,
        password: String,
        role: UserRole
    ): Result<Session> {
        val name = displayName.trim()
        val user = usernameOrEmail.trim()
        if (name.isEmpty() || user.isEmpty()) {
            return Result.failure(IllegalArgumentException("Nombre y usuario requeridos"))
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            return Result.failure(
                IllegalArgumentException("La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres")
            )
        }
        val users = readUsers().toMutableList()
        if (users.any { it.username.equals(user, ignoreCase = true) }) {
            return Result.failure(IllegalArgumentException("Ese usuario ya existe"))
        }
        users.add(
            StoredUser(
                username = user.lowercase(),
                password = password,
                role = role,
                displayName = name
            )
        )
        writeUsers(users)
        val session = Session(
            userId = user.lowercase(),
            displayName = name,
            role = role
        )
        persistSession(session, remember = true)
        _session.value = session
        return Result.success(session)
    }

    override suspend fun requestPasswordReset(usernameOrEmail: String): Result<Unit> {
        val user = usernameOrEmail.trim()
        if (user.isEmpty()) {
            return Result.failure(IllegalArgumentException("Ingresa tu usuario o correo"))
        }
        val exists = readUsers().any { it.username.equals(user, ignoreCase = true) }
        return if (exists) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("No encontramos esa cuenta"))
        }
    }

    override suspend fun logout() {
        dataStore.edit { prefs ->
            prefs[KEY_REMEMBER] = false
            prefs.remove(KEY_SESSION_ID)
            prefs.remove(KEY_SESSION_NAME)
            prefs.remove(KEY_SESSION_ROLE)
        }
        _session.value = null
    }

    private suspend fun persistSession(session: Session, remember: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_REMEMBER] = remember
            if (remember) {
                prefs[KEY_SESSION_ID] = session.userId
                prefs[KEY_SESSION_NAME] = session.displayName
                prefs[KEY_SESSION_ROLE] = session.role.name
            } else {
                prefs.remove(KEY_SESSION_ID)
                prefs.remove(KEY_SESSION_NAME)
                prefs.remove(KEY_SESSION_ROLE)
            }
        }
    }

    private suspend fun seedDemoUsersIfNeeded(prefs: Preferences) {
        if (prefs[KEY_SEEDED] == true) return
        writeUsers(
            listOf(
                StoredUser("estudiante", "123456", UserRole.STUDENT, "Estudiante Demo"),
                StoredUser("docente", "123456", UserRole.TEACHER, "Docente Demo"),
                StoredUser("padre", "123456", UserRole.PARENT, "Padre Demo")
            )
        )
        dataStore.edit { it[KEY_SEEDED] = true }
    }

    private suspend fun readUsers(): List<StoredUser> {
        val set = dataStore.data.map { it[KEY_USERS] ?: emptySet() }.first()
        return set.mapNotNull { StoredUser.decode(it) }
    }

    private suspend fun writeUsers(users: List<StoredUser>) {
        dataStore.edit { prefs ->
            prefs[KEY_USERS] = users.map { it.encode() }.toSet()
        }
    }

    private data class StoredUser(
        val username: String,
        val password: String,
        val role: UserRole,
        val displayName: String
    ) {
        fun encode(): String = listOf(username, password, role.name, displayName)
            .joinToString(SEP)

        companion object {
            private const val SEP = "\u001F"
            fun decode(raw: String): StoredUser? {
                val p = raw.split(SEP)
                if (p.size < 4) return null
                val role = runCatching { UserRole.valueOf(p[2]) }.getOrNull() ?: return null
                return StoredUser(p[0], p[1], role, p[3])
            }
        }
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
        private val KEY_USERS = stringSetPreferencesKey("users")
        private val KEY_SEEDED = booleanPreferencesKey("seeded")
        private val KEY_REMEMBER = booleanPreferencesKey("remember")
        private val KEY_SESSION_ID = stringPreferencesKey("session_id")
        private val KEY_SESSION_NAME = stringPreferencesKey("session_name")
        private val KEY_SESSION_ROLE = stringPreferencesKey("session_role")
    }
}
