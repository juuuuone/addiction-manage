package com.example.addiction_manage.feature.caffeine

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.Caffeine
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CaffeineViewModel @Inject constructor() : ViewModel() {

    private val _caffeineRecords = MutableStateFlow<List<Caffeine>>(emptyList())
    val caffeineRecords = _caffeineRecords.asStateFlow()
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth

    init {
        getCaffeineRecords()
    }

    private fun getCaffeineRecords() {
        // 비동기 함수, DB에서 데이터 받아오기
        firebaseDatabase.getReference("Caffeine").get()
            .addOnSuccessListener {
                val list = mutableListOf<Caffeine>()
                it.children.forEach { data ->
                    val caffeine = Caffeine(data.key!!, data.value.toString())
                    list.add(caffeine)
                }
                _caffeineRecords.value = list
            }
    }

    fun addCaffeineRecord(drinks: Int) {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid ?: return // 로그인하지 않은 경우 종료

        val caffeineRecord = Caffeine(
            id = firebaseDatabase.reference.child("Caffeine").push().key ?: UUID.randomUUID()
                .toString(),
            userId = uid,
            drinks = drinks,
        )

        firebaseDatabase.reference.child("Caffeine").child(uid).push().setValue(caffeineRecord)
    }

    fun listenForCaffeineRecords(userId: String) {
        firebaseDatabase.reference.child("Caffeine").child(userId).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Caffeine>()
                    snapshot.children.forEach { data ->
                        val caffeine = data.getValue(Caffeine::class.java)
                        caffeine?.let {
                            list.add(caffeine)
                        }
                    }
                    _caffeineRecords.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}