package com.fycus.ui.fragment.profile

import android.Manifest
import android.app.AlertDialog
import android.content.CursorLoader
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentEditProfileBinding
import com.fycus.models.UserModel
import com.fycus.ui.activity.HomeActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.ConnectException

class EditProfileFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding : FragmentEditProfileBinding
    private var imageFile: MultipartBody.Part? = null
    private val pickImageCamera = 1
    private val pickImageGallery = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_profile, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        callGetProfileAPI()
    }

    private fun callGetProfileAPI() {
        apiService.callGetProfileAPI("Bearer "+userPref.user.token
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1) {
                        setData(commonResponse.mData)
                    } else {
                        Toast.makeText(requireActivity(),commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }, {
                    hideProgressDialog()
                    if (it is ConnectException) {
                        utils.simpleAlert(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.check_network_connection)
                        )
                    } else {
                        utils.simpleAlert(requireActivity(), "", it.message.toString())
                    }
                }
            )
    }

    private fun setData(mData: UserModel?) {
        try {
            if (mData!!.image != null)
                Glide.with(requireActivity()).load(Uri.parse(mData.image))
                    //.error(R.drawable.user)
                    .into(binding.ivUserImage)
            binding.edtName.setText(mData.name)
            binding.edtEmail.setText(mData.email)
            binding.edtMobile.setText(mData.mobile)
        } catch (e: Exception) {

        }

    }

    private fun setListener() {
        binding.ivUserImage.setOnClickListener(this)
        binding.tvPickImage.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.ivUserImage -> {
                selectImage()
            }
            R.id.tvPickImage -> {
                selectImage()
            }
            R.id.btnUpdate -> {
                if(checkValidation()){
                    if(imageFile == null){
                        val requestFile =
                            RequestBody.create(MediaType.parse("image/jpg"), "")
                        imageFile = MultipartBody.Part.createFormData("image", "", requestFile)

                    }
                    callUpdateProfileAPI()
                }
            }
        }
    }

    /**
     * image
    name
    mobile
    email
     */
    private fun callUpdateProfileAPI() {
        val name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.edtName.text.toString())
        val email: RequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.edtEmail.text.toString())
        val mobile: RequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.edtMobile.text.toString())
        apiService.callUpdateProfileAPI("Bearer "+userPref.user.token,
            imageFile!!,name,mobile,email
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.status == 1) {
                       // setData(commonResponse.mData)
                    } else {
                        Toast.makeText(requireActivity(),commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }, {
                    hideProgressDialog()
                    if (it is ConnectException) {
                        utils.simpleAlert(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.check_network_connection)
                        )
                    } else {
                        utils.simpleAlert(requireActivity(), "", it.message.toString())
                    }
                }
            )
    }

    private fun checkValidation(): Boolean {
        when {
            binding.edtName.text.toString().isEmpty() -> {
                binding.edtName.error = "Please enter Name"
                binding.edtName.requestFocus()
                return false
            }
            binding.edtEmail.text.toString().isEmpty() -> {
                binding.edtEmail.error = "Please enter Email ID"
                binding.edtEmail.requestFocus()
                return false
            }
            binding.edtMobile.text.toString().isEmpty() -> {
                binding.edtMobile.error = "Please enter Mobile Number"
                binding.edtMobile.requestFocus()

                return false
            }
            else -> return true
        }

    }

    private fun requestStoragePermission(isCamera: Boolean) {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        if (isCamera) {
                            openCamera()
                            //dispatchTakePictureIntent()
                        } else {
                            openGallery()

                            // dispatchGalleryIntent()
                        }
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }

                /*   fun onPermissionRationaleShouldBeShown(
                       permissions: List<PermissionRequest?>?,
                       token: PermissionToken
                   ) {
                       token.continuePermissionRequest()
                   }*/
            })
            .withErrorListener { error ->
                Toast.makeText(requireActivity(), "Error occurred! ", Toast.LENGTH_SHORT)
                    .show()
            }
            .onSameThread()
            .check()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(intent, pickImageCamera)
    }

    private fun openGallery() {
        val pickPhoto =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        this.startActivityForResult(pickPhoto, pickImageGallery)
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Need Permissions")
        builder.setMessage(
            "This app needs permission to use this feature. You can grant them in app settings."
        )
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.community", null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val pm: PackageManager = context!!.packageManager
        val builder =
            AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle("Select Option")
        builder.setItems(
            options
        ) { dialog: DialogInterface, item: Int ->
            when {
                options[item] == "Take Photo" -> {
                    dialog.dismiss()
                    requestStoragePermission(true)
                }
                options[item] == "Choose From Gallery" -> {
                    dialog.dismiss()
                    requestStoragePermission(false)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageCamera && data != null) {
            val bitmap = data.extras!!["data"] as Bitmap?
            val bytes = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
            val uri = getImageUri(bitmap)
            val file = File(getPath(uri!!))
            val requestFile =
                RequestBody.create(MediaType.parse("image/jpg"), file)
            binding.ivUserImage.setImageURI(uri)
            imageFile = MultipartBody.Part.createFormData("image", file.name, requestFile)

        } else if (requestCode == pickImageGallery && data != null) {
            val selectedImage = data.data
            try {
                val file = File(getPath(selectedImage!!))
                val requestFile =
                    RequestBody.create(MediaType.parse("image/jpg"), file)
                binding.ivUserImage.setImageURI(selectedImage)
                imageFile = MultipartBody.Part.createFormData("image", file.name, requestFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(context, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        Log.d("image path", cursor.getString(columnIndex))
        return cursor.getString(columnIndex)
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context!!.contentResolver,
            inImage,
            "ProfilePic",
            null
        )
        return Uri.parse(path)
    }
}