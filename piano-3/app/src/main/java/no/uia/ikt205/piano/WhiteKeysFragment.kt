package no.uia.ikt205.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_white_keys.view.*
import no.uia.ikt205.piano.databinding.FragmentWhiteKeysBinding


class WhiteKeysFragment : Fragment() {

    private var _binding:FragmentWhiteKeysBinding? = null
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

        _binding = FragmentWhiteKeysBinding.inflate(inflater)
        val view = binding.root

        view.WhiteKeyButton.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean{
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@WhiteKeysFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@WhiteKeysFragment.onKeyUp?.invoke(note)
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
            WhiteKeysFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}