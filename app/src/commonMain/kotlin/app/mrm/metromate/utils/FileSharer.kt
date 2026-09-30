package app.mrm.metromate.utils

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class FileSharer {
    fun share(
        content: String,
        filename: String,
        mimeType: String,
    )
}
