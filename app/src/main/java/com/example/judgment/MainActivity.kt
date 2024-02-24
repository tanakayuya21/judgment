package com.example.judgment

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.judgment.databinding.ActivityMainBinding

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private lateinit var binding: ActivityMainBinding
// var binding: ActivityMainBinding? = null
class MainActivity : AppCompatActivity() {
    private lateinit var cameraExecutor: ExecutorService
    //  private  var binding: ActivityMainBinding? = null
    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Binding Classに含まれる静的 inflate() メソッドを呼び出す
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // チャート表示：円グラフ-------------------------------------------
        // 手順：①データを用意　②DataSetにデータ群を入れる　③DataにDatasetを追加　④チャートのデータにDataを入れる
        //①
        // 円グラフ用
//        private lateinit var entryPie:ArrayList<PieEntry>
//        private lateinit var datasetPie:PieDataSet
//        private lateinit var piedata: PieData
//
//        entryPie = ArrayList<com.github.mikephil.charting.data.PieEntry>()
//        //②
//        datasetPie = PieDataSet(entryPie,"Pie Sample")
//        //③
//        piedata = PieData(datasetPie)
//        //④
//        binding.chartPie.data = piedata
//        binding.chartPie.setBackgroundColor(Color.rgb(255,255,200))
        // root view への参照を取得
        val view = binding.root
//        binding.message.setText("こんにちは");
//        binding.message.setText("こんにちは");

        //  binding.message.text = "こんにちは";
        // view をsetContentView()にセット
//        setContentView(view)
//        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        //  binding.message.setText("こんにちは");

        cameraExecutor = Executors.newSingleThreadExecutor()
        //フラグメントの有無を確認し動的にフラグメントを追加する
//        if (supportFragmentManager.findFragmentByTag("labelFragment") == null){
//            supportFragmentManager.beginTransaction()
//                .add(R.id.container, newLabelFragment(0),"labelFragment")
//                .commit()
//        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val preview = Preview.Builder().build()
        val viewFinder: PreviewView = findViewById(R.id.viewFinder)
        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
//                  it.setSurfaceProvider(viewFinder.getSurfaceProvider())
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }
            // imageAnalyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    // ここ重要
                    // ここに画像解析をセット
                    it.setAnalyzer(cameraExecutor, LabelAnalyzer())
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                // ここも重要
                // カメラのライフサイクルに画像解析を組み込む
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
val ms = mutableListOf("穂乃果", "ことり", "海未")

var lengthBox =  0;
var attackPoint = 0;
// 以下を追加
// ML Kit Label Images
private class LabelAnalyzer : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy?.image ?: return

        if(lengthBox === 350) {
            val Dog = ms.count { it -> it.contains("Dog") };
            val Cat = ms.count { it -> it.contains("Cat") }
            val Pet = ms.count { it -> it.contains("Pet") }
            val Insect = ms.count { it -> it.contains("Insect") }
            val Horse = ms.count { it -> it.contains("Horse") }

            Log.d(TAG, "Dog"+Dog.toString())
            Log.d(TAG, "Cat"+Cat.toString())
            Log.d(TAG, "Pet"+Pet.toString())
            Log.d(TAG, "Insect"+Insect.toString())
            Log.d(TAG, "Horse"+Horse.toString())


            attackPoint +=  Dog * 5
            attackPoint += Cat + 1
            attackPoint += Pet + 1
            attackPoint += Horse * 10


//            if (Dog >= 100) {
//                attackPoint += attackPoint + Dog * 5
//
//                println("数字は10")
//            }
//            if (Cat >= 10) {
//                attackPoint += Cat + 1
//                println("数字は100")
//            }
//            if  (Pet >= 10) {
//                attackPoint += Pet + 1
//                println("10でも100でもない")
//            }
//            if  (Horse >= 50) {
//                attackPoint += Horse * 10
//                println("10でも100でもない")
//            }

            Log.d(TAG, "Horse"+Horse.toString())
            Log.d(TAG, "attackPoint"+attackPoint.toString())

            binding?.result?.text = "attackPoint:  $attackPoint";

            return
        }

//        val mutableList = mutableListOf(1, 2, 3)
        val list: List<String>;

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // 画像をラベラーに渡す
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    // ラベリング結果をログに出力
//                    for (label in labels) {
//                        val text = label.text
//                        val confidence = label.confidence
//                        val index = label.index
//                        binding?.message?.setText("$confidence");
//                        binding?.message?.text = "Label: $text, $confidence, $index";
//                        Log.d(TAG, "Label: $text, $confidence, $index")
//                    }

                    val i = 0;
                    for (label in labels) {
//                        Log.d(TAG, "labelsA"+labels)
                        val text = label.text;
                        Log.d(TAG, "textA"+text)
                        val confidence = label.confidence;
                        val index = label.index;
                        binding?.message?.setText("$confidence");
                        binding?.message?.text = "Label: $text, $confidence, $index";
                        Log.d(TAG, "Label: $text, $confidence, $index");
                        Log.d(TAG, "aaaaaaa"+ms[i]);
                        Log.d(TAG, "index"+i);
                        ms.add(text);
                        val length = ms.size;
                        Log.d(TAG, "textA"+ms);
                        Log.d(TAG, "length"+length);
                        if (length === 350) {
                            lengthBox = 350;
                            Log.d(TAG, "SSSSSSS"+length)
                            break
                        }
                    }

                    Log.e(TAG, ms.size.toString()+"AAAAAAAAAAAAA")
//                    for(i in array.indices){
//                        println(array[i])
//                    }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Log.e(TAG, "Label: $e")
                }
                // ImageProxyは使い終わったら閉じましょう
                .addOnCompleteListener { results -> imageProxy.close() }
        }
        else
        {
            Log.e(TAG, "Label:aassssss")
        }

        if (ms.size == 50) {
            return;
        }
    }
}
