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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SmokingViewModel @Inject constructor() : ViewModel() {

    private val _smokingRecords = MutableStateFlow<List<Smoking>>(emptyList())
    val smokingRecords = _smokingRecords.asStateFlow()
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth

//    init {
//        getSmokingRecords()
//    }
//
//    private fun getSmokingRecords() {
//        // 비동기 함수, DB에서 데이터 받아오기
//        firebaseDatabase.getReference("Smoking").get()
//            .addOnSuccessListener {
//                val list = mutableListOf<Smoking>()
//                it.children.forEach { data ->
//                    // 데이터를 Map 형태로 변환하여 파싱
//                    val smoking = data.getValue(Smoking::class.java)
//                    smoking?.let {
//                        list.add(smoking)
//                    }
//                }
//                _smokingRecords.value = list
//            }
//    }

    fun addSmokingRecord(cigarettes: Int) {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid ?: return // 로그인하지 않은 경우 종료
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val smokingRecord = Smoking(
            id = firebaseDatabase.reference.child("Smoking").push().key ?: UUID.randomUUID()
                .toString(),
            userId = uid,
            cigarettes = cigarettes,
            createdAt = today
        )

        firebaseDatabase.reference.child("Smoking").child(uid).child(today).setValue(smokingRecord)
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

    fun getYesterdaySmokingRecord(smokingRecords: List<Smoking>): Smoking? {
        val yesterday =
            LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return smokingRecords.find { it.createdAt == yesterday }

    }

    fun getTodaySmokingRecord(smokingRecords: List<Smoking>): Smoking? {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
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