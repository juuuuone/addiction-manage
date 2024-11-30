package com.example.addiction_manage.feature.smoking

import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.AlcoholGoal
import com.example.addiction_manage.feature.model.SmokingGoal
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SmokingGoalViewModel @Inject constructor() : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth
    private val _goal = MutableStateFlow<List<SmokingGoal>>(emptyList())
    val goal = _goal.asStateFlow()

    private val _isSmokingChecked = MutableStateFlow(false)
    val isSmokingChecked = _isSmokingChecked.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val databaseReference = firebaseDatabase.getReference("SmokingGoal")
    private var valueEventListener: ValueEventListener? = null

    init {
        fetchGoalsAutomatically()
    }

    // 자동으로 데이터 가져오기
    private fun fetchGoalsAutomatically() {
        _isLoading.value = true
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val uid = user.uid
            valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val smokingGoal = snapshot.child(uid).getValue(SmokingGoal::class.java)
                    if (smokingGoal != null) {
                        _goal.value = listOf(smokingGoal) // 단일 데이터를 리스트로 변환하여 Flow 업데이트
                    } else {
                        _goal.value = emptyList()
                    }
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _isLoading.value = false
                }
            }
            databaseReference.addValueEventListener(valueEventListener!!)
        } ?: run {
            _goal.value = emptyList()
            _isLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel이 클리어되면 리스너 제거
        valueEventListener?.let { databaseReference.removeEventListener(it) }
    }

    fun setNoSmokingChecked(checked: Boolean) { _isSmokingChecked.value = checked }

    fun addGoal(newGoal: String){
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid ?: return // 로그인하지 않은 경우 종료
        val smokingGoal = SmokingGoal(
            id = uid, // 유저 ID를 그대로 사용
            userId = uid,
            goal = newGoal,
            createdAt = System.currentTimeMillis()
        )
        firebaseDatabase.reference.child("SmokingGoal").child(uid).setValue(smokingGoal)
    }
}
