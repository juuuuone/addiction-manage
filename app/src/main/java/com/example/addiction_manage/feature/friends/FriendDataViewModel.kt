package com.example.addiction_manage.feature.friends

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.addiction_manage.feature.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FriendDataViewModel @Inject constructor() : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()
    private val firebaseDatabase = Firebase.database
    private val firebaseAuth = Firebase.auth

    fun listenForUsers() {
        firebaseDatabase.reference.child("User")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<User>()
                    snapshot.children.forEach { data ->
                        val user = data.getValue(User::class.java)
                        user?.let {
                            list.add(user)
                        }
                    }
                    _users.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    // 가장 최근에 선택한 친구를 DefaultFriend로 설정한다
    fun setDefaultFriend(users: List<User>, friendId: String) {
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        val user = users.find { data -> data.id == userId }
        val friend = users.find { data -> data.id == friendId }

        if (user != null && friend != null) {
            val updates = mapOf(
                "defaultFriendEmail" to friend.email
            )
            firebaseDatabase.reference.child("User").child(currentUser?.uid!!)
                .updateChildren(updates)
        }
    }

    // 사용자의 defaultFriend의 nickname을 가져온다
    fun getDefaultFriendNickname(users: List<User>): String {
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        val user = users.find { data -> data.id == userId }
        val defaultFriendEmail = user?.defaultFriendEmail
        val friend = users.find { data -> data.email == defaultFriendEmail }
        return friend?.nickname ?: ""
    }

    // 친구 email 저장하기
    fun addFriend(users: List<User>, friendEmail: String) {
        val currentUser = firebaseAuth.currentUser
        val userEmail = currentUser?.email

        val user = users.find { data -> data.email == userEmail }
        val friend = users.find { data -> data.email == friendEmail }

        if (friend != null) {
            user?.friends?.add(friend)
        }
        firebaseDatabase.reference.child("User").child(currentUser?.uid!!).setValue(user)
    }

    // 친구 선택할 때 닉네임 리스트업
    fun getAllFriendsNickname(users: List<User>): List<String>? {
        val currentUser = firebaseAuth.currentUser
        val email = currentUser?.email

        return users.find { data ->
            data.email == email
        }?.friends?.map { data ->
            data.nickname
        }
    }

    // 선택한 친구의 id 가져오기
    fun getFriendId(users: List<User>, nickname: String): String {
        return users.find { data ->
            data.nickname == nickname
        }?.id ?: ""
    }
}
