package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import gobejishvili.zangurashvili.finalproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val emailSuffix = "@freeuni.edu.ge"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance();
        //initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        setOnClickListeners()
    }

    fun setOnClickListeners(){
        binding.signup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.signInButton.setOnClickListener(loginButtonListener)
    }

    val loginButtonListener = View.OnClickListener {
        val nickname = binding.nickname.text.toString()
        val password = binding.password.text.toString()

        if (nickname.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please, fill all the fields!", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        val nicknameInEmailFormat = nickname + emailSuffix
        mAuth.signInWithEmailAndPassword(nicknameInEmailFormat, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // sign in success
                    // aq gadaxteba ProfileActivity-ze
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    // sign in failed
                    Toast.makeText(this@LoginActivity, "User doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}