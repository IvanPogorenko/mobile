package com.tsu.mobilecourse.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [TestEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                fillDatabase(database)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillDatabase(db: AppDatabase){
            val testsDao = db.testDao()

            val initialTests = listOf(
                TestEntity(1,
                    "MBTI",
                    "Узнайте ваш тип личности и помогите в подборе собеседника",
                    "MBTI (Myers-Briggs Type Indicator) - это методика типирования личности, созданная Изабель Бриггс Майерс и Кэтрин Кук Бриггс на основе теории психологических типов Карла Густава Юнга. Тест поможет определить ваш тип личности из 16 возможных комбинаций.",
                    "INJF",
                    true),
                TestEntity(2,
                    "Большая пятерка",
                    "Определите ваши основные черты личности",
                    "Тест 'Большая пятерка' (Big Five) измеряет пять основных черт личности: открытость опыту, добросовестность, экстраверсию, доброжелательность и нейротизм. Эта модель широко используется в психологии для описания личности.",
                    "result",
                    true),
            )

            GlobalScope.launch{
                initialTests.forEach { test ->
                    testsDao.insertTest(test)
                }
            }
        }
    }
}