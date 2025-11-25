package com.himanshu.osfeaturendkdemo

import android.content.Context
import java.io.File

class LoadUtils {
    companion object {
        fun loadModel(context: Context, modelName: String): String {
            val file = File(context.filesDir, modelName);
            if (!file.exists()) {
                context.assets.open(modelName).also { input ->
                    file.outputStream().also { output ->
                        input.copyTo(output);
                    }
                }
            }
            return file.absolutePath
        }
    }

}