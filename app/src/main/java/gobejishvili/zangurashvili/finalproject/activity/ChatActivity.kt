package gobejishvili.zangurashvili.finalproject.activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gobejishvili.zangurashvili.finalproject.R
import gobejishvili.zangurashvili.finalproject.adapter.ChatAdapter
import gobejishvili.zangurashvili.finalproject.entity.Chat
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    var senderId : Int = 0;
    var getterId : Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        var chatView = findViewById<RecyclerView>(R.id.chat_recycler_view)
        var layoutManager = LinearLayoutManager(this)
        chatView.layoutManager = layoutManager

        var database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true)
        senderId = 1
        getterId = 2
        var senderMessages = database.getReference("chats").child(senderId.toString()).child(getterId.toString())
        var getterMessages = database.getReference("chats").child(getterId.toString()).child(senderId.toString())
        val chatList = mutableListOf<Chat>()
        val chatAdapter = ChatAdapter(chatList, getterId)

        chatView.adapter = chatAdapter

        senderMessages.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<HashMap<String, Any>>()
                if (value != null) {
                    val newChatList = mutableListOf<Chat>()

                    for (item in value){
                        var chat = item.value as HashMap<String, Any>
                        Log.d(TAG, "Value is: $chat")

                        var chatObj = chat.toDataClass<Chat>()
                        newChatList.add(chatObj)

                    }
                    newChatList.sortBy {it.sendTime }
                    chatAdapter.chatMessages = newChatList
                    chatAdapter.notifyDataSetChanged()
                    layoutManager.smoothScrollToPosition(chatView, RecyclerView.State(), newChatList.size)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                System.out.println("Firebase" +  "Failed to read value." + error.toException())
            }

        })



        var sendButt = findViewById<Button>(R.id.sendButton)


        sendButt.setOnClickListener {
            sendMessage(senderMessages, getterMessages)
        }


    }

    private fun sendMessage(senderMessages: DatabaseReference, getterMessages: DatabaseReference) {
        var messageTxt = findViewById<EditText>(R.id.messageTextBox).text.toString()
        if(messageTxt != ""){
            var newChatSender = Chat(senderId = senderId,
                getterId = getterId,
                text = messageTxt,
                sendTime = Calendar.getInstance().time)

            var senderRef = senderMessages.push()
            senderRef.setValue(newChatSender.serializeToMap())

            var newChatGetter = newChatSender

            var getterRef = getterMessages.push()

            getterRef.setValue(newChatGetter.serializeToMap())

        }
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
