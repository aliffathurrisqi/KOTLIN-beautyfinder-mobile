package com.alfari.beautyfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var konfirmasi: String
    lateinit var email: String
    lateinit var responseText: String
    lateinit var etUsername: EditText
    lateinit var etEmail: EditText
    lateinit var etPasssword: EditText
    lateinit var etKonfirmasi: EditText
    lateinit var btnRegist: Button
    lateinit var btnBack: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etRegistUsername) as EditText
        etEmail = findViewById(R.id.etRegistEmail) as EditText
        etPasssword = findViewById(R.id.etRegistPassword) as EditText
        etKonfirmasi = findViewById(R.id.etRegistKonfirmasi) as EditText
        btnRegist = findViewById(R.id.btnDaftar) as Button
        btnBack = findViewById(R.id.btnKembali) as Button

        btnRegist.setOnClickListener {
            username = etUsername.text.toString()
            password = etPasssword.text.toString()
            konfirmasi = etKonfirmasi.text.toString()
            email = etEmail.text.toString()
            register()
        }

        btnBack.setOnClickListener {
            goLogin()
        }
    }

    private fun register() {

//        if(username != ""){
                RetrofitClient.instance.userRegister(
                    username = username,
                    email = email,
                    password = password,
                    konfirmasi = konfirmasi)
                    .enqueue(object: Callback<ArrayList<RegisterResponse>> {
                    override fun onResponse(
                        call: Call<ArrayList<RegisterResponse>>,
                        response: Response<ArrayList<RegisterResponse>>
                    ) {
//                        responseText = response.body()?.get(0)?.response.toString()
//                        if(password.equals(konfirmasi)){
//                            Toast.makeText(applicationContext, "Pendaftaran akun berhasil", Toast.LENGTH_SHORT).show()
//                            goLogin()
//                        }
//                        else{
//                            Toast.makeText(applicationContext, "Konfirmasi Password Salah", Toast.LENGTH_SHORT).show()
//                        }
                    }

                    override fun onFailure(call: Call<ArrayList<RegisterResponse>>, t: Throwable) {
                        if(username != "" && password != ""){
                            if(password.equals(konfirmasi)){
                                Toast.makeText(applicationContext, "Pendaftaran akun berhasil", Toast.LENGTH_SHORT).show()
                                goLogin()
                            }
                            else{
                                Toast.makeText(applicationContext, "Konfirmasi Password Salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            etPasssword.text = null
                            etKonfirmasi.text = null
                            Toast.makeText(applicationContext, "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
    //                    Toast.makeText(applicationContext, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show()
                    }

                })
//
//        }
//        else{
//            Toast.makeText(applicationContext, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}