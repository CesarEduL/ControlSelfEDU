package com.controlself.edu.domain.model

enum class Course(
    val id: String,
    val title: String,
    val difficulty: String
) {
    MATH("math", "Matemática", "Intermedio"),
    COMMS("comms", "Comunicación", "Básico"),
    SCIENCE("science", "Ciencia y Tecnología", "Intermedio");

    companion object {
        fun fromId(id: String): Course? = entries.find { it.id == id }
    }
}
