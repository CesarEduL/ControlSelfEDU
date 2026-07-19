package com.controlself.edu.data.admin

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.repository.AdminPasswordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private val Context.adminPasswordDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "admin_password"
)

/**
 * Hash PBKDF2 + salt (PRP-13). Nunca guarda la contraseña en claro.
 */
class PersistentAdminPasswordRepository(
    context: Context
) : AdminPasswordRepository {

    private val dataStore = context.applicationContext.adminPasswordDataStore

    override fun observeIsSet(): Flow<Boolean> =
        dataStore.data.map { prefs ->
            !prefs[KEY_HASH].isNullOrBlank() && !prefs[KEY_SALT].isNullOrBlank()
        }

    override suspend fun isSet(): Boolean {
        val prefs = dataStore.data.first()
        return !prefs[KEY_HASH].isNullOrBlank() && !prefs[KEY_SALT].isNullOrBlank()
    }

    override suspend fun setPassword(newPassword: String, currentPassword: String?): Result<Unit> {
        if (newPassword.length < MIN_LENGTH) {
            return Result.failure(IllegalArgumentException("Mínimo $MIN_LENGTH caracteres"))
        }
        val prefs = dataStore.data.first()
        val hasExisting = !prefs[KEY_HASH].isNullOrBlank()
        if (hasExisting) {
            if (currentPassword.isNullOrBlank() || !verify(currentPassword)) {
                return Result.failure(IllegalArgumentException("Contraseña actual incorrecta"))
            }
        }
        val salt = ByteArray(SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val hash = hash(newPassword, salt)
        dataStore.edit { store ->
            store[KEY_SALT] = Base64.encodeToString(salt, Base64.NO_WRAP)
            store[KEY_HASH] = Base64.encodeToString(hash, Base64.NO_WRAP)
        }
        return Result.success(Unit)
    }

    override suspend fun verify(password: String): Boolean {
        val prefs = dataStore.data.first()
        val saltB64 = prefs[KEY_SALT] ?: return false
        val hashB64 = prefs[KEY_HASH] ?: return false
        val salt = Base64.decode(saltB64, Base64.NO_WRAP)
        val expected = Base64.decode(hashB64, Base64.NO_WRAP)
        val actual = hash(password, salt)
        return actual.contentEquals(expected)
    }

    private fun hash(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_BITS)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }

    companion object {
        private const val MIN_LENGTH = 6
        private const val SALT_BYTES = 16
        private const val ITERATIONS = 12_000
        private const val KEY_BITS = 256
        private val KEY_SALT = stringPreferencesKey("admin_salt")
        private val KEY_HASH = stringPreferencesKey("admin_hash")
    }
}
