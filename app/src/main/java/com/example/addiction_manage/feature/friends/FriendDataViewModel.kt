package com.example.addiction_manage.feature.friends

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

    fun listenForUsers(email: String) {
        firebaseDatabase.reference.child("User").orderByChild("createdAt")
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

    fun addFriend(users: List<User>, email: String) {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid

        val user = users.find { data -> data.id == uid }
        val friend = users.find { data -> data.email == email }

        if (friend != null) {
            user?.friends?.add(friend)
        }
    }

    fun getAllFriends(users: List<User>): List<String>? {
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid

        return users.find { data ->
            data.id == uid
        }?.friends?.map { data ->
            data.nickname
        }
    }

}