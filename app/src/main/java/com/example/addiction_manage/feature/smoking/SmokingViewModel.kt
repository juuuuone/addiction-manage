package com.example.addiction_manage.feature.smoking

import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.Alcohol
import com.example.addiction_manage.feature.model.Smoking
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
class SmokingViewModel @Inject constructor() : ViewModel() {

    private val _smokingRecords = MutableStateFlow<List<Smoking>>(emptyList())
    val smokingRecords = _smokingRecords.asStateFlow()
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth

    init {
        getSmokingRecords()
    }

    private fun getSmokingRecords() {
        // 비동기 함수, DB에서 데이터 받아오기
        firebaseDatabase.getReference("Smoking").get()
            .addOnSuccessListener {
                val list = mutableListOf<Smoking>()
                it.children.forEach { data ->
                    val smoking = Smoking(data.key!!, data.value.toString())
                    list.add(smoking)
                }
                _smokingRecords.value = list
            }
    }

    fun addSmokingRecord(cigarettes: Int) {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid ?: return // 로그인하지 않은 경우 종료

        val smokingRecord = Smoking(
            id = firebaseDatabase.reference.child("Smoking").push().key ?: UUID.randomUUID()
                .toString(),
            userId = uid,
            cigarettes = cigarettes,
        )

        firebaseDatabase.reference.child("Smoking").child(uid).push().setValue(smokingRecord)
    }

    fun listenForSmokingRecords(userId: String) {
        firebaseDatabase.reference.child("Smoking").child(userId).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Smoking>()
                    snapshot.children.forEach { data ->
                        val smoking = data.getValue(Smoking::class.java)
                        smoking?.let {
                            list.add(smoking)
                        }
                    }
                    _smokingRecords.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}