package com.example.accelerometr

import android.content.*
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var broadCastReceiver : BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var image : ImageView = findViewById<ImageView>(R.id.image)
        broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                var coords : CoordParc? = intent?.getParcelableExtra("myCoords")
                val text = "angleX = ${coords?.x}, angleY = ${coords?.y}, angleZ = ${coords?.z}"
                Log.d("Info", text)
                image.rotation = coords?.z?.toFloat()!!
                image.rotationX = coords.x.toFloat()
                image.rotationY = -coords.y.toFloat()
//                Log.d("Info", coords.x.toString())
            }
        }
        val filter = IntentFilter(MainActivity::class.java.name)
        registerReceiver(broadCastReceiver, filter)
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, Accelerometer::class.java);
        startService(intent)
    }

    override fun onPause() {
        super.onPause()
        val intent = Intent(this, Accelerometer::class.java);
        stopService(intent)
        unregisterReceiver(broadCastReceiver)
    }

}