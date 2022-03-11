package app.com.dogfood.core.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * https://proandroiddev.com/view-model-creation-in-android-android-architecture-components-kotlin-ce9f6b93a46b
 */

inline fun <reified T : ViewModel> Fragment.getViewModelExt(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this)[T::class.java]
    else
        ViewModelProvider(this, BaseViewModelFactory(creator))[T::class.java]
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModelExt(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this)[T::class.java]
    else
        ViewModelProvider(this, BaseViewModelFactory(creator))[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.getActivityViewModelExt(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(activity!!)[T::class.java]
    else
        ViewModelProvider(activity!!, BaseViewModelFactory(creator))[T::class.java]
}

@Suppress("UNCHECKED_CAST")
class BaseViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}