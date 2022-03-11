package app.com.dogfood.data

import androidx.annotation.WorkerThread
import app.com.dogfood.db.dao.FavoritesDao
import app.com.dogfood.db.entity.Favorites

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    @WorkerThread
    suspend fun insert(favorites: Favorites) {
        favoritesDao.insert(favorites)
    }

    @WorkerThread
    suspend fun delete(favorites: Favorites) {
        favoritesDao.delete(favorites)
    }

    @WorkerThread
    suspend fun getAllFavorites(userId: String) =
        favoritesDao.getAllFavorites(userId)

    @WorkerThread
    suspend fun existFavorites(userId: String, productKey: String) =
        favoritesDao.existFavorites(userId, productKey).isNotEmpty()

}