package app.com.dogfood.ui.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import app.com.dogfood.R

class InfoPagerAdapter(private val array: Array<String>) : PagerAdapter() {

    override fun getCount() = array.size

    override fun isViewFromObject(view: View, obj: Any) = view === obj as View

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.page_info_layout, container, false)

        val helpView: TextView = view.findViewById(R.id.helpView)
        val footerView: TextView = view.findViewById(R.id.footerView)

        helpView.text = HtmlCompat.fromHtml(array[position], HtmlCompat.FROM_HTML_MODE_LEGACY)
        footerView.text = view.context.getString(R.string.helper_pager_footer, position + 1, array.size)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

}