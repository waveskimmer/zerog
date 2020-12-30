package inertia.domain

import java.util.*

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}


data class Task(
    val id : String,
    val timestamp : Date,
    val name: String,
    val details: String,
    val priority: Priority
)


