package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.lemonade.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    /**
     * DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES.
     *
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has be drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }

        setViewElements()
        binding.imageLemonState.setOnClickListener {
            clickLemonImage()
        }
        binding.imageLemonState.setOnLongClickListener {
            showSnackbar()
        }
    }

    /**
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {
        when(lemonadeState){
            SELECT -> {
                lemonadeState = SQUEEZE
                lemonSize = lemonTree.pick()
                squeezeCount = 0
                setViewElements()
            }
            SQUEEZE -> {
                squeezeCount++
                lemonSize--
                if(lemonSize==0){
                    lemonadeState = DRINK
                    squeezeCount = -1
                    setViewElements()
                }
            }
            DRINK -> {
                lemonadeState = RESTART
                lemonSize = -1
                setViewElements()
            }
            RESTART -> {
                lemonadeState = SELECT
                setViewElements()
            }
        }
    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        when(lemonadeState){
            SELECT -> {
                binding.textAction.text = getString(R.string.lemon_select)
                binding.imageLemonState.setImageResource(R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                binding.textAction.text = getString(R.string.lemon_squeeze)
                binding.imageLemonState.setImageResource(R.drawable.lemon_squeeze)
            }
            DRINK -> {
                binding.textAction.text = getString(R.string.lemon_drink)
                binding.imageLemonState.setImageResource(R.drawable.lemon_drink)
            }
            RESTART -> {
                binding.textAction.text = getString(R.string.lemon_empty_glass)
                binding.imageLemonState.setImageResource(R.drawable.lemon_restart)
            }
        }
    }

    /**
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(binding.root, squeezeText, Snackbar.LENGTH_SHORT).show()
        return true
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
