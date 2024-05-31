package com.example.book.ui.gemini

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.book.Message
import com.example.book.R
import com.example.book.User
import com.example.book.adapter.MessageListAdapter
import com.example.book.databinding.FragmentAccountBinding
import com.example.book.databinding.FragmentGeminiBinding
import java.time.LocalDateTime

class GeminiFragment : Fragment() {

    private var _binding: FragmentGeminiBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMessageRecycler: RecyclerView
    private lateinit var mMessageAdapter: MessageListAdapter
    private var messageList = listOf<Message>()

    private var messageId = 0

    private val gemini = User("Gemini", "Gemini")
    private val user = User("User", "User")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeminiBinding.inflate(inflater, container, false)

        mMessageRecycler = binding.recyclerChat

        messageId = 0

        initMessage()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.geminiChatSendImageview.setOnClickListener {

            if(binding.geminiChatMessageEdittext.text.toString() != "") {
                messageList += Message(messageId.toString(), binding.geminiChatMessageEdittext.text.toString(), user, getTime())

                displayMessageList()
            }
            else {
                Toast.makeText(activity, "質問等を入力してください", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initMessage() {
        messageList += Message(messageId.toString(), "何かお困りごとはありますか？", gemini, getTime())

        displayMessageList()
    }

    private fun displayMessageList() {
        ++messageId

        mMessageAdapter = MessageListAdapter(requireContext(), messageList)

        mMessageRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mMessageAdapter
        }
    }

    private fun getTime(): String {
        val currentDateTime = LocalDateTime.now()

        val hour = currentDateTime.hour
        val minute = currentDateTime.minute

        return "$hour:$minute"
    }
}