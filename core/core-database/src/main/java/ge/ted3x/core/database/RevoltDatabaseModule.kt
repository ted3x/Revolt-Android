package ge.ted3x.core.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.ted3x.revolt.RevoltDatabase

@Module
@InstallIn(SingletonComponent::class)
object RevoltDatabaseModule {

    @Provides
    fun provideSqliteDriver(@ApplicationContext context: Context): AndroidSqliteDriver {
        return AndroidSqliteDriver(
            schema = RevoltDatabase.Schema,
            context = context,
            name = DATABASE,
            callback = object : AndroidSqliteDriver.Callback(RevoltDatabase.Schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    setPragma(db, "JOURNAL_MODE = WAL")
                    setPragma(db, "SYNCHRONOUS = 2")
                }

                private fun setPragma(db: SupportSQLiteDatabase, pragma: String) {
                    val cursor = db.query("PRAGMA $pragma")
                    cursor.moveToFirst()
                    cursor.close()
                }
            })
    }

    @Provides
    fun provideRevoltDatabase(driver: AndroidSqliteDriver): RevoltDatabase {
        return RevoltDatabase(driver)
    }

    private const val DATABASE = "revolt.db"
}