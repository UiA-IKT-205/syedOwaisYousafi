package no.uia.ikt205.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_black_keys.view.*
import no.uia.ikt205.piano.databinding.FragmentBlackKeysBinding


class BlackKeysFragment : Fragment() {

    private var _binding: FragmentBlackKeysBinding? = null
    private val binding get() = _binding!!
    private lateinit var note:String

    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE")?: "?"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBlackKeysBinding.inflate(inflater)
        val view = binding.root

        view.BlackKeysButton.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean{
                when(event?.action){

                    MotionEvent.ACTION_DOWN -> this@BlackKeysFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@BlackKeysFragment.onKeyUp?.invoke(note)
                }
                return true
            }
        })

        // Inflate the layout for this fragment
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(note: String) =
            BlackKeysFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}