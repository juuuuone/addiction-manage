package com.example.addiction_manage.feature.alcohol

import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.Alcohol
import com.example.addiction_manage.feature.model.Caffeine
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
class AlcoholViewModel @Inject constructor() : ViewModel() {

    private val _alcoholRecords = MutableStateFlow<List<Alcohol>>(emptyList())
    val alcoholRecords = _alcoholRecords.asStateFlow()
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth

    init {
        getAlcoholRecords()
    }

    private fun getAlcoholRecords() {
        // 비동기 함수, DB에서 데이터 받아오기
        firebaseDatabase.getReference("Alcohol").get()
            .addOnSuccessListener {
                val list = mutableListOf<Alcohol>()
                it.children.forEach { data ->
                    val alcohol = Alcohol(data.key!!, data.value.toString())
                    list.add(alcohol)
                }
                _alcoholRecords.value = list
            }
    }

    fun addAlcoholRecord(doDrink: Boolean) {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid ?: return // 로그인하지 않은 경우 종료

        val alcoholRecord = Alcohol(
            id = firebaseDatabase.reference.child("Alcohol").push().key ?: UUID.randomUUID()
                .toString(),
            userId = uid,
            doDrink = doDrink,
        )

        firebaseDatabase.reference.child("Alcohol").child(uid).push().setValue(alcoholRecord)
    }

    fun listenForAlcoholRecords(userId: String) {
        firebaseDatabase.reference.child("Alcohol").child(userId).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Alcohol>()
                    snapshot.children.forEach { data ->
                        val alcohol = data.getValue(Alcohol::class.java)
                        alcohol?.let {
                            list.add(alcohol)
                        }
                    }
                    _alcoholRecords.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}