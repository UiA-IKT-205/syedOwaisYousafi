package com.example.mypiano

import android.net.Uri
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mypiano.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity() {

    private val Tag:String = "MyPiano:MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth

    private lateinit var piano:Piano

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        signInAnonymously()

        piano = supportFragmentManager.findFragmentById(binding.piano.id) as Piano

        piano.onSave = {
            this.upload(it)
        }

    }

    private fun upload(file:Uri){

        Log.d(Tag, "Upload file $file")

        val ref = FirebaseStorage.getInstance().reference.child("songs/${file.lastPathSegment}")
        var uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(Tag, "Successfullly stored ${it.toString()}")
        }.addOnFailureListener {
            Log.e(Tag, "Upload to Firebase failed", it)
        }
    }

    private fun signInAnonymously(){

        auth.signInAnonymously().addOnSuccessListener {
            Log.d(Tag, "Login success ${it.user.toString()}")
        }.addOnFailureListener {
            Log.e(Tag, "Login failed", it)
        }
    }

}