package com.mythly.app.domain.model

enum class Deity(val displayName: String) {
    KRISHNA("Krishna"),
    RAMA("Rama"),
    SHIVA("Shiva"),
    GANESHA("Ganesha"),
    DURGA("Durga"),
    HANUMAN("Hanuman"),
    VISHNU("Vishnu"),
    BRAHMA("Brahma"),
    LAKSHMI("Lakshmi"),
    SARASWATI("Saraswati"),
    OTHER("Other")
}

fun Deity.toName(): String{
    return this.displayName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
