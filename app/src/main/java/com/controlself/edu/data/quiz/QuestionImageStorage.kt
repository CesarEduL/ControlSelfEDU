package com.controlself.edu.data.quiz

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException

/**
 * Copia imágenes del selector del sistema al almacenamiento interno de la app.
 * [Question.imagePath] guarda la ruta relativa bajo [Context.getFilesDir].
 */
object QuestionImageStorage {

    private const val SUBDIR = "quiz_question_images"

    fun resolveFile(context: Context, relativePath: String): File =
        File(context.applicationContext.filesDir, relativePath)

    private fun relativePath(questionId: String, extension: String): String =
        "$SUBDIR/$questionId.$extension"

    fun importFromUri(context: Context, questionId: String, sourceUri: Uri): String? {
        val app = context.applicationContext
        val dir = File(app.filesDir, SUBDIR)
        dir.mkdirs()
        dir.listFiles()
            ?.filter { it.nameWithoutExtension == questionId }
            ?.forEach { it.delete() }
        val resolver = app.contentResolver
        val mime = resolver.getType(sourceUri).orEmpty()
        val ext = when {
            mime.contains("png", ignoreCase = true) -> "png"
            mime.contains("webp", ignoreCase = true) -> "webp"
            else -> "jpg"
        }
        val relative = relativePath(questionId, ext)
        val dest = resolveFile(app, relative)
        dest.parentFile?.mkdirs()
        return try {
            resolver.openInputStream(sourceUri)?.use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            relative
        } catch (_: IOException) {
            null
        }
    }

    fun delete(context: Context, relativePath: String?) {
        if (relativePath.isNullOrBlank()) return
        resolveFile(context.applicationContext, relativePath).delete()
    }
}
