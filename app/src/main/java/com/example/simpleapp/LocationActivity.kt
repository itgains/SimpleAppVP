package com.example.simpleapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class LocationActivity : AppCompatActivity() {

    private lateinit var tvLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        tvLocation = findViewById(R.id.tvLocation)

        val btnGetLocation = findViewById<Button>(R.id.btnGetLocation)
        val btnBack = findViewById<Button>(R.id.btnBackFromLocation)

        btnGetLocation.setOnClickListener { getLocation() }
        btnBack.setOnClickListener { finish() }
    }

    private fun getLocation() {
        // Проверяем разрешение
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Запрашиваем разрешение
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        // Получаем локацию
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        var location: Location? = null

        try {
            // Пробуем получить локацию от GPS
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            // Если GPS не дал результат, пробуем сеть
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Ошибка доступа к локации", Toast.LENGTH_SHORT).show()
        }

        // Отображаем результат
        if (location != null) {
            val lat = location.latitude
            val lon = location.longitude
            tvLocation.text = "Широта: $lat\nДолгота: $lon"
            Toast.makeText(this, "Локация найдена!", Toast.LENGTH_SHORT).show()
        } else {
            tvLocation.text = "Локация не найдена\nПопробуйте снова"
            Toast.makeText(this, "Локация не найдена", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Повторно пытаемся получить локацию после выдачи разрешения
            getLocation()
        } else {
            Toast.makeText(this, "Нужно разрешение на доступ к локации", Toast.LENGTH_SHORT).show()
        }
    }
}