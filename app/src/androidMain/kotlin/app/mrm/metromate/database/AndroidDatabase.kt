package app.mrm.metromate.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver

actual class DatabaseProvider(private val context: Context) {
    actual fun getDatabase(): AppDatabase {
        val dbFile = context.getDatabasePath("mrt_buddy.db")
        return Room.databaseBuilder<AppDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath,
        )
            .addMigrations(MIGRATION_2_3)
            .setDriver(AndroidSQLiteDriver())
            .build()
    }
}
