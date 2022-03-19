package com.makertech.assignmentquantamit.ui.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.makertech.assignmentquantamit.data.repository.AuthRepository
import com.makertech.assignmentquantamit.other.NetworkControl
import com.makertech.assignmentquantamit.other.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val networkControl: NetworkControl,
    private val firebaseAuth: FirebaseAuth
) :ViewModel() {
    private val _userLiveData = MutableLiveData<Resource<String>>()
    val userLiveData : LiveData<Resource<String>> = _userLiveData

    private val _gMailUserLiveData = MutableLiveData<Resource<String>>()
    val gMailUserLiveData : LiveData<Resource<String>> = _gMailUserLiveData

    private val _sendResetPasswordLiveData = MutableLiveData<Resource<String>>()
    val sendResetPasswordLiveData : LiveData<Resource<String>> = _sendResetPasswordLiveData

    fun signInUser(email: String, password: String){
        when {
            TextUtils.isEmpty(email) && TextUtils.isEmpty(password) -> {
                _userLiveData.postValue(Resource.error(null, "Enter email and password"))
            }
            networkControl.isConnected() -> {
                _userLiveData.postValue(Resource.loading(null))
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                    if (task.isSuccessful)
                    {
                        _userLiveData.postValue(Resource.success("Login Successful"))
                    }
                    else{
                        _userLiveData.postValue(Resource.error(null,
                            task.exception?.localizedMessage ?: "Error Occurred"))
                    }
                }
            }
            else -> {
                _userLiveData.postValue(Resource.error(null, "No internet connection"))
            }
        }
    }

    fun signInWithGoogle(acct: GoogleSignInAccount){
        repository.signInWithGoogle(acct).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _gMailUserLiveData.postValue(
                    Resource.success("Login Successfully")
                )
            } else {
                _gMailUserLiveData.postValue(Resource.error(null, "couldn't sign in user"))
            }

        } }

    fun sendResetPassword(email: String) {

        when {
            TextUtils.isEmpty(email) -> {
                _sendResetPasswordLiveData.postValue(Resource.error(null, "Enter registered email"))
            }
            networkControl.isConnected() -> {
                repository.sendForgotPassword(email).addOnCompleteListener { task ->
                    _sendResetPasswordLiveData.postValue(Resource.loading(null))
                    if (task.isSuccessful) {
                        _sendResetPasswordLiveData.postValue(Resource.success("Password Reset Email Sent"))
                    } else {
                        _sendResetPasswordLiveData.postValue(
                            Resource.error(
                                null,
                                task.exception?.message.toString()
                            )
                        )
                    }
                }
            }
            else -> {
                _sendResetPasswordLiveData.postValue(Resource.error(null, "No internet connection"))
            }
        }
    }


}