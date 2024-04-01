package com.azhamudev.kotlinproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import java.sql.Date

class SplashScreenActivity : AppCompatActivity() {
/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.get_out1 -> {
            // User chose the "exit" item...
            finish()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
 */

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.raxy_splashscreen)
//        val actionbar = supportActionBar
        //set actionbar title
//        actionbar!!.title = "eShakerLab's webview 06dec22 00:35"
        val build_ms = BuildConfig.BUILD_DATE_MS
        val author = BuildConfig.AUTHOR
        val formator = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val buildDate = formator.format( Date(build_ms))
        println("=== apk built on "+buildDate)
        val tv1 = findViewById<TextView>(R.id.tv1)
        tv1.setText("crafted by %s, for ESLabTCR & SWR, on %s".format(author, buildDate))
        val button = findViewById<Button>(R.id.goBtn)
        val cBox1 = findViewById<CheckBox>(R.id.checkBox1)
        val editTextUrl = findViewById<EditText>(R.id.editUrl)
        editTextUrl.onDrawableEndClick {
            editTextUrl.setText("")
        }
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("raxysharedprefs", Context.MODE_PRIVATE)
        val prevUrl = sharedPreferences.getString("url","")
        editTextUrl.setText(prevUrl).toString()
        val intent = Intent(this, WebViewActivity::class.java)
        var theUrl = prevUrl
        val prevFastStart = sharedPreferences.getBoolean("fastStart",false)

        button?.setOnClickListener() {
            theUrl = editTextUrl.text.toString()
//            val toast = Toast.makeText(applicationContext, editTextUrl.text, Toast.LENGTH_SHORT)
//            toast.show()
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString("url",theUrl)
            editor.putBoolean("fastStart",cBox1.isChecked)
            editor.apply()
            editor.commit()
            intent.putExtra("url",theUrl)
            if( theUrl!!.isNotEmpty()) {
                startActivity(intent)
            }
        }
        println("prevUrl:"+prevUrl)
        println("prevFastStart:"+prevFastStart)
        if( prevFastStart && theUrl!!.isNotEmpty()) {
            intent.putExtra("url",theUrl)
            startActivity(intent)
        }
//        Timer().schedule(3000) {
//        }
    }

    fun EditText.onDrawableEndClick(action: () -> Unit) {
        setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v as EditText
                val end = if (v.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
                    v.left else v.right
                if (event.rawX >= (end - v.compoundPaddingEnd)) {
                    action.invoke()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

}