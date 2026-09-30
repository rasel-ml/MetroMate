package app.mrm.metromate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val isDebug: Boolean
