package com.example.addiction_manage.ui.auth.signup

import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth

    fun signUp(name: String, email: String, password: String) {
        _state.value = SignUpState.Loading
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password) // 비동기함수
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { // 회원가입이 성공 했으면
                    task.result.user?.let {
                        it.updateProfile( // 이름 받는거
                            com.google.firebase.auth.UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(name)
                                .build()
                        ).addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // 프로필 업데이트가 성공하면 addUser 호출
                                addUser(email, name)
                                _state.value = SignUpState.Success
                            } else {
                                _state.value = SignUpState.Error
                            }
                        }
                    } ?: run {
                        _state.value = SignUpState.Error
                    }
                } else {
                    _state.value = SignUpState.Error
                }
            }
    }

    fun addUser(email: String, nickname: String) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val currentUser = firebaseAuth.currentUser
        val id = currentUser?.uid!!
        val user = User(
            id = id,
            email = email,
            nickname = nickname,
            createdAt = today
        )

        firebaseDatabase.reference.child("User").child(id).setValue(user)
    }
}

sealed class SignUpState { // 이 안에서 제안한 것만 자식으로 인정한다?
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    object Error : SignUpState()
}