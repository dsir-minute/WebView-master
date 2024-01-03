package com.azhamudev.kotlinproject

import Prefs
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.sql.Date

class SplashScreenActivity : AppCompatActivity() {

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
        tv1.setText("built by %s for ESLabTCR on %s".format(author, buildDate))
        val button = findViewById<Button>(R.id.goBtn)
        val editTextUrl = findViewById<EditText>(R.id.editUrl)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("raxysharedprefs", Context.MODE_PRIVATE)
        val prevUrl = sharedPreferences.getString("url","not_found")
        if( ! prevUrl.equals("not_found")){
            editTextUrl.setText(prevUrl).toString()
        }

        button?.setOnClickListener() {
            val intent = Intent(this, WebViewActivity::class.java)
            val theUrl = editTextUrl.text.toString()
            intent.putExtra("url",theUrl)
//            val toast = Toast.makeText(applicationContext, editTextUrl.text, Toast.LENGTH_SHORT)
//            toast.show()

            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString("url",theUrl)
            editor.apply()
            editor.commit()
            startActivity(intent)
        }

//        Timer().schedule(3000) {
//        }
    }


}