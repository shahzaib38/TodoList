package sb.lib.todolistapp.database.authentication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import sb.lib.todolistapp.models.Auth
import sb.lib.todolistapp.models.AuthError
import sb.lib.todolistapp.models.DefaultAuth
import sb.lib.todolistapp.models.User


class GoogleAuthenticator constructor(private val context  : Context) : WebAuthenticator {


    private val authenticationResult = MutableStateFlow<Auth>(DefaultAuth())
    val autLiveData = authenticationResult.asLiveData()


    override fun getResultLiveData() :LiveData<Auth>{
        return autLiveData }



   override fun userAccountDetails() : GoogleSignInAccount{


        return googleSignInAccount!!
    }


    override fun isLoggedIn(): Boolean {
        return (googleSignInAccount!=null)
    }

    companion object {

        private val TAG = GoogleAuthenticator::class.java.simpleName

        @Volatile
        private  var INSTANCE  : WebAuthenticator?=null

        fun getInstance(context: Context): WebAuthenticator =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: GoogleAuthenticator(context).also { INSTANCE = it  } } }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var resultActivity : ActivityResultLauncher<Intent>


    private var googleSignInAccount : GoogleSignInAccount?=null

   override fun initializeSignRequest(activity : Fragment){


       googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity.requireContext())



       val   googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestId()
            .build()

         mGoogleSignInClient =   GoogleSignIn.getClient(context , googleSignInOptions)



         resultActivity = activity.registerForActivityResult(
           ActivityResultContracts.StartActivityForResult()){

           val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)

           task.addOnSuccessListener {


          val email =      it.email
          val userId = it.id
               authenticationResult.update {  (User(email,userId))}
               Log.i(TAG,"Success Email  ${it.email} Display name ${it.displayName}  USer Id ${it.id}")

           }

           task.addOnFailureListener {
               val message = it.message
               authenticationResult.update {  AuthError(message)}

               Log.i(TAG,"Excepton  Email ${it.message} ")

           }
       }




   }



   override fun authenticate(){

       resultActivity.launch(mGoogleSignInClient.signInIntent)

    }

   override fun signOut(){
        mGoogleSignInClient.signOut()
    }





  override  fun getClientIntent(): Intent {
        return mGoogleSignInClient.signInIntent
    }





}