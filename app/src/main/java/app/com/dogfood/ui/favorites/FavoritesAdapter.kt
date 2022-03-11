package app.com.dogfood.ui.favorites

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.com.dogfood.db.entity.Favorites


class FavoritesAdapter : ListAdapter<Favorites, FavoritesViewHolder>(REPO_COMPARATOR) {

    var shotClickListener: ((Favorites, v: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            shotClickListener?.apply {
                holder.itemView.setOnClickListener { invoke(item, it) }
            }
            holder.bind(item)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Favorites>() {
            override fun areItemsTheSame(oldItem: Favorites, newItem: Favorites): Boolean =
                oldItem.userId == newItem.userId && oldItem.productKey == newItem.productKey

            override fun areContentsTheSame(oldItem: Favorites, newItem: Favorites): Boolean =
                oldItem == newItem
        }
    }
}
