package jp.teckacademy.yuka.komaki.autoslideshowapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import jp.teckacademy.yuka.komaki.autoslideshowapp.databinding.ActivityMainBinding
import java.sql.Time
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private val PERMISSIONS_REQUEST_CODE =100
    private val readImagesPermission =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)android.Manifest.permission.READ_MEDIA_IMAGES
        else android.Manifest.permission.READ_EXTERNAL_STORAGE

    private var timer:Timer? =null
    private var seconds=0.0
    private var handler = Handler(Looper.getMainLooper())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (checkSelfPermission(readImagesPermission) == PackageManager.PERMISSION_GRANTED) {
            getContentsInfo()
        } else {
            requestPermissions(
                arrayOf(readImagesPermission),
                PERMISSIONS_REQUEST_CODE
            )
        }
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )


        if(cursor!!.moveToFirst()){
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            binding.imageView.setImageURI(imageUri)
        }

        if (timer == null) {
            binding.forwardButton.setOnClickListener {
                if (cursor.moveToNext()) {
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                    binding.imageView.setImageURI(imageUri)
                } else {
                    cursor.moveToFirst()
                    val fieldIndex =
                        cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        android.content.ContentUris.withAppendedId(
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                    binding.imageView.setImageURI(imageUri)
                }
            }
            binding.backButton.setOnClickListener {

                if (cursor.moveToPrevious()) {
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                    binding.imageView.setImageURI(imageUri)
                } else {
                    cursor.moveToLast()
                    val fieldIndex =
                        cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        android.content.ContentUris.withAppendedId(
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                    binding.imageView.setImageURI(imageUri)
                }

            }
        }
        binding.gostopButton.setOnClickListener {
            if (timer == null) {
                timer = Timer()
                binding.gostopButton.text = "停止"
                binding.forwardButton.text = "ー"
                binding.backButton.text = "ー"
                binding.forwardButton.isClickable = false
                binding.backButton.isClickable = false
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        seconds += 3
                        handler.post {
                            if (cursor.moveToNext()) {
                                val fieldIndex =
                                    cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                binding.imageView.setImageURI(imageUri)
                            } else {
                                cursor.moveToFirst()
                                val fieldIndex =
                                    cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    android.content.ContentUris.withAppendedId(
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                binding.imageView.setImageURI(imageUri)
                            }
                        }
                    }
                }, 0, 2000)
            } else {
                timer!!.cancel()
                timer = null
                binding.gostopButton.text = "再生"
                binding.forwardButton.text = "進む"
                binding.backButton.text = "戻る"
                binding.forwardButton.isClickable = true
                binding.backButton.isClickable = true
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val resolver = contentResolver
                    val cursor = resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )
                    if (cursor!!.moveToFirst()) {
                        val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                        val id = cursor.getLong(fieldIndex)
                        val imageUri =
                            ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                        binding.imageView.setImageURI(imageUri)
                    }

                    if (timer == null) {
                        binding.forwardButton.setOnClickListener {
                            if (cursor.moveToNext()) {
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                binding.imageView.setImageURI(imageUri)
                            } else {
                                cursor.moveToFirst()
                                val fieldIndex =
                                    cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    android.content.ContentUris.withAppendedId(
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                binding.imageView.setImageURI(imageUri)
                            }
                        }
                        binding.backButton.setOnClickListener {

                            if (cursor.moveToPrevious()) {
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                binding.imageView.setImageURI(imageUri)
                            } else {
                                cursor.moveToLast()
                                val fieldIndex =
                                    cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    android.content.ContentUris.withAppendedId(
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                binding.imageView.setImageURI(imageUri)
                            }

                        }
                    }
                    binding.gostopButton.setOnClickListener {
                        if (timer == null) {
                            timer = Timer()
                            binding.gostopButton.text = "停止"
                            binding.forwardButton.text = "ー"
                            binding.backButton.text = "ー"
                            binding.forwardButton.isClickable = false
                            binding.backButton.isClickable = false
                            timer!!.schedule(object : TimerTask() {
                                override fun run() {
                                    seconds += 3
                                    handler.post {
                                        if (cursor.moveToNext()) {
                                            val fieldIndex =
                                                cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                            val id = cursor.getLong(fieldIndex)
                                            val imageUri =
                                                ContentUris.withAppendedId(
                                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                    id
                                                )
                                            binding.imageView.setImageURI(imageUri)
                                        } else {
                                            cursor.moveToFirst()
                                            val fieldIndex =
                                                cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID)
                                            val id = cursor.getLong(fieldIndex)
                                            val imageUri =
                                                android.content.ContentUris.withAppendedId(
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                    id
                                                )
                                            binding.imageView.setImageURI(imageUri)
                                        }
                                    }
                                }
                            }, 0, 2000)
                        } else {
                            timer!!.cancel()
                            timer = null
                            binding.gostopButton.text = "再生"
                            binding.forwardButton.text = "進む"
                            binding.backButton.text = "戻る"
                            binding.forwardButton.isClickable = true
                            binding.backButton.isClickable = true
                        }
                    }
                }
                else  {
                    binding.forwardButton.isClickable = false
                    binding.backButton.isClickable = false
                    binding.gostopButton.isClickable = false
                    binding.message.text="許可されませんでした"
                }
            }

        }

    }
    private fun getContentsInfo() {
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if(cursor!!.moveToFirst()){
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            binding.imageView.setImageURI(imageUri)
        }
        cursor.close()



    }

}