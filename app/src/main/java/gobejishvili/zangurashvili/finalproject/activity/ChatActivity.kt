package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gobejishvili.zangurashvili.finalproject.R
import gobejishvili.zangurashvili.finalproject.adapter.ChatAdapter
import gobejishvili.zangurashvili.finalproject.entity.Chat
import gobejishvili.zangurashvili.finalproject.entity.LastChat
import gobejishvili.zangurashvili.finalproject.entity.User
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    var senderId : String = "";
    var getterId : String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val chatView = findViewById<RecyclerView>(R.id.chat_recycler_view)
        val layoutManager = LinearLayoutManager(this)
        chatView.layoutManager = layoutManager

        val database = FirebaseDatabase.getInstance();

        var extras = intent.extras

        senderId = extras?.getString ("senderId").toString();
        getterId = extras?.getString ("getterId").toString();


        val getterMessages = database.getReference("chats").child(getterId).child(senderId)
        val senderMessages = database.getReference("chats").child(senderId).child(getterId)
        val getterLastMessages = database.getReference("lastMessages").child(getterId).child(senderId)
        val senderLastMessages = database.getReference("lastMessages").child(senderId).child(getterId)
        val usersRef = database.getReference("Users").child(senderId)
        var getterUser : User
        val chatList = mutableListOf<Chat>()
        val chatAdapter = ChatAdapter(chatList, getterId)

        chatView.adapter = chatAdapter

        senderMessages.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<HashMap<String, Any>>()
                if (value != null) {
                    val newChatList = mutableListOf<Chat>()

                    for (item in value){
                        val chat = item.value as HashMap<String, Any>

                        val chatObj = chat.toDataClass<Chat>()
                        newChatList.add(chatObj)

                    }
                    newChatList.sortBy {it.sendTime }
                    chatAdapter.chatMessages = newChatList
                    chatAdapter.notifyDataSetChanged()
                    layoutManager.smoothScrollToPosition(chatView, RecyclerView.State(), newChatList.size)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity,"Firebase" +  "Failed to read value." + error.toException(), Toast.LENGTH_SHORT).show()

            }

        })


        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<HashMap<String, Any>>()
                if (value != null) {
                    getterUser = value.toDataClass<User>()
                    var nameText = findViewById<TextView>(R.id.nameTextView)
                    var occupationText = findViewById<TextView>(R.id.occupationTextView)
                    var imagePic = findViewById<ShapeableImageView>(R.id.chatProfilePic)
                    nameText.text = getterUser.username
                    occupationText.text = getterUser.profession

                    Glide.with(this@ChatActivity)
                        .asBitmap()
                        .load(getterUser.profilePictureUrl)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                imagePic.setImageBitmap(resource)
                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error reading from Firebase: ${error.message}")
            }
        })



        val sendButt = findViewById<Button>(R.id.sendButton)


        sendButt.setOnClickListener {
            sendMessage(senderMessages, getterMessages, senderLastMessages, getterLastMessages)
        }

        val backButt = findViewById<ImageView>(R.id.backbutt)


        backButt.setOnClickListener {
            val intent = Intent(this@ChatActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun saveLastMessages(senderLastMessages: DatabaseReference,
                                 getterLastMessages: DatabaseReference,
                                 message: String) {
        var lastMessage = LastChat(senderId, "", message,
            sendTime = Calendar.getInstance().time, null)

        getterLastMessages.updateChildren(lastMessage.serializeToMap())

        lastMessage.userId = getterId
        senderLastMessages.updateChildren(lastMessage.serializeToMap())

    }

    private fun sendMessage(
        senderMessages: DatabaseReference,
        getterMessages: DatabaseReference,
        senderLastMessages: DatabaseReference,
        getterLastMessages: DatabaseReference
    ) {
        val messageTxt = findViewById<EditText>(R.id.messageTextBox).text.toString()
        if(messageTxt != ""){
            val newChatSender = Chat(senderId = senderId,
                getterId = getterId,
                text = messageTxt,
                sendTime = Calendar.getInstance().time)

            val senderRef = senderMessages.push()
            senderRef.setValue(newChatSender.serializeToMap())

            val newChatGetter = newChatSender

            val getterRef = getterMessages.push()

            getterRef.setValue(newChatGetter.serializeToMap())

            saveLastMessages(senderLastMessages, getterLastMessages, messageTxt)
        }
        findViewById<EditText>(R.id.messageTextBox).setText("")
    }
}

fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

inline fun <reified T> Map<String, Any>.toDataClass(): T {
    return convert()
}

inline fun <I, reified O> I.convert(): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}
