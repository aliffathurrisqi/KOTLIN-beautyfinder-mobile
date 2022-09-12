package com.alfari.beautyfinder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create

import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.jar.Manifest
import java.math.BigInteger
import java.security.MessageDigest

class LoginActivity() : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var etUsername: EditText
    lateinit var etPasssword: EditText
    lateinit var btnMasuk: Button
    lateinit var btnRegister: Button
    lateinit var btnLupa: Button

    lateinit var latitude: String
    lateinit var longitude: String

    private var loginPassword: String = ""

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fetchLocation()

        etUsername = findViewById(R.id.etUsername) as EditText
        etPasssword = findViewById(R.id.etPassword) as EditText
        btnMasuk = findViewById(R.id.btnMasuk) as Button
        btnRegister = findViewById(R.id.btnRegister) as Button
        btnLupa = findViewById(R.id.btnLupaPassword) as Button

        btnMasuk.setOnClickListener {
            username = etUsername.text.toString()
            password = etPasssword.text.toString()
            Login(username)
        }

        btnRegister.setOnClickListener {
            goRegister()
        }

        btnLupa.setOnClickListener {
            goForget()
        }

    }

//    Mendeteksi lokasi mobile
    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
      task.addOnSuccessListener {
          if(it != null){
              latitude = it.latitude.toString()
              longitude = it.longitude.toString()
          }
      }
    }

    private fun Login(username: String){

        RetrofitClient.instance.getUser(id = username).
        enqueue(object: Callback<ArrayList<LoginResponse>>{
            override fun onResponse(
                call: Call<ArrayList<LoginResponse>>,
                 response: Response<ArrayList<LoginResponse>>
            ) {

                val md5 = MessageDigest.getInstance("MD5")
                md5.update(password.toByteArray())
                val enkripsiPassword = BigInteger(1, md5.digest()).toString(16)

                loginPassword = response.body()?.get(0)?.password.toString()
                if(username != ""){
                    if(loginPassword.equals(enkripsiPassword)){
                        goHome(response.body()?.get(0)?.username.toString())
                    }
                    else{
                        etPasssword.text = null
                        Toast.makeText(applicationContext, "Password anda tidak cocok", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<LoginResponse>>, t: Throwable) {
                Toast.makeText(applicationContext, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun goHome(userLogin: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("loginUsername", userLogin)
        intent.putExtra("location", latitude + "," + longitude)
        startActivity(intent)
    }

    private fun goRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun goForget(){
        val intent = Intent(this, ForgetActivity::class.java)
        startActivity(intent)
    }
}