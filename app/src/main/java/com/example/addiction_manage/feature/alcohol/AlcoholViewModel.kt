package com.example.addiction_manage.feature.alcohol

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.Alcohol
import com.example.addiction_manage.feature.model.Caffeine
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AlcoholViewModel @Inject constructor() : ViewModel() {

    private val _alcoholRecords = MutableStateFlow<List<Alcohol>>(emptyList())
    val alcoholRecords = _alcoholRecords.asStateFlow()
    private val _friendAlcoholRecords = MutableStateFlow<List<Alcohol>>(emptyList())
    val friendAlcoholRecords = _friendAlcoholRecords.asStateFlow()

    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth
    val currentUser = firebaseAuth.currentUser
    val uid = currentUser?.uid!!
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    fun addAlcoholRecord(doDrink: Boolean) {
        val email = currentUser?.email ?: return
        val alcoholRecord = Alcohol(
            id = currentUser.uid,
            email = email,
            doDrink = doDrink,
            createdAt = today
        )
        firebaseDatabase.reference.child("Alcohol").child(currentUser.uid).child(today)
            .setValue(alcoholRecord)
    }

    fun listenForAlcoholRecords() {
        firebaseDatabase.reference.child("Alcohol").child(uid).orderByChild("createdAt")
//            .equalTo(today)
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

    fun listenForFriendAlcoholRecords(id: String) {
        firebaseDatabase.reference.child("Alcohol").child(id).orderByChild("createdAt")
            .equalTo(today)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Alcohol>()
                    snapshot.children.forEach { data ->
                        val alcohol = data.getValue(Alcohol::class.java)
                        alcohol?.let {
                            list.add(alcohol)
                        }
                    }
                    _friendAlcoholRecords.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getYesterdayAlcoholRecord(alcoholRecords: List<Alcohol>): Alcohol? {
        val yesterday =
            LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return alcoholRecords.find { data ->
            data.createdAt == yesterday
        }
    }

    fun getTodayAlcoholRecord(alcoholRecords: List<Alcohol>): Alcohol? {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return alcoholRecords.find { data ->
            data.createdAt == today
        }
    }

    fun getAlcoholRecord(alcoholRecords: List<Alcohol>, date: LocalDate): Alcohol? {
        val day = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return alcoholRecords.find { data ->
            data.createdAt == day
        }
    }

    fun getWeekAlcoholRecord(alcoholRecords: List<Alcohol>): List<Pair<String, Int>> {
        val today = LocalDate.now()
        val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
        // 일주일 날짜를 초기화 (월요일부터 일요일까지)
        val weekDates = (0..6).map { offset ->
            val date = startOfWeek.plusDays(offset.toLong())
            date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
        val weekData = weekDates.associateWith { 0 }.toMutableMap()
        alcoholRecords.forEach { record ->
            val recordDate =
                LocalDate.parse(record.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val recordDateString = recordDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            if (recordDateString in weekData) {
                weekData[recordDateString] =
                    weekData[recordDateString]!! + if (record.doDrink) 1 else 0
            }
        }
        return weekData.map { it.key to it.value }
    }

    fun getWeekTotalAlcoholRecord(alcoholRecords: List<Alcohol>): Int {
        val today = LocalDate.now()
        val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
        val endOfWeek = today.with(java.time.DayOfWeek.SUNDAY)
        var count = 0

        alcoholRecords.filter { record ->
            val recordDate =
                LocalDate.parse(record.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            recordDate in startOfWeek..endOfWeek
        }.forEach { data ->
            count += if (data.doDrink) 1 else 0
        }

        return count
    }
}