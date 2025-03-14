package com.gerwalex.batteryguard

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform