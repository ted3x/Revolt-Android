package ge.ted3x.core.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import io.requery.android.database.sqlite.SQLiteOpenHelper
import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.MemberEntity
import ge.ted3x.revolt.MessageEntity
import ge.ted3x.revolt.ReactionsEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.UserEntity
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RevoltDatabaseModule {

    @Provides
    @Singleton
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
    @Singleton
    fun provideRevoltDatabase(driver: AndroidSqliteDriver): RevoltDatabase {
        return RevoltDatabase(
            driver,
            FileEntityAdapter = FileEntity.Adapter(
                sizeAdapter = IntColumnAdapter,
                metadata_heightAdapter = IntColumnAdapter,
                metadata_widthAdapter = IntColumnAdapter
            ),
            UserEntityAdapter = UserEntity.Adapter(
                badgesAdapter = IntColumnAdapter,
                flagsAdapter = IntColumnAdapter
            ),
            MessageEntityAdapter = MessageEntity.Adapter(
                mentionsAdapter = listOfStringsAdapter,
                repliesAdapter = listOfStringsAdapter,
                interactions_reactionsAdapter = listOfStringsAdapter
            ),
            ReactionsEntityAdapter = ReactionsEntity.Adapter(
                usersAdapter = listOfStringsAdapter
            ),
            MemberEntityAdapter = MemberEntity.Adapter(
                rolesAdapter = listOfStringsAdapter
            )
        )
    }

    private val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
        override fun decode(databaseValue: String) =
            if (databaseValue.isEmpty()) {
                listOf()
            } else {
                databaseValue.split(",")
            }

        override fun encode(value: List<String>) = value.joinToString(separator = ",")
    }

    private const val DATABASE = "revolt.db"
}