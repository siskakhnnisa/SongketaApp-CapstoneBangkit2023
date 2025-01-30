package com.songketa.songket_recognition_app.ui.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.databinding.FragmentCameraBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.utils.getImageUri
import com.songketa.songket_recognition_app.utils.reduceFileImage
import com.songketa.songket_recognition_app.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.data.model.Songket
import com.songketa.songket_recognition_app.ui.detailsongket.DetailSongketActivity
import com.songketa.songket_recognition_app.utils.Constant.CAMERA_PERMISSION_REQUEST_CODE
import android.Manifest


class CameraFragment : Fragment(), View.OnClickListener{

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CameraViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnCheckOut.setOnClickListener(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val granted = getString(R.string.access_granted)
                    showToast(granted)
                } else {
                    val denied = getString(R.string.access_denied)
                    showToast(denied)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.btnCamera.id -> {
                startCamera()
            }
            binding.btnGallery.id -> {
                startGallery()
            }
            binding.btnCheckOut.id -> {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            showLoading(true)

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            viewModel.postImage(multipartBody).observe(viewLifecycleOwner) {upload ->
                if(upload != null){
                    when(upload){
                        is Result.Loading ->{
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            val songket = Songket(
                                id = upload.data.datasetInfo.idfabric,
                                img = upload.data.datasetInfo.imgUrl,
                                name = upload.data.datasetInfo.fabricname,
                                origin = upload.data.datasetInfo.origin,
                                motif = upload.data.datasetInfo.pattern ,
                                description = upload.data.datasetInfo.description
                            )
                            AlertCustom(songket.img, songket.name, songket.id)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showToast(upload.error)
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.error_upload))
    }

    private fun AlertCustom(image: String, name: String, id: String){
        val alertDialogBuilder = AlertDialog.Builder(requireContext()).create()
        val view = layoutInflater.inflate(R.layout.dialog_songket, null)
        alertDialogBuilder.setView(view)
        val tvsongket = view.findViewById<TextView>(R.id.tv_fabricname)
        val ivsongket = view.findViewById<ImageView>(R.id.iv_item_dialog_songket_picture)
        val btnCheck = view.findViewById<Button>(R.id.btn_check)
        alertDialogBuilder.setCanceledOnTouchOutside(false)

        tvsongket.text = name
        Glide.with(requireContext())
            .load(image).into(ivsongket)
        btnCheck.setOnClickListener {
            val intent = Intent(requireContext(), DetailSongketActivity::class.java)
            intent.putExtra(DetailSongketActivity.ID, id)
            startActivity(intent)

            alertDialogBuilder.dismiss()
        }
        alertDialogBuilder.setCanceledOnTouchOutside(true)
        alertDialogBuilder.show()
    }


    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivInputImage.setImageURI(it)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d(getString(R.string.photo_picker), getString(R.string.no_media_selected))
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
