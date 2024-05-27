package com.example.book.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.book.R
import com.example.book.databinding.FragmentAccountBinding
import com.example.book.databinding.FragmentRegisterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("com.example.book", Context.MODE_PRIVATE)

        val savedImageUriString = sharedPreferences.getString("userIconImageUri", null)

        bottomSheet = BottomSheetBehavior.from(binding.bottomSheetLayout)

        if(savedImageUriString == null) {
            binding.userIconImageview.setImageResource(R.drawable.sample_user_image)
        }
        else {
            savedImageUriString?.let {
                val savedImageUri = Uri.parse(it)
                loadImageUri(savedImageUri)
            }
        }

        binding.userEditButton.setOnClickListener {
            when(it.id) {
                R.id.user_edit_button -> {
                    if(bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    else {
                        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
        }

        binding.userIconImageview.setOnClickListener {
            changeIconImage()
        }
    }

    private fun changeIconImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }

        setUserIcon.launch(intent)
    }

    private val setUserIcon =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                binding.userIconImageview.setImageURI(it.data?.data)

                saveImageUri(it.data?.data!!)
            }else{
                Toast.makeText(activity, "エラーが発生しました", Toast.LENGTH_LONG).show()
            }
        }

    private fun saveImageUri(imageUri: Uri) {

        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        context?.contentResolver?.takePersistableUriPermission(imageUri, takeFlags)

        val sharedPreferences = requireActivity().getSharedPreferences("com.example.book", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.putString("userIconImageUri", imageUri.toString())

        editor.apply()
    }

    private fun loadImageUri(imageUri: Uri) {
        binding.userIconImageview.setImageURI(imageUri)
    }
}