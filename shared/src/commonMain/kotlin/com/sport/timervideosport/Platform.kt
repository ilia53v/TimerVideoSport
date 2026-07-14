package com.sport.timervideosport

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform