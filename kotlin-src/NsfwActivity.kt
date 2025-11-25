package com.himanshu.osfeaturendkdemo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.himanshu.osfeaturendkdemo.databinding.ActivityNsfwBinding
import com.os.nsfwProcessor.NsfwDetector
import com.os.nsfwProcessor.NsfwFactors
import kotlinx.coroutines.launch

class NsfwActivity : AppCompatActivity() {
    lateinit var binding: ActivityNsfwBinding
    lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    lateinit var drawingScore: TextView
    lateinit var hentaiScore: TextView
    lateinit var pornScore: TextView
    lateinit var neutralScore: TextView
    lateinit var sexyScore: TextView
    lateinit var processTime: TextView
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nsfw)

        // adding the launcher
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            pickActivityResultCallback
        )
        drawingScore = binding.scoreDrawing
        hentaiScore = binding.scoreHentai
        neutralScore = binding.scoreNeutral
        pornScore = binding.pornScore
        sexyScore = binding.sexyScore
        processTime = binding.processTime
        image = binding.imageView

        binding.btnLoadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            pickImageLauncher.launch(intent)
        }
    }

    private var pickActivityResultCallback: ActivityResultCallback<ActivityResult> =
        ActivityResultCallback { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null && result.data?.clipData != null) {
                    val uri = result.data?.clipData!!.getItemAt(0).uri
                    contentResolver.openInputStream(uri).also {
                        val bitMap = BitmapFactory.decodeStream(it)
                        lifecycleScope.launch {
                            val startTime = System.nanoTime()
                            val nsfwFactor =
                                NsfwDetector.detectNsfwSinglePass(applicationContext, bitMap)
                            drawingScore.text = "%.2f".format(nsfwFactor.drawing)
                            hentaiScore.text = "%.2f".format(nsfwFactor.hentai)
                            neutralScore.text = "%.2f".format(nsfwFactor.neutral)
                            pornScore.text = "%.2f".format(nsfwFactor.porn)
                            sexyScore.text = "%.2f".format(nsfwFactor.sexy)
                            val endTime = System.nanoTime()
                            val timeTaken = (endTime - startTime) / 1_000_000.0f
                            processTime.text = "%.2f".format(timeTaken) + " ms"
                        }
                        image.setImageBitmap(bitMap)
                    }

                }
            }
        }
}