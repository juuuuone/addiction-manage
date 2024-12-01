package com.example.addiction_manage.feature.smoking

import android.content.Context
import android.util.Log
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class SmokingGoalViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth
    private val _goal = MutableStateFlow<List<SmokingGoal>>(emptyList())
    val goal = _goal.asStateFlow()

    private val _isSmokingChecked = MutableStateFlow(loadState())
    val isSmokingChecked: StateFlow<Boolean> = _isSmokingChecked

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val databaseReference = firebaseDatabase.getReference("SmokingGoal")
    private var valueEventListener: ValueEventListener? = null

    fun setNoSmokingChecked(checked: Boolean) {
        _isSmokingChecked.value = checked
        saveState(checked)
    }

    private fun saveState(checked: Boolean) {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isSmokingChecked_${firebaseAuth.currentUser?.uid}", checked).apply()
        // 현재 유저한테만 상태 저장 (계정간 상태 공유 x)
    }

    private fun loadState(): Boolean {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isSmokingChecked_${firebaseAuth.currentUser?.uid}", false)
    }

    init {
        fetchGoalsAutomatically()
    }


    private fun fetchGoalsAutomatically() {
        _isLoading.value = true
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val uid = user.uid
            valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // 체크 상태에 따라 데이터 불러오기 또는 비우기
                    Log.d("re", isSmokingChecked.value.toString())
                    if (!_isSmokingChecked.value) {
                        val smokingGoal = snapshot.child(uid).getValue(SmokingGoal::class.java)
                        _goal.value = if (smokingGoal != null) {
                            listOf(smokingGoal)
                        } else {
                            emptyList()
                        }
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

    fun getCurrentUserGoal(): SmokingGoal? {
        val uid = firebaseAuth.currentUser?.uid ?: return null
        return _goal.value.firstOrNull { it.userId == uid }
    }

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
