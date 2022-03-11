package app.com.dogfood.core.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation


fun Fragment.navigateExt(directions: NavDirections) {
    view?.let { Navigation.findNavController(it).navigate(directions) }
}

fun Fragment.navigateExt(@IdRes resId: Int) {
    view?.let { Navigation.findNavController(it).navigate(resId) }
}

fun Fragment.showSoftKeyboardExt(view: View) {
    if (view.requestFocus()) {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}

fun Fragment.hideSoftKeyboardExt() {
    activity?.hideSoftKeyboardExt()
}