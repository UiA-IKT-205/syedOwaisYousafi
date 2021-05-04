package com.example.mypiano

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.mypiano.data.Note
import com.example.mypiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream

class Piano : Fragment() {

    var onSave:((file: Uri) -> Unit)? = null

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val naturals = listOf("C", "D", "E" ,"F", "G", "A", "B", "C2", "D2", "E2") // White keys on the piano
    private val sharps = listOf("C#", "D#", "F#", "G#", "A#","C#2","D#2", "F#2", "G#2", "A#2") // Black keys on the piano
    private var score:MutableList<Note> = mutableListOf<Note>() // Score == sheet music

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)

        val view = binding.root
        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        naturals.forEach{ originalNoteVal ->
            val naturalKeys = NaturalKeysFragment.newInstance(originalNoteVal)
            val sharpKeys = SharpKeysFragment.newInstance(sharps[naturals.indexOf(originalNoteVal)])
            var startPlay:Long = 0

            naturalKeys.onKeyDown = { note ->
                println("Piano key down $note")
                startPlay = System.nanoTime()
            }

            naturalKeys.onKeyUp = {
                var endPlay = System.nanoTime()
                val note = Note(it, startPlay, endPlay)
                score.add(note)
                println("Piano key up $note")
            }

            sharpKeys.onKeyDown = { note ->
                println("Sharp down $note")
                startPlay = System.nanoTime()
            }

            sharpKeys.onKeyUp = {
                var endPlay = System.nanoTime()
                val note = Note(it, startPlay, endPlay)
                score.add(note)
                println("Sharp up $note")
            }

            ft.add(view.pianoKeys.id, naturalKeys, "note_$originalNoteVal")

            ft.add(view.pianoKeys.id, sharpKeys, "note_$originalNoteVal")
        }

        ft.commit()

        view.saveScoreBt.setOnClickListener {
            var fileName = view.fileNameTextEdit.text.toString()
            val path = this.activity?.getExternalFilesDir(null)
            if (score.count() > 0 && path != null){

                if(fileName.isNotEmpty()){

                    fileName = "$fileName.music"
                    if (checkFileName(fileName, path)){
                        print("$fileName does exist. Please try another name. \n")

                    } else {
                        val file = File(path, fileName)
                        FileOutputStream(file, true).bufferedWriter().use { writer ->
                            // bufferedWriter exists within this scope
                            score.forEach {
                                writer.write( "${it.toString()}\n")
                            }

                            this.onSave?.invoke(file.toUri());
                        }
                        println("$fileName saved to $path")
                    }
                } else {
                    println("Please provide a name for the file. \n")
                }
            } else {
                println("File is empty! \n")
            }
        }
        return view
    }

    fun checkFileName(name: String, path: File): Boolean { // Function checks if name is already in use, given path
        var file = File(path, name)
        return file.exists()
    }

}