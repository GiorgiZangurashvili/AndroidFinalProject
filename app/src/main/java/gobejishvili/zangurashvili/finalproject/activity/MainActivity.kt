package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import gobejishvili.zangurashvili.finalproject.fragment.ChatListFragment
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        if (savedInstanceState == null) {
            loadFragment(ChatListFragment())
        }

        val buttonA: ImageButton = findViewById(R.id.navigationButton1)
        val buttonB: ImageButton = findViewById(R.id.navigationbutton2)

        buttonA.setOnClickListener {
            loadFragment(ChatListFragment())
        }

/*        buttonB.setOnClickListener {
            loadFragment(FragmentB())
        }*/


    }

    fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}