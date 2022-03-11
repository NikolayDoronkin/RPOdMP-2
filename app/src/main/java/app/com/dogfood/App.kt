package app.com.dogfood

import android.app.Application
import app.com.dogfood.data.FavoritesRepository
import app.com.dogfood.db.DogfoodRoomDatabase

class App : Application() {

    val database by lazy { DogfoodRoomDatabase.getDatabase(this) }
    val favoritesRepository by lazy { FavoritesRepository(database.favoritesDao()) }

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}