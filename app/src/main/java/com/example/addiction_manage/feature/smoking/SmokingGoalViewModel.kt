package com.example.addiction_manage.feature.smoking

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.SmokingGoal
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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

    private val _isNoSmokingChecked = MutableStateFlow(false)
    val isNoSmokingChecked = _isNoSmokingChecked.asStateFlow()

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
                    val list = mutableListOf<SmokingGoal>()
                    snapshot.children.forEach { data ->
                        val smokingGoal = data.getValue(SmokingGoal::class.java)
                        if (smokingGoal?.userId == uid) {
                            list.add(smokingGoal)
                        }
                    }
                    _goal.value = list // Flow 상태 업데이트
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

    fun setNoSmokingChecked(checked: Boolean) { _isNoSmokingChecked.value = checked }

    fun addGoal(newGoal: String){
        val currentUser = firebaseAuth.currentUser
        val key = firebaseDatabase.reference.child("SmokingGoal").push().key ?: UUID.randomUUID().toString()
        val smokingGoal = SmokingGoal(
            id = key,
            userId = currentUser?.uid?: "",
            goal = newGoal
        )
        firebaseDatabase.reference.child("SmokingGoal").push().setValue(smokingGoal)
    }
}
