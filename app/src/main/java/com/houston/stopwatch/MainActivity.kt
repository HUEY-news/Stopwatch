package com.houston.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DELAY = 1000L
    }

    private lateinit var mainThreadHandler: Handler

    private lateinit var inputField: EditText
    private lateinit var outputField: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainThreadHandler = Handler(Looper.getMainLooper())

        inputField = findViewById(R.id.inputField)
        outputField = findViewById(R.id.outputField)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            val input = inputField.text.toString().takeIf { it.isNotBlank() }?.toLong() ?: 0L
            if (input <= 0) {
                showMessage("Нихрена не получится без циферок!")
            } else {
                старт(input)
                button.isEnabled = false
            }
        }
    }

    private fun старт (продолжительность: Long) {
        val времяСтарта = System.currentTimeMillis()
        mainThreadHandler.post (
            createUpdateTimerTask(времяСтарта, продолжительность * DELAY)
        )
    }

    private fun createUpdateTimerTask (времяСтарта: Long, продолжительность: Long): Runnable{
        return object: Runnable {
            override fun run () {
                val прошлоВремени = System.currentTimeMillis() - времяСтарта
                val осталосьВремени = продолжительность - прошлоВремени

                if (осталосьВремени > 0){
                    val секунды = осталосьВремени / DELAY
                    outputField.text = String.format("%d:%02d", секунды / 60, секунды % 60)
                    mainThreadHandler.postDelayed(this, DELAY)
                } else {
                    outputField.text = "ГАПОВА!"
                    button.isEnabled = true
                    showMessage("ГАПОВА!")
                }
            }
        }
    }

    private fun showMessage (text: String){
        val rootView = findViewById<View>(android.R.id.content)?.rootView
        if (rootView != null){
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }
}