package com.example.accelerometr

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.TextView

class Accelerometer : Service(), SensorEventListener {
    lateinit var sensorManager: SensorManager
    var x: Float? = 0f
    var y: Float? = 0f
    var z: Float? = 0f
    var text : String? = null

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event : SensorEvent?) {
        val rotationMatrix = FloatArray(16)
        SensorManager.getRotationMatrixFromVector(
            rotationMatrix, event!!.values
        )
        val remappedRotationMatrix = FloatArray(16)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            SensorManager.AXIS_X,
            SensorManager.AXIS_Z,
            remappedRotationMatrix
        )

        val angles = FloatArray(3)
        SensorManager.getOrientation(remappedRotationMatrix, angles)
        for (i in 0..2) {
            angles[i] = Math.toDegrees(angles[i].toDouble()).toFloat()
        }
        var x = angles[0].toInt()
        var y = angles[1].toInt()
        var z = angles[2].toInt()

        x = intervalDegrees(x, 45)
        y = intervalDegrees(y, 45)
        z = intervalDegrees(z, 90)
        val coords = CoordParc(y, x, z)
        sendBroadcastMessage(coords)
//        Log.d("Info", text.toString())
    }

    fun intervalDegrees(angle: Int, max : Int) : Int{
        var x : Int = angle
        if(x > max) {
            x = max
        }
        if(x < -max) {
            x = -max
        }
        return x
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Do a periodic task
        Log.d("Info", "Start")
        registerSensor()
        return START_STICKY
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        null
    }

    fun registerSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_FASTEST)
    }

    fun sendBroadcastMessage(coords : CoordParc) {
        val intent = Intent(MainActivity::class.java.name)
        intent.putExtra("myCoords", coords)
        sendBroadcast(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("Info", "Destroy")
        sensorManager.unregisterListener(this)
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}