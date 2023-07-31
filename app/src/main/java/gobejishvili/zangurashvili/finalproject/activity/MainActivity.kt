package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.ContactsContract.Profile
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarMenu
import com.google.android.material.navigation.NavigationView
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
import gobejishvili.zangurashvili.finalproject.fragment.ChatFragment
import gobejishvili.zangurashvili.finalproject.fragment.ChatListFragment
import gobejishvili.zangurashvili.finalproject.fragment.ProfileFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadFragment(ChatListFragment())
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    loadFragment(ChatListFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        var floatbutt = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        floatbutt.setOnClickListener() {
            startActivity(Intent(this, UserListActivity::class.java))
        }
    }

    fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}