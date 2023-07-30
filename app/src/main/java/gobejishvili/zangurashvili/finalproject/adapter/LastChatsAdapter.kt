package gobejishvili.zangurashvili.finalproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import gobejishvili.zangurashvili.finalproject.R
import gobejishvili.zangurashvili.finalproject.entity.LastChat
import java.text.SimpleDateFormat
import java.util.*

class LastChatsAdapter(var lastChats : List<LastChat>) : RecyclerView.Adapter<LastChatsAdapter.LastChatItemViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    inner class LastChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }

        val nameText: TextView = itemView.findViewById(R.id.lastChatsName)
        val timeText: TextView = itemView.findViewById(R.id.lastMessagetimeText)
        var messageText: TextView = itemView.findViewById(R.id.lastMessage)
        val lastMessageImage: ShapeableImageView = itemView.findViewById(R.id.chatProfilePic)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastChatItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_last_chats, parent, false)
        return LastChatItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lastChats.size
    }

    override fun onBindViewHolder(holder: LastChatItemViewHolder, position: Int) {
        var lastChat = lastChats[position]
        holder.messageText.text = lastChat.text
        holder.nameText.text = lastChat.userName
        holder.timeText.text = formatTime(lastChat.sendTime)
        holder.lastMessageImage.setImageBitmap(lastChat.ImageBitmap)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatTime(inputTime: Date): String {
        return try {
            val date = inputTime

            val calendar = Calendar.getInstance()
            calendar.time = date

            val now = Calendar.getInstance()
            val differenceMillis = now.timeInMillis - calendar.timeInMillis
            val seconds = differenceMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            if (days > 0) {
                val dayMonthFormat = SimpleDateFormat("dd MMM")
                dayMonthFormat.format(date)
            } else if (hours > 0) {
                "$hours hour"
            } else {
                "$minutes min"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}








