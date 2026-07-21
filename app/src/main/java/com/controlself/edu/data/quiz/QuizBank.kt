package com.controlself.edu.data.quiz

import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuestionType
import com.controlself.edu.domain.model.quiz.QuizAttempt

/**
 * Semilla embebida MVP ([QuizAttempt.TOTAL_QUESTIONS] preguntas / curso).
 * Incluye al menos un ejemplo de cada tipo que el docente puede crear
 * (opción múltiple y verdadero/falso). Overrides → [EditableQuizRepository].
 */
object QuizBank {

    fun seedQuestions(courseId: String): List<Question> {
        val course = Course.fromId(courseId) ?: Course.MATH
        val list = when (course) {
            Course.MATH -> mathQuestions()
            Course.COMMS -> commsQuestions()
            Course.SCIENCE -> scienceQuestions()
        }
        require(list.size == QuizAttempt.TOTAL_QUESTIONS) {
            "El banco de ${course.id} debe tener ${QuizAttempt.TOTAL_QUESTIONS} preguntas"
        }
        return list
    }

    /** @deprecated Usar [seedQuestions]; se mantiene por previews. */
    fun questionsFor(courseId: String): List<Question> = seedQuestions(courseId)

    private fun mc(
        id: String,
        prompt: String,
        options: List<String>,
        correctIndex: Int
    ) = Question(id, prompt, QuestionType.MULTIPLE_CHOICE, options, correctIndex)

    private fun tf(id: String, prompt: String, correctIsTrue: Boolean) = Question(
        id = id,
        prompt = prompt,
        type = QuestionType.TRUE_FALSE,
        options = listOf("Verdadero", "Falso"),
        correctIndex = if (correctIsTrue) 0 else 1
    )

    /**
     * Matemática: set corto con ambos tipos creables por el docente.
     * MC = aritmética / porcentaje; V/F = geometría / paridad.
     */
    private fun mathQuestions(): List<Question> = listOf(
        mc("m1", "¿Cuánto es 7 × 8?", listOf("54", "56", "63", "64"), 1),
        tf("m2", "Un triángulo tiene 3 lados.", true),
        mc(
            "m3",
            "Si un producto cuesta 50 y tiene 10% de descuento, ¿cuánto pagas?",
            listOf("40", "45", "48", "55"),
            1
        ),
        tf("m4", "El número 17 es par.", false)
    )

    /** Comunicación: mismo tamaño; se refinará como mates. */
    private fun commsQuestions(): List<Question> = listOf(
        mc("c1", "¿Qué es un sustantivo?", listOf("Acción", "Nombre de persona, animal o cosa", "Calificativo", "Conector"), 1),
        tf("c2", "Las mayúsculas se usan al inicio de una oración.", true),
        mc("c3", "Sinónimo de “feliz”:", listOf("Triste", "Alegre", "Enojado", "Cansado"), 1),
        tf("c4", "“Hola” necesita tilde.", false)
    )

    /** Ciencia: mismo tamaño; se refinará como mates. */
    private fun scienceQuestions(): List<Question> = listOf(
        mc("s1", "¿Qué planeta es el tercero desde el Sol?", listOf("Marte", "Venus", "Tierra", "Mercurio"), 2),
        tf("s2", "El agua hierve a 100 °C a nivel del mar.", true),
        mc("s3", "Órgano que bombea la sangre:", listOf("Pulmón", "Hígado", "Corazón", "Riñón"), 2),
        tf("s4", "Los mamíferos ponen huevos siempre.", false)
    )
}
