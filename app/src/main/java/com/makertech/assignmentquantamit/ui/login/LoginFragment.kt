package com.makertech.assignmentquantamit.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.makertech.assignmentquantamit.R
import com.makertech.assignmentquantamit.databinding.FragmentLoginBinding
import com.makertech.assignmentquantamit.other.Constants.RC_SIGN_IN
import com.makertech.assignmentquantamit.other.Status
import com.makertech.assignmentquantamit.ui.base.BaseFragment
import com.makertech.assignmentquantamit.ui.news.NewsListActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment:BaseFragment<FragmentLoginBinding>(R.layout.fragment_login,FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    @Inject
    lateinit var auth: FirebaseAuth

    lateinit var callbackManager:CallbackManager



    override fun initViews() {
        super.initViews()

        if (auth.currentUser != null)
        {
            gotoNewsList()
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create()

        binding.txtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginUsingFacebook.setOnClickListener {
            signInWithFacebook()
        }

        binding.goToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val emailText = binding.edtEmail.text.toString()
            val passwordText = binding.edtPassword.text.toString()
            viewModel.signInUser(emailText,passwordText)
        }

        binding.loginUsingGoogle.setOnClickListener {
            signIn()
        }

        binding.txtForgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
                .create()
            val view = layoutInflater.inflate(R.layout.forgot_password_alert_dialog,null)
            val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
            val editText = view.findViewById<EditText>(R.id.forgot_pass_email)
            builder.setView(view)
            button.setOnClickListener {
                viewModel.sendResetPassword(editText.text.toString())
                builder.dismiss()
            }
            builder.show()
        }
    }


    override fun observe() {
        super.observe()

        viewModel.userLiveData.observe(this, Observer {result->
            when(result.status)
            {
                Status.LOADING ->{
                    binding.btnLogin.visibility = View.INVISIBLE
                    binding.progressCircular.visibility = View.VISIBLE
                }

                Status.SUCCESS->{
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE
                    makeToast(result.data?: " Login Success")
                    gotoNewsList()
                }

                Status.ERROR->{
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE
                    makeToast(result.message?: "Error")
                }
            }
        })

        viewModel.gMailUserLiveData.observe(this, Observer { result->
            when(result.status)
            {
                Status.LOADING ->{
                    binding.btnLogin.visibility = View.INVISIBLE
                    binding.progressCircular.visibility = View.VISIBLE

                }

                Status.SUCCESS->{
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE
                    makeToast(result.data?: " Login Success")
                    gotoNewsList()
                }

                Status.ERROR->{
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE
                    makeToast(result.message?: "Error")


                }
            }
        })

        viewModel.sendResetPasswordLiveData.observe(this, Observer { result->
            when(result.status)
            {
                Status.LOADING ->{
                    binding.btnLogin.visibility = View.INVISIBLE
                    binding.progressCircular.visibility = View.VISIBLE

                }

                Status.SUCCESS->{
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE
                    makeToast("Password Reset Email Sent Pls Check Your Email")
                }

                Status.ERROR->{
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.signInWithGoogle(account!!)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn() {

        val signInIntent: Intent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private  fun gotoNewsList(){
        val intent = Intent(requireContext(),NewsListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
        ActivityCompat.finishAffinity(requireActivity());
    }


    private fun signInWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this,callbackManager, mutableListOf("email","public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    gotoNewsList()
                } else {
                    makeToast("Authentication Failed Pls Try Again")
                }
            }
    }
}