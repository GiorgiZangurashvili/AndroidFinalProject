package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import gobejishvili.zangurashvili.finalproject.R
import gobejishvili.zangurashvili.finalproject.adapter.LastChatsAdapter
import gobejishvili.zangurashvili.finalproject.entity.LastChat
import gobejishvili.zangurashvili.finalproject.entity.User
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

        var lastMessagesList = mutableListOf<LastChat>()


        val lastChatAdapter = LastChatsAdapter(lastMessagesList)


        val database = FirebaseDatabase.getInstance();
        val lastMessages = database.getReference("lastMessages")
            .child(currentFirebaseUser!!.uid)


        lastMessages.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<HashMap<String, Any>>()
                if (value != null) {
                    val lastChats = mutableListOf<LastChat>()
                    for (item in value){
                        val lastChatStr = item.value as HashMap<String, Any>

                        val lastChat = lastChatStr.toDataClass<LastChat>()

                        lastChats.add(lastChat)

                    }

                    lastMessagesList = lastChats

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val usersRef = FirebaseDatabase.getInstance().getReference("Users")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<HashMap<String, Any>>()
                if (value != null) {
                    for (item in value) {
                        if (lastMessagesList.any({ it.userId.equals(item.key) })) {
                            val userStr = item.value as HashMap<String, Any>

                            val user = userStr.toDataClass<User>()

                            var lastMessage =
                                lastMessagesList.first({ it.userId.equals(user.userId) })
                            lastMessage.userName = user.username

                            Glide.with(this@MainActivity)
                                .asBitmap()
                                .load(user.profilePictureUrl)
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        lastMessage.ImageBitmap = resource
                                        lastChatAdapter.notifyDataSetChanged()
                                    }
                                })

                        }
                    }
                }

                lastMessagesList.sortBy { it.sendTime }
                lastChatAdapter.lastChats = lastMessagesList
                lastChatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        lastChatAdapter.setOnItemClickListener(object : LastChatsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedItem: LastChat = lastMessagesList[position]

                val intent = Intent(this@MainActivity, ChatActivity::class.java)
                intent.putExtra("senderId" , currentFirebaseUser!!.uid)
                intent.putExtra("getterId" , clickedItem.userId)
                startActivity(intent)
            }
        })
        chatView.adapter = lastChatAdapter


        var searchView = findViewById<SearchView>(R.id.lastMessagesSearch)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterItems(newText, lastMessagesList, lastChatAdapter)
                return true
            }
        })
    }

    private fun filterItems(
        query: String,
        lastMessagesList: MutableList<LastChat>,
        lastChatAdapter: LastChatsAdapter
    ) {
        val filteredItems = lastMessagesList.filter { item ->
            item.userName.contains(query, true) }


        lastChatAdapter.lastChats = filteredItems

        lastChatAdapter.notifyDataSetChanged()
    }
}