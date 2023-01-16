package sb.lib.todolistapp.repositories

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.android.gms.tasks.Task
import sb.lib.todolistapp.database.authentication.GoogleAuthenticator
import sb.lib.todolistapp.database.authentication.WebAuthenticator
import sb.lib.todolistapp.database.network.RemoteService
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.models.User
import javax.inject.Inject

class TodoRepository @Inject constructor(private val remoteService: RemoteService ,private val googleAuthenticator: WebAuthenticator) : BaseRepository() {



    val authentication = googleAuthenticator.getResultLiveData()


    fun addTodo(todo: Todo ,user :User): Task<Void> {

       return remoteService.addTodo(todo,user)
    }

    fun getClientIntent(): Intent {
        return googleAuthenticator.getClientIntent()
    }

    fun initGoogleAuthentication(fragment: Fragment) {


        googleAuthenticator.initializeSignRequest(fragment)
    }


    fun isAlreadyLoggedIn():Boolean {
        return googleAuthenticator.isLoggedIn() }

    fun googleAuthenticate() {

        googleAuthenticator.authenticate()
    }

    fun signOut() {

        googleAuthenticator.signOut()
    }

    fun getUserAccountDetails(): GoogleSignInAccount {

        return googleAuthenticator.userAccountDetails() }

}