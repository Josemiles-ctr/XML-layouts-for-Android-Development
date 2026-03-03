package com.example.mobileprogrammingxml

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

/**
 * Calculator screen.
 *
 * The display [EditText] is set non-focusable / non-clickable with
 * inputType="none" in XML so the soft keyboard never appears and cursor
 * positioning is a non-issue.  All arithmetic is evaluated at runtime by
 * the exp4j library via [calculate].
 *
 * State machine:
 *   - [enabled]  — false while the calculator is "off" (all input is ignored).
 *   - [isError]  — true after an invalid expression; the next digit press clears
 *                  the error and starts a fresh input.
 */
class SecondActivity : AppCompatActivity() {
    /** The calculator display — shows the current expression or result. */
    private lateinit var screen: EditText

    /** Whether the calculator is powered on and accepting input. */
    private var enabled = true

    /** True when the display is showing an error message instead of a valid expression. */
    private var isError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        screen = findViewById(R.id.welcome_text)
    }

    /**
     * Updates the calculator display with [value], resets the error flag, and
     * restores the normal (non-error) text colour.
     */
    private fun setScreen(value: String) {
        screen.typeface = Typeface.MONOSPACE
        screen.setTextColor(getColor(R.color.calc_display_text))
        screen.setText(value)
        isError = false
    }

    /**
     * Called by every digit and operator button via [android:onClick].
     *
     * If the display is in an error state the error is cleared and the pressed
     * character starts a fresh expression.  If the display currently shows "0",
     * the zero is replaced rather than appended.
     */
    @Suppress("UNUSED_PARAMETER")
    fun addInput(view: View) {
        if (!enabled) return
        val passed = (view as android.widget.Button).text.toString()

        if (isError) {
            // Clear error and start fresh with the pressed digit
            setScreen(passed)
        } else {
            setScreen(
                // Replace a lone "0" instead of appending to it
                if (screen.text.toString() == "0") passed
                else getString(R.string.calc_display, screen.text.toString(), passed)
            )
        }
    }

    /**
     * Removes the last character from the display (backspace).
     * Resets to "0" when the expression becomes empty or on error.
     * Called by the DEL button.
     */
    @Suppress("UNUSED_PARAMETER")
    fun popNumber(view: View) {
        if (!enabled) return
        if (isError) {
            setScreen(getString(R.string._0))
            return
        }
        val str = screen.text.toString()
        if (str.length > 1) {
            setScreen(str.dropLast(1))
        } else {
            // Single character left — reset to neutral "0"
            setScreen(getString(R.string._0))
        }
    }

    /**
     * Powers the calculator off: clears the display, sets a black background,
     * and disables the [EditText] so no further input is accepted.
     * Called by the OFF button.
     */
    @Suppress("UNUSED_PARAMETER")
    fun powerOff(view: View) {
        enabled = false
        isError = false
        screen.setText("")
        screen.hint = ""
        screen.setBackgroundColor(getColor(R.color.black))
        screen.isEnabled = false
    }

    /**
     * Powers the calculator on: resets the display to "0" and restores the
     * rounded-corner background drawable.
     * Called by the ON button.
     */
    @Suppress("UNUSED_PARAMETER")
    fun powerOn(view: View) {
        enabled = true
        setScreen(getString(R.string._0))
        screen.hint = getString(R.string._0)
        screen.setBackgroundResource(R.drawable.rounded_corners)
        screen.isEnabled = true
    }

    /**
     * Evaluates the expression currently shown on the display using exp4j.
     *
     * Before parsing, display symbols (×, ÷, −) are mapped to their ASCII
     * equivalents that exp4j understands (* / -).
     *
     * Integer results are shown without a decimal point (e.g. "6" not "6.0").
     * Division by zero and malformed expressions show an error via [showError].
     * Called by the ANS button.
     */
    @Suppress("UNUSED_PARAMETER")
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
                // Show as integer when the result has no fractional part
                setScreen(
                    if (result == result.toLong().toDouble()) result.toLong().toString()
                    else result.toString()
                )
            }
        } catch (_: ArithmeticException) {
            showError("Math Error")
        } catch (_: Exception) {
            showError("Invalid Expr")
        }
    }

    /**
     * Displays [message] in red on the calculator screen and sets [isError] = true.
     * The next call to [addInput] will automatically clear the error.
     */
    private fun showError(message: String) {
        isError = true
        screen.typeface = Typeface.MONOSPACE
        screen.setTextColor(getColor(android.R.color.holo_red_light))
        screen.setText(message)
    }

}
