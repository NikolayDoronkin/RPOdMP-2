package app.com.dogfood.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "favorites",
    primaryKeys = ["user_id", "product_key"]
)
data class Favorites(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "product_key") val productKey: String,
) {
    @ColumnInfo(name = "name") var name: String? = null
    @ColumnInfo(name = "description") var description: String? = null
    @ColumnInfo(name = "image_url") var imageUrl: String? = null
}
