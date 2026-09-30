package app.mrm.metromate.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import app.mrm.metromate.dao.CardDao
import app.mrm.metromate.dao.DemoDao
import app.mrm.metromate.dao.ScanDao
import app.mrm.metromate.dao.TransactionDao
import app.mrm.metromate.data.CardEntity
import app.mrm.metromate.data.DemoLocal
import app.mrm.metromate.data.ScanEntity
import app.mrm.metromate.data.TransactionEntity

expect class DatabaseProvider {
    fun getDatabase(): AppDatabase
}

@Database(entities = [DemoLocal::class, CardEntity::class, ScanEntity::class, TransactionEntity::class], version = 3)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DemoDao

    abstract fun getCardDao(): CardDao

    abstract fun getScanDao(): ScanDao

    abstract fun getTransactionDao(): TransactionDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
