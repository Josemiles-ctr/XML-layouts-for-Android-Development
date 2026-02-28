package com.example.mobileprogrammingxml

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class SecondActivity : AppCompatActivity() {
    private lateinit var screen: EditText
    var enabled = true
    private var isError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        screen = findViewById(R.id.welcome_text)
        screen.isSingleLine = true
        screen.setHorizontallyScrolling(true)

        // Always keep cursor at the end
        screen.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                screen.setSelection(screen.text.length)
            }
        })

        // Prevent user from repositioning cursor by tapping
        screen.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                screen.setSelection(screen.text.length)
            }
            false
        }

        screen.setCursorToEnd()
    }

    private fun EditText.setCursorToEnd() {
        setSelection(text.length)
    }

    private fun setScreen(value: String) {
        screen.typeface = Typeface.MONOSPACE
        screen.setTextColor(getColor(R.color.calc_display_text))
        screen.setText(value)
        screen.setCursorToEnd()
        isError = false
    }

    fun addInput(view: View) {
        if (!enabled) return
        val passed = (view as android.widget.Button).text.toString()

        if (isError) {
            // Clear error and start fresh with the pressed digit
            setScreen(passed)
        } else {
            setScreen(
                if (screen.text.toString() == "0") passed
                else getString(R.string.calc_display, screen.text.toString(), passed)
            )
        }
    }

    fun popNumber(view: View) {
        if (isError) {
            setScreen(getString(R.string._0))
            return
        }
        val str = screen.text.toString()
        if (str.length > 1) {
            setScreen(str.dropLast(1))
        } else {
            setScreen(getString(R.string._0))
        }
    }

    fun powerOff(view: View) {
        enabled = false
        isError = false
        screen.setText("")
        screen.hint = ""
        screen.setBackgroundColor(getColor(R.color.black))
        screen.isEnabled = false
    }

    fun powerOn(view: View) {
        enabled = true
        setScreen(getString(R.string._0))
        screen.hint = getString(R.string._0)
        screen.setBackgroundResource(R.drawable.rounded_corners)
        screen.isEnabled = true
    }

    fun calculate(view: View) {
        if (!enabled) return
        val raw = screen.text.toString()
        // Convert UX symbols to valid math operators for evaluation
        val expression = raw
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("–", "-")

        try {
            val result = ExpressionBuilder(expression).build().evaluate()
            if (result.isInfinite() || result.isNaN()) {
                showError("Math Error")
            } else {
                setScreen(
                    if (result == result.toLong().toDouble()) result.toLong().toString()
                    else result.toString()
                )
            }
        } catch (e: ArithmeticException) {
            showError("Math Error")
        } catch (e: Exception) {
            showError("Invalid Expr")
        }
    }

    private fun showError(message: String) {
        isError = true
        screen.typeface = Typeface.MONOSPACE
        screen.setTextColor(getColor(android.R.color.holo_red_light))
        screen.setText(message)
        screen.setSelection(screen.text.length)
    }

}
