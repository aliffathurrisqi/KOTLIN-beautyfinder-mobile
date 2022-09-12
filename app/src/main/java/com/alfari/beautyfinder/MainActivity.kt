package com.alfari.beautyfinder

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var mUploadMessage: ValueCallback<Uri>? = null
    private var uploadMessage: ValueCallback<Array<Uri>>? = null

    val FILECHOOSER_RESULTCODE = 1
    val REQUEST_SELECT_FILE = 100



    private lateinit var alfariWebview : WebView
    lateinit var bottomNavigation : BottomNavigationView
//    lateinit var btnHome : BottomNavigationMenuView
//    lateinit var btnTambah : BottomNavigationMenuView
//    lateinit var btnKategori : BottomNavigationMenuView
//    lateinit var btnAkun : BottomNavigationMenuView
    lateinit var data:String
    lateinit var location:String
    lateinit var url:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        data = intent.getStringExtra("loginUsername").toString()
        location = intent.getStringExtra("location").toString()
        url = "http://beautyfinder.alfaristudio.my.id/"

        alfariWebview = findViewById(R.id.alfariWebview)
        bottomNavigation = findViewById(R.id.bottom_navigation)


        alfari_webview_setup(url + "index.php?username=" + data + "&location=" + location + "&search=")

        bottomNavigation.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.ic_home -> alfari_webview_setup(url + "index.php?username=" + data + "&location=" + location + "&search=")
                R.id.ic_plus -> alfari_webview_setup(url + "data_add.php?location=" + location)
                R.id.ic_user -> alfari_webview_setup(url + "people.php?id=" + data + "&location=" + location)
                R.id.ic_logout -> goLogin()
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private fun alfari_webview_setup(url: String){

        alfariWebview.webViewClient = WebViewClient()

        alfariWebview.apply{
//            loadUrl("http://beautyfinder.epizy.com/?search=")
            loadUrl(url)
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            settings.setGeolocationEnabled(true)

        }

        //

        alfariWebview.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                Log.d("MainActivity", "onPermissionRequest")
//                requestPermission(request)
            }



            // For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<*>, acceptType: String) {
                mUploadMessage = uploadMsg as ValueCallback<Uri>
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                this@MainActivity.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE)
            }

            //For Android 4.1
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                this@MainActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)

            }

            protected fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                mUploadMessage = uploadMsg
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE)
            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                uploadMessage?.onReceiveValue(null)
                uploadMessage = null

                uploadMessage = filePathCallback

                val intent = fileChooserParams!!.createIntent()
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
                    Toast.makeText(applicationContext, "Cannot Open File Chooser", Toast.LENGTH_LONG).show()
                    return false
                }

                return true
            }

        }


        //
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode === REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return
                print("result code = " + resultCode)
                var results: Array<Uri>? = WebChromeClient.FileChooserParams.parseResult(resultCode, data)
                uploadMessage?.onReceiveValue(results)
                uploadMessage = null
            }
        } else if (requestCode === FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            val result = if (intent == null || resultCode !== RESULT_OK) null else intent.data
            mUploadMessage?.onReceiveValue(result)
            mUploadMessage = null
        } else
            Toast.makeText(applicationContext, "Failed to Upload Image", Toast.LENGTH_LONG).show()
    }



    override fun onBackPressed() {
        if(alfariWebview.canGoBack())
            alfariWebview.goBack()
        else
            super.onBackPressed()
    }

    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}