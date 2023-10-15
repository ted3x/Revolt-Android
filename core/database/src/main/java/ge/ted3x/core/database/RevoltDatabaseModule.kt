package ge.ted3x.core.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.RevoltMemberEntity
import ge.ted3x.revolt.RevoltMessageEntity
import ge.ted3x.revolt.RevoltReactionsEntity
import ge.ted3x.revolt.RevoltServerCategoryEntity
import ge.ted3x.revolt.RevoltServerEntity
import ge.ted3x.revolt.RevoltServerRoleEntity
import ge.ted3x.revolt.RevoltUserEntity
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
            RevoltFileEntityAdapter = RevoltFileEntity.Adapter(
                sizeAdapter = IntColumnAdapter,
                metadata_heightAdapter = IntColumnAdapter,
                metadata_widthAdapter = IntColumnAdapter
            ),
            RevoltUserEntityAdapter = RevoltUserEntity.Adapter(
                badgesAdapter = IntColumnAdapter,
                flagsAdapter = IntColumnAdapter
            ),
            RevoltMessageEntityAdapter = RevoltMessageEntity.Adapter(
                mentionsAdapter = listOfStringsAdapter,
                repliesAdapter = listOfStringsAdapter,
                interactions_reactionsAdapter = listOfStringsAdapter
            ),
            RevoltReactionsEntityAdapter = RevoltReactionsEntity.Adapter(
                usersAdapter = listOfStringsAdapter
            ),
            RevoltMemberEntityAdapter = RevoltMemberEntity.Adapter(
                rolesAdapter = listOfStringsAdapter
            ),
            RevoltServerCategoryEntityAdapter = RevoltServerCategoryEntity.Adapter(
                channelsAdapter = listOfStringsAdapter
            ),
            RevoltServerRoleEntityAdapter = RevoltServerRoleEntity.Adapter(
                permissions_allowedAdapter = IntColumnAdapter,
                permissions_disallowedAdapter = IntColumnAdapter,
                rankAdapter = IntColumnAdapter,
            ),
            RevoltServerEntityAdapter = RevoltServerEntity.Adapter(
                channelsAdapter = listOfStringsAdapter,
                categoriesAdapter = listOfStringsAdapter,
                rolesAdapter = listOfStringsAdapter,
                default_permissionsAdapter = IntColumnAdapter,
                flagsAdapter = IntColumnAdapter
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