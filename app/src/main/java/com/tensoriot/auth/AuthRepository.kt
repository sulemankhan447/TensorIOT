package com.tensoriot.auth

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.tensoriot.Constants
import com.tensoriot.model.LoginRequestModel
import com.tensoriot.model.User
import com.tensoriot.other.SharedPrefHelper
import com.tensoriot.other.UiState
import com.tensoriot.utils.ImageUtils
import javax.inject.Inject

interface AuthRepository {
    fun registerUser(request: User)
    fun loginUser(request: LoginRequestModel)
}

class FirebaseRepository @Inject constructor(
    val mAuth: FirebaseAuth,
    val mDatabase: FirebaseDatabase,
    val mStorage: FirebaseStorage,
    val sharedPrefHelper: SharedPrefHelper
) : AuthRepository {

    var statusLiveData = MutableLiveData<UiState<Nothing>>()


    override fun registerUser(request: User) {
        statusLiveData.value = UiState.Loading()
        request.profile?.let {

            val uploader = mStorage.reference
                .child("profile")
                .child(ImageUtils.getNameForProfile())

                uploader.putFile(Uri.parse(it)).addOnSuccessListener {
                    uploader.downloadUrl.addOnSuccessListener { profileUrl ->
                        request.profile = profileUrl.toString()
                        registerUserInFirebase(request)
                    }.addOnFailureListener {
                        statusLiveData.value = UiState.Error("")
                    }
                }.addOnFailureListener {
                    statusLiveData.value = UiState.Error("")
                }
        } ?: run {
            registerUserInFirebase(request)
        }


    }

    override fun loginUser(request: LoginRequestModel) {
        statusLiveData.value = UiState.Loading()
        mAuth.signInWithEmailAndPassword(request.email ?: "", request.password ?: "")
            .addOnCompleteListener { signInTask ->
                if (signInTask.isSuccessful) {
                    val query = mDatabase.getReference(Constants.USERS_TABLE)
                        .child(mAuth.uid ?: "")
                    val eventListener: ValueEventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            val username = dataSnapshot.child(Constants.USERNAME.toLowerCase())
                                .getValue(String::class.java)
                            val profilePhoto = dataSnapshot.child(Constants.PROFILE.toLowerCase())
                                .getValue(String::class.java)
                            val shortBio = dataSnapshot.child(Constants.SHORT_BIO.toLowerCase())
                                .getValue(String::class.java)
                            sharedPrefHelper.saveString(Constants.USERNAME, username ?: "")
                            sharedPrefHelper.saveBoolean(Constants.IS_LOGIN, true)
                            sharedPrefHelper.saveString(Constants.PROFILE, profilePhoto ?: "")
                            sharedPrefHelper.saveString(Constants.SHORT_BIO, shortBio ?: "")
                            statusLiveData.value = UiState.Success()

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            statusLiveData.value = UiState.Error("")
                        }
                    }
                    query.addListenerForSingleValueEvent(eventListener)

                } else {
                    statusLiveData.value = UiState.Error(signInTask.exception?.message)
                }
            }
    }

    private fun registerUserInFirebase(request: User) {
        mAuth.createUserWithEmailAndPassword(request.email ?: "", request.password ?: "")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mDatabase.getReference(Constants.USERS_TABLE)
                        .child(mAuth.currentUser?.uid ?: "").setValue(request)
                        .addOnCompleteListener { userTask ->
                            if (userTask.isSuccessful) {
                                sharedPrefHelper.saveString(
                                    Constants.PROFILE,
                                    request.profile ?: ""
                                )
                                sharedPrefHelper.saveString(
                                    Constants.USERNAME,
                                    request.username ?: ""
                                )
                                sharedPrefHelper.saveString(
                                    Constants.SHORT_BIO,
                                    request.shortBio ?: ""
                                )
                                sharedPrefHelper.saveBoolean(Constants.IS_LOGIN, true)
                                statusLiveData.value = UiState.Success()
                            } else {
                                statusLiveData.value =
                                    UiState.Error(userTask.exception?.message ?: "")

                            }
                        }

                } else {
                    statusLiveData.value = UiState.Error(task.exception?.message ?: "")
                }
            }
    }
}

class SQLRepository @Inject constructor() : AuthRepository {

    var statusLiveData = MutableLiveData<UiState<Nothing>>()


    override fun registerUser(request: User) {
        /**
         * Implementation of api call
         */
    }

    override fun loginUser(request: LoginRequestModel) {
        /**
         * Implementation of api call
         */
    }

}