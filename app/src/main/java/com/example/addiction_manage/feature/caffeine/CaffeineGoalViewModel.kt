package com.example.addiction_manage.feature.caffeine

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.CaffeineGoal
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
import javax.inject.Inject

@HiltViewModel
class CaffeineGoalViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth
    private val _goal = MutableStateFlow<List<CaffeineGoal>>(emptyList())
    val goal = _goal.asStateFlow()

    private val _isCaffeineChecked = MutableStateFlow(loadState())
    val isCaffeineChecked: StateFlow<Boolean> = _isCaffeineChecked

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val databaseReference = firebaseDatabase.getReference("CaffeineGoal")
    private var valueEventListener: ValueEventListener? = null

    fun setNoCaffeineChecked(checked: Boolean) {
        _isCaffeineChecked.value = checked
        saveState(checked)
    }

    private fun saveState(checked: Boolean) {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isCaffeineChecked_${firebaseAuth.currentUser?.uid}", checked).apply()
    }

    private fun loadState(): Boolean {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isCaffeineChecked_${firebaseAuth.currentUser?.uid}", false)
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
                    if (!_isCaffeineChecked.value) {
                        val caffeineGoal = snapshot.child(uid).getValue(CaffeineGoal::class.java)
                        _goal.value = if (caffeineGoal != null) {
                            listOf(caffeineGoal)
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


    fun addGoal(newGoal: String){
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid ?: return
        val caffeineGoal = CaffeineGoal(
            id = uid, // 유저 ID를 그대로 사용
            userId = uid,
            goal = newGoal,
            createdAt = System.currentTimeMillis()
        )
        firebaseDatabase.reference.child("CaffeineGoal").child(uid).setValue(caffeineGoal)
    }
}
