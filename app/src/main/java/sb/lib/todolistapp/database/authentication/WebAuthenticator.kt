package sb.lib.todolistapp.database.authentication

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import sb.lib.todolistapp.models.Auth

interface WebAuthenticator {

    fun initializeSignRequest(activity: Fragment)
    fun authenticate()
    fun signOut()
    fun getClientIntent(): Intent

    fun getResultLiveData() : LiveData<Auth>
    fun isLoggedIn() :Boolean

    fun userAccountDetails(): GoogleSignInAccount


}