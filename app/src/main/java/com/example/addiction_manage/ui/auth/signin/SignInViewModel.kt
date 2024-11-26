package com.example.addiction_manage.ui.auth.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(): ViewModel(){
    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state = _state.asStateFlow()

    fun signIn(email: String, password: String){
        _state.value = SignInState.Loading
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password) // 비동기함수
            . addOnCompleteListener{task ->
                if(task.isSuccessful){
                    _state.value = SignInState.Success
                }else{
                    _state.value = SignInState.Error
                }
            }
    }

}

sealed class SignInState{ // 이 안에서 제안한 것만 자식으로 인정한다?
    object Nothing: SignInState()
    object Loading: SignInState()
    object Success: SignInState()
    object Error: SignInState()
}