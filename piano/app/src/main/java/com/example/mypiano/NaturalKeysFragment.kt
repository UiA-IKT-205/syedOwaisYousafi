package com.example.mypiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_natural_keys.view.*
import com.example.mypiano.databinding.FragmentNaturalKeysBinding

class NaturalKeysFragment : Fragment() {

    private var _binding:FragmentNaturalKeysBinding? = null
    private val binding get() = _binding!! // remember to add !!
    private lateinit var note:String

    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?" // Elvis operator to allow default value in case of NULL
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNaturalKeysBinding.inflate(inflater)
        val view = binding.root // remember to add !! at binding get()

        view.naturalKey.setOnTouchListener(object: View.OnTouchListener{

            var startTime = 0

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@NaturalKeysFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@NaturalKeysFragment.onKeyUp?.invoke(note)
                }
                return true
            }
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            NaturalKeysFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}