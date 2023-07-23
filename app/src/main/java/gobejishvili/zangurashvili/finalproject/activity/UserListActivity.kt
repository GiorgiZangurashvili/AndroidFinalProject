package gobejishvili.zangurashvili.finalproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gobejishvili.zangurashvili.finalproject.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}