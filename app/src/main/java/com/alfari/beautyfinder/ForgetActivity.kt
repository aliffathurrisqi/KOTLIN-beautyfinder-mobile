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

class ForgetActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var etUsername: EditText
    lateinit var btnKirim: Button
    lateinit var btnBack: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        etUsername = findViewById(R.id.etF_Username) as EditText
        btnKirim = findViewById(R.id.btnKirim) as Button
        btnBack = findViewById(R.id.btnMenu) as Button

        btnKirim.setOnClickListener {
            username = etUsername.text.toString()
            register()
        }

        btnBack.setOnClickListener {
            goLogin()
        }
    }

    private fun register() {

//        if(username != ""){
                RetrofitClient.instance.userForget(
                    id = username,)
                    .enqueue(object: Callback<ArrayList<ForgetResponse>> {
                    override fun onResponse(
                        call: Call<ArrayList<ForgetResponse>>,
                        response: Response<ArrayList<ForgetResponse>>
                    ) {

//                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ForgetResponse>>, t: Throwable) {
                        if(username != ""){
                            Toast.makeText(applicationContext, "Permintaan di proses, silakan cek email", Toast.LENGTH_SHORT).show()
                            goLogin()
                        }
                        else{
                            Toast.makeText(applicationContext, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                    }

                })

    }

    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}