package com.makertech.assignmentquantamit.ui.register

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.makertech.assignmentquantamit.data.repository.AuthRepository
import com.makertech.assignmentquantamit.other.NetworkControl
import com.makertech.assignmentquantamit.other.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val networkControl: NetworkControl,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<String>>()
    val loginResult : LiveData<Resource<String>> = _loginResult


    fun signUpUser(email: String, password: String, fullName: String) {
        when {
            TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty( fullName ) -> {
                _loginResult.postValue(Resource.error(null, "Fields must not be empty"))
            }
            password.length < 8 -> {
                _loginResult.postValue( Resource.error(null, "Password must not be less than 8"))
            }
            networkControl.isConnected() -> {
                _loginResult.postValue(Resource.loading(null))
                firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                    if (it.result?.signInMethods?.size == 0) {
                        repository.signUpUser(email, password, fullName).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _loginResult.postValue( Resource.success(" User Registration Successful"))
                            } else {
                                _loginResult.postValue( Resource.error(null, it.exception?.message.toString()))
                            } }
                    } else {
                        _loginResult.postValue(Resource.error(null, "email already exist"))
                    } }
            } else -> {
            _loginResult.postValue(Resource.error(null, "No internet connection"))
            }
        }
    }
}