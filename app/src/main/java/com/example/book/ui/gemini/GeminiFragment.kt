package com.example.book.ui.gemini

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.book.BuildConfig
import com.example.book.Message
import com.example.book.User
import com.example.book.adapter.MessageListAdapter
import com.example.book.databinding.FragmentGeminiBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class GeminiFragment : Fragment() {

    private val TAG = "GeminiFragment"

    private var _binding: FragmentGeminiBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMessageRecycler: RecyclerView
    private lateinit var mMessageAdapter: MessageListAdapter
    private var messageList = listOf<Message>()

    private lateinit var startDate: TextView

    private var messageId = 0
    private var userEditText = ""

    private val gemini = User("Gemini", "Gemini")
    private val user = User("User", "User")

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

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
                hideKeyboard()

                displayMessageList()

                userEditText = binding.geminiChatMessageEdittext.text.toString()

                binding.geminiChatMessageEdittext.setText("")

                CoroutineScope(Dispatchers.IO).launch {
                    postMessage2Gemini(binding.geminiChatMessageEdittext.text.toString())

                    withContext(Dispatchers.Main) {
                        displayMessageList()
                    }
                }
            }
            else {
                Toast.makeText(activity, "質問等を入力してください", Toast.LENGTH_SHORT).show()
            }

        }

        binding.recyclerChat.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    suspend fun postMessage2Gemini(message: String) {
        try {

            var chatHistoryMessageList = listOf<Content>()

            val roleList = listOf("model", "user")

            for(index in messageList.indices) {
                chatHistoryMessageList += content(role = roleList[index % 2]) {text(messageList[index].getMessage())}
            }

            messageList += Message(messageId.toString(), userEditText, user, getTime())

            displayMessageList()

            val chat = generativeModel.startChat(
                history = chatHistoryMessageList
            )
            val resultResponse = chat.sendMessage(userEditText)

            messageList += Message(messageId.toString(), resultResponse.text!!, gemini, getTime())
        }
        catch (e: Exception) {
            Log.d(TAG, "Error: $e")
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

    private fun hideKeyboard(){
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.geminiChatMessageEdittext.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}