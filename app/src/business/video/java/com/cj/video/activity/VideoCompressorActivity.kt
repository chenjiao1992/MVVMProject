package com.cj.video.activity

import android.annotation.TargetApi
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.library.utils.Util
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ActivityVideoCompressorBinding
import com.vincent.videocompressor.VideoCompress
import kotlinx.android.synthetic.main.activity_video_compressor.pb_compress
import kotlinx.android.synthetic.main.activity_video_compressor.tv_indicator
import kotlinx.android.synthetic.main.activity_video_compressor.tv_input
import kotlinx.android.synthetic.main.activity_video_compressor.tv_output
import java.io.File
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

/**
 *  Create by chenjiao at 2020/5/6 0006
 *  描述：
 */
class VideoCompressorActivity : BaseBindingActivity<ActivityVideoCompressorBinding>() {
    private var inputPath: String? = null
    var destPath = ""
    private var startTime: Long = 0
    private var endTime: Long = 0
    private val outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .absolutePath

    override fun getLayoutId() = R.layout.activity_video_compressor

    private fun getLocale(): Locale? {
        val config = resources.configuration
        var sysLocale: Locale? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config)
        } else {
            sysLocale = getSystemLocaleLegacy(config)
        }
        return sysLocale
    }

    fun getSystemLocaleLegacy(config: Configuration): Locale? {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getSystemLocale(config: Configuration): Locale? {
        return config.locales[0]
    }

    override fun onConfig(arguments: Intent?) {
        binding.btnSelect.setOnClickListener { addLoacalVideo() }
        binding.btnCompress.setOnClickListener {
            destPath = tv_output.getText().toString() + File.separator + "out_VID_" + SimpleDateFormat("yyyyMMdd_HHmmss", getLocale())
                .format(Date()) + ".mp4"
            VideoCompress.compressVideoMedium(tv_input.getText().toString(), destPath, object : VideoCompress.CompressListener {
                override fun onStart() {
                    tv_indicator.setText("""Compressing...
    Start at: ${SimpleDateFormat("HH:mm:ss", getLocale()).format(Date())}
    """.trimIndent())
                    pb_compress.setVisibility(View.VISIBLE)
                    startTime = System.currentTimeMillis()
                    Util.writeFile(this@VideoCompressorActivity, """
     Start at: ${SimpleDateFormat("HH:mm:ss", getLocale()).format(Date())}
     
     """.trimIndent())
                }

                override fun onSuccess() {
                    val previous: String = tv_indicator.getText().toString()
                    tv_indicator.setText("""
    $previous
    Compress Success!
    End at: ${SimpleDateFormat("HH:mm:ss", getLocale()).format(Date())}
    """.trimIndent())
                    pb_compress.setVisibility(View.INVISIBLE)
                    endTime = System.currentTimeMillis()
                    Util.writeFile(this@VideoCompressorActivity, """
     End at: ${SimpleDateFormat("HH:mm:ss", getLocale()).format(Date())}
     
     """.trimIndent())
                    Util.writeFile(this@VideoCompressorActivity, """
     Total: ${(endTime - startTime) / 1000}s
     
     """.trimIndent())
                    Util.writeFile(this@VideoCompressorActivity)
                    startActivity(Intent(this@VideoCompressorActivity, VideoActivity::class.java).putExtra("vvVideo", destPath))
                }

                override fun onFail() {
                    binding.tvIndicator.setText("Compress Failed!")
                    binding.pbCompress.setVisibility(View.INVISIBLE)
                    endTime = System.currentTimeMillis()
                    Util.writeFile(this@VideoCompressorActivity, "Failed Compress!!!" + SimpleDateFormat("HH:mm:ss", getLocale()).format(Date()))
                }

                override fun onProgress(percent: Float) {
                    binding.tvProgress.setText("$percent%")
                }
            })
        }
        tv_output.setText(outputDir)
    }

    private fun addLoacalVideo() {
        val intentvideo = Intent()
        if (Build.VERSION.SDK_INT < 19) {
            intentvideo.action = Intent.ACTION_GET_CONTENT
            intentvideo.type = "video/*"
        } else {
            intentvideo.action = Intent.ACTION_OPEN_DOCUMENT
            intentvideo.addCategory(Intent.CATEGORY_OPENABLE)
            intentvideo.type = "video/*"
        }
        startActivityForResult(Intent.createChooser(intentvideo, "选择要导入的视频"),
            REQUEST_FOR_VIDEO_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FOR_VIDEO_FILE && resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                //                inputPath = data.getData().getPath();
                //                tv_input.setText(inputPath);
                try {
                    inputPath = Util.getFilePath(this, data.data)
                    binding.tvInput.setText(inputPath)
                    Log.d("xxx", "inputPath=$inputPath")
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
                //                inputPath = "/storage/emulated/0/DCIM/Camera/VID_20170522_172417.mp4"; // 图片文件路径
                //                tv_input.setText(inputPath);// /storage/emulated/0/DCIM/Camera/VID_20170522_172417.mp4
            }
        }
    }

    companion object {
        private const val REQUEST_FOR_VIDEO_FILE = 1000
    }
}