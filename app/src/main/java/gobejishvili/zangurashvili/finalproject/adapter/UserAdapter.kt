package gobejishvili.zangurashvili.finalproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import gobejishvili.zangurashvili.finalproject.activity.ChatActivity
import gobejishvili.zangurashvili.finalproject.databinding.UserItemBinding
import gobejishvili.zangurashvili.finalproject.entity.User

class UserAdapter(val users: ArrayList<User>, val mAuth: FirebaseAuth): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(binding: UserItemBinding): RecyclerView.ViewHolder(binding.root){
        val username = binding.username
        val profession = binding.job
        val profilePicture = binding.profilePicture
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = users[position]
        holder.username.text = currentUser.username
        holder.profession.text = currentUser.profession
        Glide
            .with(holder.itemView.context)
            .load(users[position].profilePictureUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.profilePicture)

        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(it.context, ChatActivity::class.java)
            intent.putExtra("getterId", mAuth.currentUser?.uid)
            intent.putExtra("senderId", currentUser.userId)
            it.context.startActivity(intent)
        }
    }
}