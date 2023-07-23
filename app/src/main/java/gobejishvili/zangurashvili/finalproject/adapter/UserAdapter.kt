package gobejishvili.zangurashvili.finalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gobejishvili.zangurashvili.finalproject.databinding.UserItemBinding
import gobejishvili.zangurashvili.finalproject.entity.User

class UserAdapter(val users: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

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
            .into(holder.profilePicture)
    }
}