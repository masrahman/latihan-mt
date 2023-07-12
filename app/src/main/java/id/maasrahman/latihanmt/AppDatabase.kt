package id.maasrahman.latihanmt

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Biodata::class], version = 1)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bioDao(): BiodataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE
                ?: synchronized(this){
                    val instance = databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "biodata_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }
        }
    }
}