package sb.lib.todolistapp.viewmodel

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.SignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import sb.lib.todolistapp.navigators.AuthenticateNavigator
import sb.lib.todolistapp.repositories.TodoRepository
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val todoRepository : TodoRepository) : BaseViewModel<AuthenticateNavigator>(todoRepository)
{


    val authenticationLiveData = todoRepository.authentication


    fun initGoogleAuthentication(fragment : Fragment){
        todoRepository.initGoogleAuthentication(fragment)
    }

    fun authenticate(){

        getNavigator().authenticate()
    }

    fun googleAuthenticate(){
        todoRepository.googleAuthenticate()
    }

    fun getSignIntent(): Intent {


        return todoRepository.getClientIntent()
    }

    fun signOut() {

        todoRepository.signOut()
    }


    fun userDetails ():GoogleSignInAccount{

       return  todoRepository.getUserAccountDetails()
    }


    fun isAlreadyLoggedIn():Boolean {
        return todoRepository.isAlreadyLoggedIn() }


}