package com.your.orgname.judgment

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.your.orgname.judgment.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private lateinit var binding: ActivityMainBinding
// var binding: ActivityMainBinding? = null
private lateinit var entryLine:ArrayList<Entry>
private lateinit var datasetLine: LineDataSet
private lateinit var linedata: LineData
// 円グラフ用
private lateinit var entryPie:ArrayList<PieEntry>
private lateinit var datasetPie:PieDataSet
var eleColors:ArrayList<Int> = ArrayList()

private var mediaImage: Image? = null
var lengthBox =  0;
var attackPoint = 0;
var i = 0;

var ms = mutableListOf("")

class MainActivity : AppCompatActivity() {
    // ランダム値生成用
    private val range = (0..10)

    private lateinit var cameraExecutor: ExecutorService
    //  private  var binding: ActivityMainBinding? = null
    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.your.orgname.judgment.R.layout.activity_main)
        // Binding Classに含まれる静的 inflate() メソッドを呼び出す
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.message.setTextColor(Color.GREEN);
        binding.result.setTextColor(Color.GREEN);
        setContentView(binding.root)
        //①
        val reFloat = attackPoint.toFloat();
        binding.chartPie.setBackgroundColor(Color.rgb(0,0,0))

        // root view への参照を取得
        val view = binding.root
        binding.message.setText("");
        binding.message.text = "";

        binding.result.setText("");
//        if (allPermissionsGranted()) {
//           startCamera()
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        }
        binding.message.setText("");
        cameraExecutor = Executors.newSingleThreadExecutor()
//        binding.button.setOnClickListener {
//            val context = binding.root.context
//
//            Intent(Intent.ACTION_VIEW).also {
//                val url = "https://www.google.co.jp"
//                it.data = Uri.parse(url)
//                context.startActivity(it)
//            }
//        }

        binding.text.movementMethod = LinkMovementMethod.getInstance()
        //フラグメントの有無を確認し動的にフラグメントを追加する
    }
    fun buttonOnClick(view: View){
        binding.result.setText("loading...");
        mediaImage = null
        lengthBox =  0;
        i = 0;
        ms = mutableListOf("")
        setContentView(com.your.orgname.judgment.R.layout.activity_main)
        // Binding Classに含まれる静的 inflate() メソッドを呼び出す
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.message.setTextColor(Color.GREEN);
        binding.result.setTextColor(Color.GREEN);
        setContentView(binding.root)
        //①
        val reFloat = attackPoint.toFloat();
        binding.chartPie.setBackgroundColor(Color.rgb(0,0,0))

        // root view への参照を取得
        val view = binding.root
        binding.message.setText("");
        binding.message.setText("");
        binding.message.text = "";

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        binding.message.setText("");
        cameraExecutor = Executors.newSingleThreadExecutor()
        //フラグメントの有無を確認し動的にフラグメン
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val preview = Preview.Builder().build()
        val viewFinder: PreviewView = findViewById(com.your.orgname.judgment.R.id.viewFinder)
        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.getSurfaceProvider())
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
                    "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
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



// 以下を追加
// ML Kit Label Images
private class LabelAnalyzer : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        mediaImage = imageProxy?.image ?: return
        Log.d(TAG, "FFFF"+lengthBox)
       if(lengthBox >= 350) {
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
            val inputs2 = ArrayList<PieEntry>()
//            inputs2.add(PieEntry(reFloat,"element1"))
            inputs2.add(PieEntry(attackPoint.toFloat(),"element1"))

            inputs2.add(PieEntry(10f,"element2"))

            binding?.result?.text = "POWER:  $attackPoint Pt";

            var parent = 1000 - attackPoint;
            val sampleData: List<Int> = listOf(parent,attackPoint)
            val sampleLabel: List<String> = listOf("緑", "青", "赤", "灰")
            val pieEntryList: List<PieEntry> = sampleData.zip(sampleLabel).map {
                PieEntry(it.first.toFloat(), it.second)
            }

            val pieDataSet = PieDataSet(pieEntryList, "ラベル")
            pieDataSet.colors = listOf(Color.GRAY,Color.GREEN)
           var pieData = PieData(pieDataSet)

           binding.chartPie.centerText = "PowerMeter"
           binding.chartPie.setCenterTextColor(Color.GREEN)
//            // 背景色の変更
           binding.chartPie.setHoleColor(Color.rgb(0,0,0))
           binding.chartPie.data = pieData
           binding.chartPie.invalidate()
           binding.chartPie.notifyDataSetChanged();

           return
        }

//        val mutableList = mutableListOf(1, 2, 3)
        val list: List<String>;
//        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage!!, imageProxy.imageInfo.rotationDegrees)
            // 画像をラベラーに渡す
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
//                        Log.d(TAG, "labelsA"+labels)
                        val text = label.text;
                        Log.d(TAG, "textA"+text)
                        val confidence = label.confidence;
                        val index = label.index;
                        binding?.message?.setText("$confidence");
                        binding?.message?.text = "$confidence, $index";
                        Log.d(TAG, " $text, $confidence, $index");
                        Log.d(TAG, "aaaaaaa"+ms[i]);
                        Log.d(TAG, "index"+i);
                        ms.add(text);
                        val length = ms.size;
                        Log.d(TAG, "textA"+ms);
                        Log.d(TAG, "length"+length);
                        if (length === 350) {
                            lengthBox = 350;
                            Log.d(TAG, "SSSSSSS"+length)
                            binding.result.setTextColor(Color.RED);
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
//        }
//        else
//        {
//            Log.e(TAG, "Label:aassssss")
//        }

        if (ms.size == 50) {
            return;
        }
    }
}
