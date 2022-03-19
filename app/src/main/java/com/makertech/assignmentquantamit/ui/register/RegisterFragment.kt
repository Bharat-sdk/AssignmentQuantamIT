package com.makertech.assignmentquantamit.ui.register

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.room.Index
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.makertech.assignmentquantamit.R
import com.makertech.assignmentquantamit.databinding.FragmentRegisterBinding
import com.makertech.assignmentquantamit.other.Status
import com.makertech.assignmentquantamit.ui.base.BaseFragment
import com.makertech.assignmentquantamit.ui.news.NewsListActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment:BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register,FragmentRegisterBinding::inflate) {
    private val registerViewModel : RegisterViewModel by viewModels()


    override fun initViews() {
        super.initViews()

        binding.btnRegister.setOnClickListener {
            val email:String
            val password:String
            val name:String
                binding.apply {
                 email = edtEmail.text.toString()
                 password = edtPassword.text.toString()
                 name = edtName.text.toString()
            }
            if(binding.chkTermsConditions.isChecked)
            {
                registerViewModel.signUpUser(email , password ,name)
            }
            else{
                makeToast("Please agree to the terms and conditions")
            }

        }

        binding.goToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun observe() {
        super.observe()
        registerViewModel.loginResult.observe(this, Observer { result->
            result?.let {
                when(result.status)
                {
                    Status.LOADING ->{
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.btnRegister.visibility = View.INVISIBLE
                    }

                    Status.SUCCESS ->{
                        makeToast(result.data!!)
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    Status.ERROR ->{
                        binding.progressCircular.visibility = View.INVISIBLE
                        binding.btnRegister.visibility = View.VISIBLE
                        makeToast("Error : "+result.message)

                    }
                }
            }
        })
    }

}