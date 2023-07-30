package gobejishvili.zangurashvili.finalproject.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import gobejishvili.zangurashvili.finalproject.R
import gobejishvili.zangurashvili.finalproject.activity.LoginActivity
import gobejishvili.zangurashvili.finalproject.databinding.FragmentProfileBinding
import gobejishvili.zangurashvili.finalproject.entity.User
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mStorage: StorageReference
    private val emailSuffix = "@freeuni.edu.ge"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        mStorage = FirebaseStorage.getInstance().reference.child("profilePictures")
        mDatabaseReference = FirebaseDatabase
            .getInstance("https://android-final-project-877bc-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Users")
        prepareUI()

        setOnClickListeners()

        return binding.root
    }

    private fun prepareUI() {
        mDatabaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (currentSnapshot in snapshot.children){
                        val currentUser = currentSnapshot.getValue(User::class.java)
                        if (currentUser != null) {
                            if (currentUser.userId == mAuth.currentUser?.uid!!){
                                binding.name.setText(currentUser.username)
                                binding.profession.setText(currentUser.profession)
                                Glide
                                    .with(activity!!)
                                    .load(currentUser.profilePictureUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(binding.profilePic)
                                break
                            }

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setOnClickListeners(){
        binding.signout.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        binding.update.setOnClickListener {
            updateUserDataInDatabase()
        }

        binding.profilePic.setOnClickListener {
            openGalleryLauncher.launch("image/*")
        }
    }



    //    set image from gallery
//    binding.signup.setOnClickListener {
//        openGalleryLauncher.launch("image/*")
//    }
    private val openGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri ->
            Glide
                .with(requireActivity())
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profilePic)

            val profilePictureRef = mStorage.child(UUID.randomUUID().toString())
            val upload = profilePictureRef.putFile(imageUri)

            upload.addOnSuccessListener {
                profilePictureRef.downloadUrl.addOnSuccessListener {downloadUri ->
                    val url = downloadUri.toString()
                    updateUserImageInDatabase(url)
                }
            }.addOnFailureListener{
                TODO("Not implemented yet")
            }
        }
    }

    private fun updateUserImageInDatabase(imageUrl: String){
        val userReference = mDatabaseReference.child(mAuth.currentUser?.uid!!)

        userReference.child("profilePictureUrl").setValue(imageUrl)
            .addOnSuccessListener {
                Toast.makeText(activity, "Image successfully saved to database", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Couldn't save image to database", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserDataInDatabase(){
        val userReference = mDatabaseReference.child(mAuth.currentUser?.uid!!)

        userReference.child("username").setValue(binding.name.text.toString())
            .addOnSuccessListener {
                val user = mAuth.currentUser
                val newUsername = binding.name.text.toString() + emailSuffix

                user?.updateEmail(newUsername)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            activity,
                            "Username successfully saved to database",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            activity,
                            "Couldn't save username to database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Couldn't save username to database", Toast.LENGTH_SHORT).show()
            }
        userReference.child("profession").setValue(binding.profession.text.toString())
            .addOnSuccessListener {
                Toast.makeText(activity, "Profession successfully saved to database", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Couldn't save profession to database", Toast.LENGTH_SHORT).show()
            }
    }
}