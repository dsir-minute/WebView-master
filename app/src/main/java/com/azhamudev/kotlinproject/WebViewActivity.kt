package com.azhamudev.kotlinproject

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.http.SslError
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_view.loaderImage
import kotlinx.android.synthetic.main.activity_web_view.webView

class WebViewActivity : AppCompatActivity(){
    private var isAlreadyCreated = false
    var context = this
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
//may23, because alpine/lci/eslab gamechanger        val actionbar = supportActionBar
        //set actionbar title
//        actionbar!!.title = "crafted by eShakerLab, 2022"

        setContentView(R.layout.activity_web_view)
        val URL = intent.getStringExtra("url")
//        val toast = Toast.makeText(applicationContext, URL, Toast.LENGTH_SHORT)
//        toast.show()
        startLoaderAnimate()
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)
        webView.setInitialScale(100)
        webView.settings.domStorageEnabled = true // needed on android 13+ else screen might stays blank
        WebView.setWebContentsDebuggingEnabled(true);
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
                handler.proceed() // Ignore SSL certificate errors
            }
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if(url.startsWith("intent:")) {
                    // url like : intent:#Intent;action=android.intent.action.MAIN;package=com.azhamudev.kotlinproject;end
                    var arr1 = url.takeIf(String::isNotEmpty)
                        ?.split(";")
                        ?.toTypedArray()
                        ?: emptyArray()
                    var found = ""
                    for(element in arr1) {
                        if( element.startsWith("package=")){
                            var arr2 = element.takeIf(String::isNotEmpty)
                                ?.split("=")
                                ?.toTypedArray()
                                ?: emptyArray()
                            println( "package name is "+arr2[1])
                            found = arr2[1]
                            break
                        }
                    }
                    if( found.isNotEmpty()) {
//                        view.stopLoading()

                        val intent = Intent("android.intent.action.MAIN")
                        intent.setPackage(found)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent)

                        // FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        // FLAG_ACTIVITY_REORDER_TO_FRONT
                        // FLAG_ACTIVITY_LAUNCH_ADJACENT
                        // FLAG_ACTIVITY_NEW_TASK
                        // FLAG_ACTIVITY_CLEAR_TOP
                        /*
                        val openURL = Intent(android.content.Intent.ACTION_VIEW)
                        openURL.data = Uri.parse("http://192.168.100.191:8080/VelvetWeb/?tablet=1&buttonText=MONBUTTO&buttonUrl=http//www.google.com")
                        startActivity(openURL)

                         */
                        return true
                    }
                } else {
//                        view.stopLoading()
//                        view.loadUrl(url)
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
/*
            override fun onLoadResource(view: WebView, url: String) {
//                    println("in onLoadResource "+url)
                if(url.startsWith("intent:")) {
                    // url like : intent:#Intent;action=android.intent.action.MAIN;package=com.azhamudev.kotlinproject;end
                    var arr1 = url.takeIf(String::isNotEmpty)
                        ?.split(";")
                        ?.toTypedArray()
                        ?: emptyArray()
                    var found = ""
                    for(element in arr1) {
                        if( element.startsWith("package=")){
                            var arr2 = element.takeIf(String::isNotEmpty)
                                ?.split("=")
                                ?.toTypedArray()
                                ?: emptyArray()
                            println( "package name is "+arr2[1])
                            found = arr2[1]
                            break
                        }
                    }
                    if( found.isNotEmpty()) {
//                        view.stopLoading()
                        /*
                        val intent = Intent("android.intent.action.MAIN")
                        intent.setPackage(found)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent)

                         */
                        // FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        // FLAG_ACTIVITY_REORDER_TO_FRONT
                        // FLAG_ACTIVITY_LAUNCH_ADJACENT
                        // FLAG_ACTIVITY_NEW_TASK
                        // FLAG_ACTIVITY_CLEAR_TOP
                        val openURL = Intent(android.content.Intent.ACTION_VIEW)
                        openURL.data = Uri.parse("http://192.168.100.191:8080/VelvetWeb/?tablet=1&buttonText=MONBUTTO&buttonUrl=http//www.google.com")
                        startActivity(openURL)
                    }
                } else {
//                        view.stopLoading()
//                        view.loadUrl(url)
                }
            }
*/
            override fun onPageFinished(view: WebView?, url: String?) {
                endLoaderAnimate()
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                endLoaderAnimate()
                showErrorDialog("Error",
                    "in onReceivedError : "+error,
                    this@WebViewActivity
                )
            }
        }
        webView.clearCache(true)
        webView.loadUrl(URL.toString())
    }

    override fun onResume() {
        super.onResume()
        if (isAlreadyCreated && !isNetworkAvailable()) {
            isAlreadyCreated = false
            showErrorDialog("in onResume Error : ", "isAlreadyCreated && !isNetworkAvailable ",
                    this@WebViewActivity)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager =
                this@WebViewActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorDialog(title: String, message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("Cancel", { _, _ ->
            this@WebViewActivity.finish()
        })
        dialog.setNeutralButton("Settings", {_, _ ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        })
        dialog.setPositiveButton("Retry", { _, _ ->
            this@WebViewActivity.recreate()
        })
        dialog.create().show()
    }

    private fun endLoaderAnimate() {
        loaderImage.clearAnimation()
        loaderImage.visibility = View.GONE
    }

    private fun startLoaderAnimate() {
        val objectAnimator = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val startHeight = 170
                val newHeight = (startHeight + (startHeight + 40) * interpolatedTime).toInt()
                loaderImage.layoutParams.height = newHeight
                loaderImage.requestLayout()
            }
            override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
                super.initialize(width, height, parentWidth, parentHeight)
            }
            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        objectAnimator.repeatCount = -1
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.duration = 700
        loaderImage.startAnimation(objectAnimator)
    }
}