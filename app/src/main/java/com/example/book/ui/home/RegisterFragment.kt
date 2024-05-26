package com.example.book.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.book.MainActivity
import com.example.book.R
import com.example.book.databinding.FragmentHistoryBinding
import com.example.book.databinding.FragmentRegisterBinding
import com.example.book.makeHttpPost
import com.example.book.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegisterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = "RegisterFragment"

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = activity as MainActivity?

        binding.registerBookInfoButton.setOnClickListener {
            if(isEditTextEmpty()) {
                return@setOnClickListener
            }

            makeHttpPost(mainActivity?.ipAddress, binding.registerBookTitleEdittext.text.toString(), binding.registerBookAuthorEdittext.text.toString()) { responseData, error ->
                if(error != null) {
                    Log.e(TAG, "Error: $error")

                    handler.post{
                        Toast.makeText(activity, "送信エラー", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Log.d(TAG, "Response: $responseData")

                    handler.post {
                        Toast.makeText(activity, "送信成功", Toast.LENGTH_SHORT).show()
                    }

                    clearEditText()
                }
            }
        }
    }

    private fun clearEditText() {

        handler.post {
            binding.registerBookTitleEdittext.setText("")
            binding.registerBookAuthorEdittext.setText("")
        }

    }

    private fun isEditTextEmpty(): Boolean {
        if(TextUtils.isEmpty(binding.registerBookTitleEdittext.text) || TextUtils.isEmpty(binding.registerBookAuthorEdittext.text)) {
            Toast.makeText(requireContext(), "入力されていない箇所があります", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}