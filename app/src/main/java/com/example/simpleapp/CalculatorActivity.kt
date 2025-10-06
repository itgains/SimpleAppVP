package com.example.simpleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class CalculatorActivity : AppCompatActivity() {

    private lateinit var etNumber1: EditText
    private lateinit var etNumber2: EditText
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        etNumber1 = findViewById(R.id.etNumber1)
        etNumber2 = findViewById(R.id.etNumber2)
        tvResult = findViewById(R.id.tvResult)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnSubtract = findViewById<Button>(R.id.btnSubtract)
        val btnMultiply = findViewById<Button>(R.id.btnMultiply)
        val btnDivide = findViewById<Button>(R.id.btnDivide)
        val btnBack = findViewById<Button>(R.id.btnBackFromCalc)

        btnAdd.setOnClickListener { calculate('+') }
        btnSubtract.setOnClickListener { calculate('-') }
        btnMultiply.setOnClickListener { calculate('*') }
        btnDivide.setOnClickListener { calculate('/') }
        btnBack.setOnClickListener { finish() }
    }

    private fun calculate(operation: Char) {
        val num1Str = etNumber1.text.toString()
        val num2Str = etNumber2.text.toString()

        if (num1Str.isEmpty() || num2Str.isEmpty()) {
            showToast("Введите оба числа")
            return
        }

        try {
            val num1 = num1Str.toDouble()
            val num2 = num2Str.toDouble()
            val result = when (operation) {
                '+' -> num1 + num2
                '-' -> num1 - num2
                '*' -> num1 * num2
                '/' -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }

            if (result.isNaN()) {
                tvResult.text = "Ошибка: деление на ноль"
            } else {
                tvResult.text = "Результат: $result"
            }
        } catch (e: Exception) {
            showToast("Ошибка в числах")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}