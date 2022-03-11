package app.com.dogfood.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


abstract class BaseSignInActivity : AppCompatActivity() {

    protected var account: GoogleSignInAccount? = null

    fun getGoogleSinginClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    fun isUserSignedIn(): Boolean {
        account = GoogleSignIn.getLastSignedInAccount(this)
        return account != null
    }

}