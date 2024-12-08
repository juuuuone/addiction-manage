package com.example.addiction_manage.feature.smoking

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
class SmokingViewModel @Inject constructor() : ViewModel() {

    private val _smokingRecords = MutableStateFlow<List<Smoking>>(emptyList())
    val smokingRecords = _smokingRecords.asStateFlow()
    private val _friendSmokingRecords = MutableStateFlow<List<Smoking>>(emptyList())
    val friendSmokingRecords = _friendSmokingRecords.asStateFlow()

    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth
    val currentUser = firebaseAuth.currentUser
    val uid = currentUser?.uid!!
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    fun addSmokingRecord(cigarettes: Int) {
        val email = currentUser?.email ?: return // 로그인하지 않은 경우 종료
        val smokingRecord = Smoking(
            id = currentUser.uid,
            email = email,
            cigarettes = cigarettes,
            createdAt = today
        )

        firebaseDatabase.reference.child("Smoking").child(currentUser.uid).child(today)
            .setValue(smokingRecord)
    }

    fun listenForSmokingRecords() {
        firebaseDatabase.reference.child("Smoking").child(uid).orderByChild("createdAt")
//            .equalTo(today)
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

    fun listenForFriendSmokingRecords(id: String) {
        firebaseDatabase.reference.child("Smoking").child(id).orderByChild("createdAt")
            .equalTo(today)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Smoking>()
                    snapshot.children.forEach { data ->
                        val smoking = data.getValue(Smoking::class.java)
                        smoking?.let {
                            list.add(smoking)
                        }
                    }
                    _friendSmokingRecords.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getYesterdaySmokingRecord(smokingRecords: List<Smoking>): Smoking? {
        val yesterday =
            LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return smokingRecords.find { it.createdAt == yesterday }

    }

    fun getTodaySmokingRecord(smokingRecords: List<Smoking>): Smoking? {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return smokingRecords.find { it.createdAt == today }
    }

    fun getSmokingRecord(smokingRecords: List<Smoking>, date: LocalDate): Smoking? {
        val today = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return smokingRecords.find { it.createdAt == today }
    }

    fun getWeekSmokingRecord(smokingRecords: List<Smoking>): List<Pair<String, Int>> {
        val today = LocalDate.now()
        val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
        // 일주일 날짜를 초기화 (월요일부터 일요일까지)
        val weekDates = (0..6).map { offset ->
            val date = startOfWeek.plusDays(offset.toLong())
            date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
        val weekData = weekDates.associateWith { 0 }.toMutableMap()
        smokingRecords.forEach { record ->
            val recordDate =
                LocalDate.parse(record.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val recordDateString = recordDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            if (recordDateString in weekData) {
                weekData[recordDateString] = weekData[recordDateString]!! + record.cigarettes
            }
        }
        return weekData.map { it.key to it.value }
    }

    fun getWeekTotalSmokingRecord(smokingRecords: List<Smoking>): Int {
        val today = LocalDate.now()
        val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
        val endOfWeek = today.with(java.time.DayOfWeek.SUNDAY)
        var count = 0

        smokingRecords.filter { record ->
            val recordDate =
                LocalDate.parse(record.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            recordDate in startOfWeek..endOfWeek
        }.forEach { data ->
            count += data.cigarettes
        }

        return count
    }
}