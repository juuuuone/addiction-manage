package com.example.addiction_manage.feature.caffeine

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.Alcohol
import com.example.addiction_manage.feature.model.Caffeine
import com.example.addiction_manage.feature.model.Smoking
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CaffeineViewModel @Inject constructor() : ViewModel() {

    private val _caffeineRecords = MutableStateFlow<List<Caffeine>>(emptyList())
    val caffeineRecords = _caffeineRecords.asStateFlow()
    private val _friendCaffeineRecords = MutableStateFlow<List<Caffeine>>(emptyList())
    val friendCaffeineRecords = _friendCaffeineRecords.asStateFlow()

    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth
    val currentUser = firebaseAuth.currentUser
    val uid = currentUser?.uid!!
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    fun addCaffeineRecord(drinks: Int) {
        val email = currentUser?.email ?: return // 로그인하지 않은 경우 종료
        val caffeineRecord = Caffeine(
            id = currentUser.uid,
            email = email,
            drinks = drinks,
            createdAt = today
        )

        firebaseDatabase.reference.child("Caffeine").child(currentUser.uid).child(today)
            .setValue(caffeineRecord)
    }

    fun listenForCaffeineRecords() {
        firebaseDatabase.reference.child("Caffeine").child(uid).orderByChild("createdAt")
            .equalTo(today)
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

    fun listenForFriendCaffeineRecords(id: String) {
        firebaseDatabase.reference.child("Caffeine").child(id).orderByChild("createdAt")
            .equalTo(today)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Caffeine>()
                    snapshot.children.forEach { data ->
                        val caffeine = data.getValue(Caffeine::class.java)
                        caffeine?.let {
                            list.add(caffeine)
                        }
                    }
                    _friendCaffeineRecords.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getYesterdayCaffeineRecord(caffeineRecords: List<Caffeine>): Caffeine? {
        val yesterday =
            LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return caffeineRecords.find { it.createdAt == yesterday }
    }

    fun getTodayCaffeineRecord(caffeineRecords: List<Caffeine>): Caffeine? {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return caffeineRecords.find { data ->
            data.createdAt == today
        }
    }

    fun getCaffeineRecord(caffeineRecords: List<Caffeine>, date: LocalDate): Caffeine? {
        val today = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return caffeineRecords.find { data ->
            data.createdAt == today
        }
    }

    fun getWeekCaffeineRecord(caffeineRecords: List<Caffeine>): List<Pair<String, Int>> {
        val today = LocalDate.now()
        val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
        // 일주일 날짜를 초기화 (월요일부터 일요일까지)
        val weekDates = (0..6).map { offset ->
            val date = startOfWeek.plusDays(offset.toLong())
            date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
        val weekData = weekDates.associateWith { 0 }.toMutableMap()
        caffeineRecords.forEach { record ->
            val recordDate =
                LocalDate.parse(record.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val recordDateString = recordDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            if (recordDateString in weekData) {
                weekData[recordDateString] = weekData[recordDateString]!! + record.drinks
            }
        }
        return weekData.map { it.key to it.value }
    }

    fun getWeekTotalCaffeineRecord(caffeineRecords: List<Caffeine>): Int {
        val today = LocalDate.now()
        val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
        val endOfWeek = today.with(java.time.DayOfWeek.SUNDAY)
        var count = 0

        caffeineRecords.filter { record ->
            val recordDate =
                LocalDate.parse(record.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            recordDate in startOfWeek..endOfWeek
        }.forEach { data ->
            count += data.drinks
        }

        return count
    }
}