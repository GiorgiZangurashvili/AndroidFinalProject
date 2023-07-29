package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import gobejishvili.zangurashvili.finalproject.R
import gobejishvili.zangurashvili.finalproject.adapter.LastChatsAdapter
import gobejishvili.zangurashvili.finalproject.entity.LastChat
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        var chatView = findViewById<RecyclerView>(R.id.chat_panel_recycler_view)
        var layoutManager = LinearLayoutManager(this)
        chatView.layoutManager = layoutManager

        val lastMessagesList = mutableListOf<LastChat>()

        val calendar = Calendar.getInstance()
        var time = calendar.time

        var lastChat = LastChat(
            userId =  "1" , userName = "Nikoloz Gobejishviliiiiiiiiiiii",
            "MAGARIAMAGARIAMAGARIAMAGARIAMAGARIAMAGARIAMAGARIAMAGARIAMAGARIAMAGARIAMAGARIAMAGARIA",
            time, null)

        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        lastMessagesList.add(lastChat)
        val lastChatAdapter = LastChatsAdapter(lastMessagesList)

        lastChatAdapter.setOnItemClickListener(object : LastChatsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedItem: LastChat = lastMessagesList[position]

                val intent = Intent(this@MainActivity, ChatActivity::class.java)
/*                intent.putExtra("senderId" , currentFirebaseUser!!.uid)
                intent.putExtra("getterId" , clickedItem.userId)*/
                intent.putExtra("senderId" , "P1")
                intent.putExtra("getterId" , "P2")
                startActivity(intent)
            }
        })
        chatView.adapter = lastChatAdapter

    }
}