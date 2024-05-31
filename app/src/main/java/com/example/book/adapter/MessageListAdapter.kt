package com.example.book.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book.Message
import com.example.book.R
import com.example.book.User


class MessageListAdapter(
    private val mContext: Context,
    private val mMessageList: List<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message: Message = mMessageList[position]

        return if (message.getSender().getUserId() == "User") {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_message_my_layout, parent, false)
            SentMessageHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_message_gemini_layout, parent, false)
            ReceivedMessageHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Message = mMessageList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    private inner class SentMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.text_chat_message_my)
        private val timeText: TextView = itemView.findViewById(R.id.text_chat_timestamp_my)

        fun bind(message: Message) {
            messageText.text = message.getMessage()
            timeText.text = message.getCreatedAt()
        }
    }

    private inner class ReceivedMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.text_chat_message_gemini)
        private val timeText: TextView = itemView.findViewById(R.id.text_chat_timestamp_gemini)
        private val nameText: TextView = itemView.findViewById(R.id.text_chat_user_gemini)
        private val profileImage: ImageView = itemView.findViewById(R.id.image_chat_profile_gemini)

        fun bind(message: Message) {
            messageText.text = message.getMessage()
            timeText.text = message.getCreatedAt()
            nameText.text = message.getSender().getNickname()
            profileImage.setImageResource(R.drawable.gemini_img)
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }
}