package no.uia.ikt205.piano

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.fragment_piano_layout.view.*
import no.uia.ikt205.piano.data.Note
import no.uia.ikt205.piano.databinding.FragmentPianoLayoutBinding
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class PianoLayout : Fragment() {

    var onSave: ((file: Uri) -> Unit)? = null

    private var _binding:FragmentPianoLayoutBinding ? = null
    private val binding get() = _binding!!

    private val whitekeys = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2")
    private val blackkeys = listOf("C#", "D#", "F#", "G#", "A#", "C2#", "D2#", "F2#", "G2#")

    private var score:MutableList<Note> = mutableListOf<Note>()


    fun millisecondsToDescriptiveTime(ms:Long):String{
        val sec = ((ms/1000)%60)
        println("seconds in desc: $sec")
        val milliseconds = ms
        println("milliseconds in desc: $milliseconds")
        var newTime:Long = 0
        if(sec < 1){
            newTime = milliseconds
        } else {
            newTime = (1000 - ms)
        }

        if (ms > 1000){
            newTime = (1000-ms)*-1
        }

        return String.format("%02d.%02d", sec, newTime)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPianoLayoutBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentmanager = childFragmentManager
        var fragmentTransaction = fragmentmanager.beginTransaction()
        var initialTime = System.currentTimeMillis()

        whitekeys.forEach{
            val whitePianoKey = WhiteKeysFragment.newInstance(it)
            var startPlay:Long = 0
            var endPlay:Long = 0
            whitePianoKey.onKeyDown = {note ->
                startPlay = System.currentTimeMillis()
                println("White piano key down $note")
            }

            whitePianoKey.onKeyUp = { note ->
                endPlay = System.currentTimeMillis()
                val totalPlay = endPlay - startPlay
                println("White piano key up $note")
                if (score.isNullOrEmpty()){
                    initialTime = System.currentTimeMillis()
                    val note = Note(it, millisecondsToDescriptiveTime(initialTime - initialTime), millisecondsToDescriptiveTime(totalPlay))
                    score.add(note)
                } else{
                    val note = Note(it, millisecondsToDescriptiveTime(endPlay - initialTime), millisecondsToDescriptiveTime(totalPlay))
                    score.add(note)
                }
            }

            fragmentTransaction.add(view.whiteKeysLayout.id, whitePianoKey, "note_$it")
        }

        blackkeys.forEach{
            val blackPianoKey = BlackKeysFragment.newInstance(it)

            var startPlay:Long = 0
            var endPlay:Long = 0

            blackPianoKey.onKeyDown = { note ->
                startPlay = System.currentTimeMillis()
                println("Black piano key down $note")
            }

            blackPianoKey.onKeyUp = { note ->
                endPlay = System.currentTimeMillis()
                val totalPlay = endPlay - startPlay
                println("Black piano key up $note")
                if (score.isNullOrEmpty()){
                    initialTime = System.currentTimeMillis()
                    val note = Note(it, millisecondsToDescriptiveTime(initialTime - initialTime), millisecondsToDescriptiveTime(totalPlay))
                    score.add(note)
                } else{
                    val note = Note(it, millisecondsToDescriptiveTime(endPlay - initialTime), millisecondsToDescriptiveTime(totalPlay))
                    score.add(note)
                }
            }

            fragmentTransaction.add(view.blackKeysLayout.id, blackPianoKey, "note_$it")
        }

        fragmentTransaction.commit()


        view.saveScoreBt.setOnClickListener{
            var fileName = view.fileNameTextEdit.text.toString()
            if(score.count() > 0 && fileName.isNotEmpty()){
                fileName = "$fileName.musikk"
                val content:String = score.map{it.toString()}.reduce {acc, s -> acc + s + "\n"}
                saveFile(fileName, content)
            }
            else{
                println("something is wrong")
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun saveFile(fileName:String, content:String){
        val path = this.activity?.getExternalFilesDir(null)
        val file=File(path,fileName)
        if (path != null){
            if (!file.exists()){
                FileOutputStream(file,true).bufferedWriter().use {writer ->
                    writer.write(content)
                }

                this.onSave?.invoke(file.toUri());
                Toast.makeText(activity,"File sucessfully saved as $fileName", Toast.LENGTH_LONG).show()
                score.clear()
            } else {
                Toast.makeText(activity,"File already exists", Toast.LENGTH_LONG).show()
            }
        } else{
            //no external path
            Toast.makeText(activity, "No path found", Toast.LENGTH_LONG).show()
        }
    }

}