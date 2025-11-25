package com.himanshu.osfeaturendkdemo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import com.google.android.material.switchmaterial.SwitchMaterial
import com.himanshu.osfeaturendkdemo.databinding.ActivityMainBinding
import com.os.imageprocessor.CameraFrameCapture
import com.os.imageprocessor.NativeImageProcessor
import com.os.nsfwProcessor.NsfwDetector

import com.osfeature.coreui.PrimaryButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageFilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageCaptureButton: PrimaryButton
    private lateinit var imageView: ImageView
    private lateinit var sourceBitMap: Bitmap
    private lateinit var btnGrayScale: PrimaryButton
    private lateinit var btnNegative: PrimaryButton
    private lateinit var btnBlur: PrimaryButton
    private lateinit var btnSharp: PrimaryButton
    private lateinit var btnEmboss: PrimaryButton
    private lateinit var btnEdgeDetect: PrimaryButton
    private lateinit var startCameraX: PrimaryButton
    private lateinit var optimizeNeon: SwitchMaterial


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initBinding()

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        defineActivityResultLaunchers()

        optimizeNeon = binding.switchNeon
        imageView = binding.imageView
        imageView.post {
            sourceBitMap = imageView.drawToBitmap(Bitmap.Config.ARGB_8888)
        }

        imageCaptureButton = binding.captureImageButton
        imageCaptureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            captureImageLauncher.launch(intent)
        }
        btnGrayScale = binding.btnGrayScale
        btnGrayScale.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = NativeImageProcessor.processImage(
                    applicationContext,
                    sourceBitMap,
                    NativeImageProcessor.Companion.PROCESS_TYPE.GRAY,
                    optimizeNeon.isChecked
                )
                imageView.setImageBitmap(bitmap)
            }
        }
        btnNegative = binding.btnNegative
        btnNegative.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = NativeImageProcessor.processImage(
                    applicationContext,
                    sourceBitMap,
                    NativeImageProcessor.Companion.PROCESS_TYPE.NEGATIVE,
                    optimizeNeon.isChecked
                )
                imageView.setImageBitmap(bitmap)
            }

        }
        btnBlur = binding.btnBlur
        btnBlur.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = NativeImageProcessor.processImage(
                    applicationContext,
                    sourceBitMap,
                    NativeImageProcessor.Companion.PROCESS_TYPE.Blur,
                    optimizeNeon.isChecked, 5, 5
                )
                imageView.setImageBitmap(bitmap)
            }
        }
        btnSharp = binding.btnSharp
        btnSharp.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = NativeImageProcessor.processImage(
                    applicationContext,
                    sourceBitMap,
                    NativeImageProcessor.Companion.PROCESS_TYPE.SHARPEN,
                    optimizeNeon.isChecked
                )
                imageView.setImageBitmap(bitmap)
            }
        }
        btnEmboss = binding.btnEmboss
        btnEmboss.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = NativeImageProcessor.processImage(
                    applicationContext,
                    sourceBitMap,
                    NativeImageProcessor.Companion.PROCESS_TYPE.EMBOSS,
                    optimizeNeon.isChecked
                )
                imageView.setImageBitmap(bitmap)
            }
        }
        btnEdgeDetect = binding.btnEdgeDetect
        btnEdgeDetect.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = NativeImageProcessor.processImage(
                    applicationContext,
                    sourceBitMap,
                    NativeImageProcessor.Companion.PROCESS_TYPE.SOBEL_EDGE,
                    optimizeNeon.isChecked
                )
                imageView.setImageBitmap(bitmap)
            }
        }
        startCameraX = binding.btnCameraX
        startCameraX.setOnClickListener {
            val intent = Intent(this, CameraFrameCapture::class.java)
            startActivity(intent)
        }
    }

    private fun defineActivityResultLaunchers() {
        captureImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            imageCaptureCallback
        )
    }

    private val imageCaptureCallback: ActivityResultCallback<ActivityResult> =
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null && result.data!!.clipData != null) {
                    val uri = result.data!!.clipData!!.getItemAt(0).uri
                    imageView.setImageURI(uri)
                    contentResolver.openInputStream(uri).also {
                        sourceBitMap = BitmapFactory.decodeStream(it)
                    }
                }
            }
        }
}