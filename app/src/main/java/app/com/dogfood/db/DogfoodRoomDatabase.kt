package app.com.dogfood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.com.dogfood.db.dao.FavoritesDao
import app.com.dogfood.db.entity.Favorites


@Database(entities = [Favorites::class], version = 1, exportSchema = false)
abstract class DogfoodRoomDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {

        @Volatile
        private var INSTANCE: DogfoodRoomDatabase? = null

        fun getDatabase(context: Context): DogfoodRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        DogfoodRoomDatabase::class.java,
                        "catfood_database"
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
