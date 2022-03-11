package app.com.dogfood.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import app.com.dogfood.R
import app.com.dogfood.core.BaseFragment
import app.com.dogfood.core.extension.getActivityViewModelExt
import app.com.dogfood.core.extension.navigateExt
import app.com.dogfood.core.extension.showExt
import app.com.dogfood.databinding.FragmentProfileBinding
import app.com.dogfood.ui.activity.MainActivity
import app.com.dogfood.ui.activity.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val account = getActivityViewModelExt { MainViewModel() }.account
        account?.let {
            show(it)
            binding.btnFavorites.setOnClickListener {
                navigateExt(ProfileFragmentDirections.actionNavProfileToNavFavorites())
            }
            binding.btnLogout.setOnClickListener {
                activity?.let { a -> (a as MainActivity).signOut() }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNightSwitch()
    }

    private fun show(account: GoogleSignInAccount) {
        with(account) {
            binding.nameView.text = getString(R.string.profile_name, displayName)
            binding.emailView.text = getString(R.string.profile_email, email)
            binding.imageView.showExt(photoUrl.toString())
        }
    }

    private fun initNightSwitch() {
        val booleanValue = sharedPreferences.getBoolean("night_mode", true)
        if (booleanValue) {
            binding.switchCompat.isChecked = true
        }

        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchCompat.isChecked = true
                sharedPreferences.edit().putBoolean("night_mode", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchCompat.isChecked = false
                sharedPreferences.edit().putBoolean("night_mode", false).apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}