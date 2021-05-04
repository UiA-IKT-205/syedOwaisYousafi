package no.uia.ikt205.piano

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import no.uia.ikt205.piano.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG:String = "MyPiano:MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth

    private lateinit var piano:PianoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        signInAnonymously()

        piano = supportFragmentManager.findFragmentById(binding.mainPiano.id) as PianoLayout

        piano.onSave = {
            this.upload(it)
        }

    }

    private fun upload(file: Uri){
        Log.d(TAG,"upload file $file")
        val ref = FirebaseStorage.getInstance().reference.child("melodies/${file.lastPathSegment}")
        var uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(TAG, "Saved on the cloud successfully ${it.toString()}")
        }.addOnFailureListener{
            Log.e(TAG, "failed to save on cloud", it)

        }
    }

    private fun signInAnonymously(){
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login successful ${it.user.toString()}")
        }.addOnFailureListener{
            Log.e(TAG, "Login failed", it)

        }
    }
}