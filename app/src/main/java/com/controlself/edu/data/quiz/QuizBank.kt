package com.controlself.edu.data.quiz

import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuestionType
import com.controlself.edu.domain.model.quiz.QuizAttempt

/**
 * Semilla embebida MVP (20 preguntas / curso). Overrides del docente → [EditableQuizRepository].
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

    private fun mathQuestions(): List<Question> = listOf(
        mc("m1", "¿Cuánto es 7 × 8?", listOf("54", "56", "63", "64"), 1),
        mc("m2", "¿Cuál es la mitad de 48?", listOf("18", "24", "28", "32"), 1),
        tf("m3", "Un triángulo tiene 3 lados.", true),
        mc("m4", "¿Cuál es el resultado de 15 + 27?", listOf("32", "40", "42", "52"), 2),
        mc("m5", "¿Qué fracción equivale a 0.5?", listOf("1/4", "1/3", "1/2", "2/3"), 2),
        tf("m6", "El número 17 es par.", false),
        mc("m7", "¿Cuántos grados tiene un ángulo recto?", listOf("45°", "90°", "180°", "360°"), 1),
        mc("m8", "¿Cuál es el perímetro de un cuadrado de lado 5?", listOf("10", "15", "20", "25"), 2),
        tf("m9", "2⁵ = 32.", true),
        mc("m10", "Resuelve: 100 − 37", listOf("63", "67", "73", "77"), 0),
        mc("m11", "¿Cuál es el MCD de 12 y 18?", listOf("2", "3", "6", "9"), 2),
        tf("m12", "π es aproximadamente 3.14.", true),
        mc("m13", "¿Cuánto es 9²?", listOf("18", "27", "81", "99"), 2),
        mc("m14", "En una proporción, si 2/4 = x/10, x =", listOf("4", "5", "6", "8"), 1),
        tf("m15", "Un hexágono tiene 5 lados.", false),
        mc("m16", "¿Cuál es el área de un rectángulo 4×6?", listOf("10", "20", "24", "48"), 2),
        mc("m17", "¿Qué es 3/4 de 40?", listOf("10", "20", "30", "35"), 2),
        tf("m18", "La suma de dos números impares es par.", true),
        mc("m19", "¿Cuál es la raíz cuadrada de 81?", listOf("8", "9", "10", "11"), 1),
        mc("m20", "Si un producto cuesta 50 y tiene 10% de descuento, ¿cuánto pagas?", listOf("40", "45", "48", "55"), 1)
    )

    private fun commsQuestions(): List<Question> = listOf(
        mc("c1", "¿Qué es un sustantivo?", listOf("Acción", "Nombre de persona, animal o cosa", "Calificativo", "Conector"), 1),
        tf("c2", "Las mayúsculas se usan al inicio de una oración.", true),
        mc("c3", "Sinónimo de “feliz”:", listOf("Triste", "Alegre", "Enojado", "Cansado"), 1),
        mc("c4", "¿Qué signo cierra una pregunta?", listOf(".", ",", ";", "?"), 3),
        tf("c5", "“Correr” es un verbo.", true),
        mc("c6", "Antónimo de “grande”:", listOf("Enorme", "Pequeño", "Alto", "Ancho"), 1),
        mc("c7", "¿Qué es un adjetivo?", listOf("Nombra", "Describe o califica", "Une oraciones", "Expresa acción"), 1),
        tf("c8", "Una fábula siempre tiene moraleja.", true),
        mc("c9", "¿Cuál es la sílaba tónica de “camión”?", listOf("ca", "mi", "ón", "cio"), 2),
        mc("c10", "Género literario de “Caperucita Roja”:", listOf("Ensayo", "Cuento", "Novela histórica", "Crónica"), 1),
        tf("c11", "El punto y seguido separa párrafos distintos.", false),
        mc("c12", "¿Qué es un diálogo?", listOf("Descripción", "Conversación entre personajes", "Lista", "Título"), 1),
        mc("c13", "Prefijo que significa “contra”:", listOf("pre-", "anti-", "sub-", "re-"), 1),
        tf("c14", "“Hola” necesita tilde.", false),
        mc("c15", "Figura literaria que compara usando “como”:", listOf("Metáfora", "Símil", "Hipérbole", "Ironía"), 1),
        mc("c16", "¿Qué tipo de texto instruye pasos?", listOf("Narrativo", "Instructivo", "Poético", "Diálogo"), 1),
        tf("c17", "El sujeto realiza la acción del verbo.", true),
        mc("c18", "Plural de “lápiz”:", listOf("lápizes", "lápices", "lapices", "lápizs"), 1),
        mc("c19", "¿Qué es un párrafo?", listOf("Una palabra", "Conjunto de oraciones relacionadas", "Un título", "Una coma"), 1),
        mc("c20", "Conector de causa:", listOf("sin embargo", "porque", "además", "luego"), 1)
    )

    private fun scienceQuestions(): List<Question> = listOf(
        mc("s1", "¿Qué planeta es el tercero desde el Sol?", listOf("Marte", "Venus", "Tierra", "Mercurio"), 2),
        tf("s2", "El agua hierve a 100 °C a nivel del mar.", true),
        mc("s3", "Estado de la materia sin forma ni volumen fijos:", listOf("Sólido", "Líquido", "Gas", "Plasma solo"), 2),
        mc("s4", "Órgano que bombea la sangre:", listOf("Pulmón", "Hígado", "Corazón", "Riñón"), 2),
        tf("s5", "Las plantas realizan fotosíntesis.", true),
        mc("s6", "¿Qué gas absorben las plantas?", listOf("Oxígeno", "Nitrógeno", "CO₂", "Helio"), 2),
        mc("s7", "Fuerza que nos mantiene en la Tierra:", listOf("Magnetismo", "Gravedad", "Fricción", "Inercia"), 1),
        tf("s8", "Los mamíferos ponen huevos siempre.", false),
        mc("s9", "Unidad básica de la vida:", listOf("Átomo", "Célula", "Tejido", "Órgano"), 1),
        mc("s10", "¿Qué es un ecosistema?", listOf("Solo animales", "Seres vivos y su ambiente", "Solo plantas", "Una roca"), 1),
        tf("s11", "La luz viaja más rápido que el sonido.", true),
        mc("s12", "Metal líquido a temperatura ambiente:", listOf("Hierro", "Mercurio", "Cobre", "Aluminio"), 1),
        mc("s13", "¿Qué produce la combustión típica?", listOf("Solo agua", "Calor y a menudo CO₂", "Solo oxígeno", "Hielo"), 1),
        tf("s14", "El Sol es una estrella.", true),
        mc("s15", "Hueso más largo del cuerpo humano:", listOf("Tibia", "Fémur", "Húmero", "Radio"), 1),
        mc("s16", "¿Qué mide un termómetro?", listOf("Masa", "Temperatura", "Velocidad", "Presión solo"), 1),
        tf("s17", "Los virus son células completas.", false),
        mc("s18", "Fuente renovable de energía:", listOf("Carbón", "Petróleo", "Solar", "Gas natural"), 2),
        mc("s19", "¿Qué es la evaporación?", listOf("Líquido a sólido", "Líquido a gas", "Gas a líquido", "Sólido a líquido"), 1),
        mc("s20", "Planeta conocido como el rojo:", listOf("Júpiter", "Marte", "Saturno", "Neptuno"), 1)
    )
}
