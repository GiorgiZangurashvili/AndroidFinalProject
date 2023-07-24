package gobejishvili.zangurashvili.finalproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import gobejishvili.zangurashvili.finalproject.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, ChatActivity::class.java)
        FirebaseApp.initializeApp(this)
        startActivity(intent)
    }
}