package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import gobejishvili.zangurashvili.finalproject.adapter.UserAdapter
import gobejishvili.zangurashvili.finalproject.databinding.ActivityUserListBinding
import gobejishvili.zangurashvili.finalproject.entity.User

class UserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserListBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter

    private var searchTimeoutHandler: Handler = Handler()
    private val debounceTimeout: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        init()
    }

    private fun init(){
        binding.imageButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        userList = ArrayList()
        adapter = UserAdapter(userList, mAuth)

        userRecyclerView = binding.usersRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))
        userRecyclerView.adapter = adapter

        binding.searchBar.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){
            }

            override fun afterTextChanged(s: Editable?) {
                val searchBarText: String = s.toString()
                if (searchBarText.length >= 3){
                    searchTimeoutHandler.postDelayed({
                        updateUsers(searchBarText)
                    }, debounceTimeout)
                }
            }

        })
    }

    private fun updateUsers(searchBarText: String){
        mDatabaseReference = FirebaseDatabase
            .getInstance("https://android-final-project-877bc-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users")

        mDatabaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()){
                    for (currentSnapshot in snapshot.children){
                        val currentUser = currentSnapshot.getValue(User::class.java)
                        if (currentUser?.username!!.contains(searchBarText) && currentUser?.userId!! != mAuth.currentUser?.uid!!){
                            userList.add(currentUser)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                if (userList.size == 0){
                    Toast.makeText(this@UserListActivity, "Such username doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}