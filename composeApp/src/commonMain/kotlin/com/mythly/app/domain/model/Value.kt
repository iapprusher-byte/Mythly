package com.mythly.app.domain.model

enum class Value(val displayName: String) {
    DEVOTION("Devotion"),
    COURAGE("Courage"),
    WISDOM("Wisdom"),
    DHARMA("Dharma"),
    COMPASSION("Compassion"),
    TRUTH("Truth"),
    DUTY("Duty"),
    HUMILITY("Humility"),
    PERSEVERANCE("Perseverance"),
    LOYALTY("Loyalty"),
    PATIENCE("Patience")
}

fun Value.toName() = this.displayName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

