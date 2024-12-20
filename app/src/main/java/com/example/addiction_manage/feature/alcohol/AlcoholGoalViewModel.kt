package com.example.addiction_manage.feature.alcohol

import android.content.Context
import com.example.addiction_manage.feature.model.AlcoholGoal

import androidx.lifecycle.ViewModel
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
class AlcoholGoalViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth
    private val _goal = MutableStateFlow<List<AlcoholGoal>>(emptyList())
    val goal = _goal.asStateFlow()

    private val _isAlcoholChecked = MutableStateFlow(loadState())
    val isAlcoholChecked: StateFlow<Boolean> = _isAlcoholChecked

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val databaseReference = firebaseDatabase.getReference("AlcoholGoal")
    private var valueEventListener: ValueEventListener? = null

    fun setNoAlcoholChecked(checked: Boolean) {
        _isAlcoholChecked.value = checked
        saveState(checked)
    }

    private fun saveState(checked: Boolean) {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean("isAlcoholChecked_${firebaseAuth.currentUser?.uid}", checked).apply()
    }

    private fun loadState(): Boolean {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(
            "isAlcoholChecked_${firebaseAuth.currentUser?.uid}",
            false
        )
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
                    if (!_isAlcoholChecked.value) {
                        val alcoholGoal = snapshot.child(uid).getValue(AlcoholGoal::class.java)
                        _goal.value = if (alcoholGoal != null) {
                            listOf(alcoholGoal)
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
        valueEventListener?.let { databaseReference.removeEventListener(it) }
    }

    fun getCurrentUserGoal(): AlcoholGoal? {
        val uid = firebaseAuth.currentUser?.uid ?: return null
        return _goal.value.firstOrNull { it.id == uid }
    }

    fun addGoal(newGoal: String) {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid ?: return
        val alcoholGoal = AlcoholGoal(
            id = uid,
            goal = newGoal,
            createdAt = System.currentTimeMillis()
        )
        firebaseDatabase.reference.child("AlcoholGoal").child(uid).setValue(alcoholGoal)
    }
}
