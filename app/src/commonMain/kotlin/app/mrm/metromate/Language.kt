package app.mrm.metromate

sealed class Language(val isoFormat: String) {
    data object English : Language("en")

    data object Bangla : Language("bn")
}
