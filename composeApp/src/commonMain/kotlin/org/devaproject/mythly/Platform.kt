package org.devaproject.mythly

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform