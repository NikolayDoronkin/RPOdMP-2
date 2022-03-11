package app.com.dogfood.ui.products

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.com.dogfood.api.onliner.Product


class ProductsAdapter : ListAdapter<Product, ProductViewHolder>(REPO_COMPARATOR) {

    var shotClickListener: ((Product, v: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            shotClickListener?.apply {
                holder.itemView.setOnClickListener { invoke(item, it) }
            }
            holder.bind(item)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}
