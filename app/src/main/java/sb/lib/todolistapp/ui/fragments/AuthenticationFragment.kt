package sb.lib.todolistapp.ui.fragments

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.AndroidEntryPoint
import sb.lib.todolistapp.BR
import sb.lib.todolistapp.R
import sb.lib.todolistapp.databinding.AuthenticationDataBinding
import sb.lib.todolistapp.models.AuthError
import sb.lib.todolistapp.models.DefaultAuth
import sb.lib.todolistapp.models.User
import sb.lib.todolistapp.navigators.AuthenticateNavigator
import sb.lib.todolistapp.viewmodel.AuthenticationViewModel


@AndroidEntryPoint
class AuthenticationFragment : BaseFragment<AuthenticationDataBinding, AuthenticationViewModel>() , AuthenticateNavigator{



    companion object{

        private val TAG = AuthenticationFragment::class.java.simpleName
    }

    private val mViewModel : AuthenticationViewModel by activityViewModels()


    override fun getLayoutId(): Int  = R.layout.athentication_layout
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getViewModel(): AuthenticationViewModel  = mViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mViewModel.setNavigator(this )

       mViewModel.initGoogleAuthentication(this)


        if(mViewModel.isAlreadyLoggedIn()) {
            val googleSignInAccount = mViewModel.userDetails()

            navigate(
                User(
                    googleSignInAccount.email, googleSignInAccount.id
                )
            )


        }
        mViewModel.authenticationLiveData.observe(viewLifecycleOwner ){


           if(it!=null)
            when(it ){
               is  User -> {

                   navigate(it)
                }

                is DefaultAuth ->{
                    Log.i(TAG ,"Default Auth  ")


                }

                is AuthError ->{
                    Log.i(TAG ,"AuthError  ")

                }




            }

        }

    }

    private fun navigate(user : User ) {

        Toast.makeText(requireContext() , "Successfully Logged In",Toast.LENGTH_LONG).show()

        val action = AuthenticationFragmentDirections
            .actionAuthenticationFragmentToTodoListFragment(
            user
            )
        findNavController().navigate(action )

    }

    override fun onResume() {
        super.onResume()


    }

    override fun authenticate() {

        mViewModel.googleAuthenticate()
    }


}