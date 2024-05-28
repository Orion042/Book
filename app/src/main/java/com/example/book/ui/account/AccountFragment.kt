package com.example.book.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
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

    private var isIconChanged = false
    private var isNameChanged = false

    private var savedImageUri: Uri? = null

    private val handler = Handler(Looper.getMainLooper())

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

        loadImage(sharedPreferences, binding.userIconImageview)

        loadUserName(sharedPreferences, binding.userNameTextview)

        bottomSheet = BottomSheetBehavior.from(binding.bottomSheetLayout)

        binding.userEditButton.setOnClickListener {
            when(it.id) {
                R.id.user_edit_button -> {
                    if(bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED) {
                        loadImage(sharedPreferences, binding.userEditIconImageview)
                        loadUserName(sharedPreferences, binding.editUserNameEdittext)

                        updateSavedButton(false)

                        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    else {

                        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
        }

        binding.userEditIconImageview.setOnClickListener {
            changeIconImage()
        }

        binding.accountCancelTextview.setOnClickListener {

            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.accountSaveTextview.setOnClickListener {
            if(isIconChanged) {
                savedImageUri?.let {
                    saveImageUri(it)
                }
            }
            else if(isNameChanged) {
                saveUserNameUri(binding.editUserNameEdittext.text.toString())
            }

            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.editUserNameEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isNameChanged = true
                updateSavedButton(true)
            }

            override fun afterTextChanged(s: Editable?) {
                isNameChanged
                updateSavedButton(true)
            }
        })
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
                binding.userEditIconImageview.setImageURI(it.data?.data)

                updateSavedButton(true)

                savedImageUri = it.data?.data!!

            }else{
                Toast.makeText(activity, "エラーが発生しました", Toast.LENGTH_LONG).show()

            }
        }

    private fun loadImage(sharedPreferences: SharedPreferences, imageView: ImageView) {
        val savedImageUriString = sharedPreferences.getString("userIconImageUri", null)

        if(savedImageUriString == null) {
            imageView.setImageResource(R.drawable.sample_user_image)
        }
        else {
            savedImageUriString?.let {
                val savedImageUri = Uri.parse(it)
                loadImageUri(savedImageUri, imageView)
            }
        }
    }

    private fun loadUserName(sharedPreferences: SharedPreferences, textView: TextView) {
        val savedImageUriString = sharedPreferences.getString("userNameUri", null)

        if(savedImageUriString == null) {
            handler.post {
                textView.text = "ユーザー"
            }

        }
        else {
            savedImageUriString?.let {
                handler.post {
                    textView.text = savedImageUriString
                }
            }
        }
    }

    private fun loadUserName(sharedPreferences: SharedPreferences, editText: EditText) {
        val savedImageUriString = sharedPreferences.getString("userNameUri", null)

        if(savedImageUriString == null) {
            handler.post {
                editText.setText("ユーザー")
            }
        }
        else {
            savedImageUriString?.let {
                handler.post {
                    editText.setText(savedImageUriString)
                }
            }
        }
    }

    private fun saveImageUri(imageUri: Uri) {

        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        context?.contentResolver?.takePersistableUriPermission(imageUri, takeFlags)

        val sharedPreferences = requireActivity().getSharedPreferences("com.example.book", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.putString("userIconImageUri", imageUri.toString())

        editor.apply()

        binding.userIconImageview.setImageURI(imageUri)
    }

    private fun saveUserNameUri(userName: String) {

        val sharedPreferences = requireActivity().getSharedPreferences("com.example.book", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.putString("userNameUri", userName)

        editor.apply()

        binding.userNameTextview.text = userName
    }

    private fun loadImageUri(imageUri: Uri, iconImageView: ImageView) {
        iconImageView.setImageURI(imageUri)
    }

    private fun updateSavedButton(isChanged: Boolean) {

        if(isChanged) {
            binding.accountSaveTextview.visibility = View.VISIBLE
            binding.accountSaveTextview.isClickable = true
        }
        else {
            binding.accountSaveTextview.visibility = View.INVISIBLE
            binding.accountSaveTextview.isClickable = false
        }
    }
}