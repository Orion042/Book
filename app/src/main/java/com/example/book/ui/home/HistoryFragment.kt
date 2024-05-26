package com.example.book.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import com.example.book.R
import com.example.book.database.BookEntity
import com.example.book.databinding.FragmentHistoryBinding
import com.example.book.repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookRepository: BookRepository

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
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        bookRepository = BookRepository(requireContext())

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = listOf(
            mapOf("title" to "Book 1", "author" to "Author 1", "date" to "2023-08-01"),
            mapOf("title" to "Book 2", "author" to "Author 2", "date" to "2023-08-02")
        )

        binding.bookSearchHistoryList.adapter = SimpleAdapter(
            requireContext(),
            data,
            R.layout.books_search_history_list,
            arrayOf("title", "author", "date"),
            intArrayOf(R.id.book_title_textview, R.id.book_author_textview, R.id.book_search_time_textview)
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}