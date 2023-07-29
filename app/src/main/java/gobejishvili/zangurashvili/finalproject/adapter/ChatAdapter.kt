package gobejishvili.zangurashvili.finalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gobejishvili.zangurashvili.finalproject.R
import gobejishvili.zangurashvili.finalproject.entity.Chat
import java.text.SimpleDateFormat

class ChatAdapter(var chatMessages : List<Chat>, var getterId : String) : RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {
    class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderMessage: TextView = itemView.findViewById(R.id.senderTextView)
        val getterMessage: TextView = itemView.findViewById(R.id.getterTextView)
        val senderBox: ConstraintLayout = itemView.findViewById(R.id.senderMessage)
        val getterBox: ConstraintLayout = itemView.findViewById(R.id.getterMessage)
        val senderTimestamp: TextView = itemView.findViewById(R.id.timestampSender)
        val getterTimestamp: TextView = itemView.findViewById(R.id.timestampGetter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        if(chatMessages[position].getterId.equals(getterId)) {
            holder.getterBox.isVisible = false
            holder.getterTimestamp.isVisible = false
            holder.senderMessage.text = chatMessages[position].text
            val sdf = SimpleDateFormat("HH:mm")
            val formattedTime: String = sdf.format(chatMessages[position].sendTime)
            holder.senderTimestamp.text = formattedTime
        } else {
            holder.senderBox.isVisible = false
            holder.senderTimestamp.isVisible = false
            holder.getterMessage.text = chatMessages[position].text
            val sdf = SimpleDateFormat("HH:mm")
            val formattedTime: String = sdf.format(chatMessages[position].sendTime)
            holder.getterTimestamp.text = formattedTime
        }
    }
}
