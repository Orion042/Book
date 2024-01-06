package com.example.book.ui.home

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.book.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var radioButtonBookId : RadioButton
    private lateinit var radioButtonBookTitle : RadioButton
    private lateinit var editText : EditText
    private lateinit var searchButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioButtonBookId = view.findViewById(R.id.radioButtonBookId)
        radioButtonBookTitle = view.findViewById(R.id.radioButtonBookTitle)
        editText = view.findViewById(R.id.editTextText)
        searchButton = view.findViewById(R.id.searchButton)

        radioButtonBookId.isChecked = true
        editText.setHint(resources.getString(R.string.search_book_id))

        radioButtonBookId.setOnClickListener {
            onRadioButtonClicked(R.id.radioButtonBookId)
        }

        radioButtonBookTitle.setOnClickListener {
            onRadioButtonClicked(R.id.radioButtonBookTitle)
        }

    }

    fun onRadioButtonClicked(selectedId : Int) {
        when(selectedId) {
            R.id.radioButtonBookId -> {
                Toast.makeText(requireContext(), "id", Toast.LENGTH_SHORT).show()
                editText.setHint(resources.getString(R.string.search_book_id))
            }
            R.id.radioButtonBookTitle -> {
                Toast.makeText(requireContext(), "title", Toast.LENGTH_SHORT).show()
                editText.setHint(resources.getString(R.string.search_book_title))
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}