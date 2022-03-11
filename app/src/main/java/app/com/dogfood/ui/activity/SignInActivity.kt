package app.com.dogfood.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import app.com.dogfood.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn


class SignInActivity : BaseSignInActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.googleSignIn.setOnClickListener { signIn() }
    }

    private val signInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                handleSignData(result.data)
            }
        }

    private fun signIn() {
        val signInIntent = getGoogleSinginClient().signInIntent
        signInResult.launch(signInIntent)
    }

    private fun handleSignData(data: Intent?) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }

}