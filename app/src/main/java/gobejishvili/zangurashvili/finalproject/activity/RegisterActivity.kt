package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import gobejishvili.zangurashvili.finalproject.databinding.ActivityRegisterBinding
import gobejishvili.zangurashvili.finalproject.entity.User

class RegisterActivity : AppCompatActivity() {
    private val emailSuffix = "@freeuni.edu.ge"
    private val profilePicturePlaceholderUrl = "https://www.vhv.rs/dpng/d/556-5566192_mystery-man-avatar-new-taipei-city-hd-png.png"
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener(signUpButtonListener)
    }

    val signUpButtonListener = View.OnClickListener {
        val nickname = binding.nickname.text.toString()
        val password = binding.password.text.toString()
        val profession = binding.profession.text.toString()

        if (nickname.isEmpty() || password.isEmpty() || profession.isEmpty()){
            Toast.makeText(this@RegisterActivity, "Please, fill all the fields!", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        val nicknameInEmailFormat = nickname + emailSuffix
        mAuth.createUserWithEmailAndPassword(nicknameInEmailFormat, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateDatabase(User(mAuth.currentUser?.uid!!, nickname, profilePicturePlaceholderUrl, profession))
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                } else {
                    //If sign in fails, display a message to the user.
                    Toast.makeText(
                        this@RegisterActivity,
                        "This nickname already exists or your password length is less than 6",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun updateDatabase(user: User){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        mDatabaseReference.child(user.userId).setValue(user)
    }
}