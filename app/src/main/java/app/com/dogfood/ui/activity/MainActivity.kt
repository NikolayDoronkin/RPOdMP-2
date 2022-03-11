package app.com.dogfood.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.com.dogfood.R
import app.com.dogfood.databinding.ActivityMainBinding

class MainActivity : BaseSignInActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_search, R.id.nav_profile, R.id.nav_info)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        if (!isUserSignedIn()) startSignInActivity()

        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.account = account
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut() {
        if (isUserSignedIn()) {
            getGoogleSinginClient().signOut().addOnCompleteListener {
                if (it.isSuccessful) startSignInActivity()
            }
        }
    }

    private fun startSignInActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}