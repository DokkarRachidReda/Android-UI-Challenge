package com.reda_dk.uichallenge.moviz.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.reda_dk.uichallenge.moviz.R
import com.reda_dk.uichallenge.moviz.requestInterface.MovizApiEndPoints
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Login : AppCompatActivity() {
    var movizBaseUrl = "http://192.168.1.3:3000/"
    val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mclient = OkHttpClient.Builder()
            .connectTimeout(180, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()



        val Movizretrofit = Retrofit.Builder()
            .baseUrl(movizBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(mclient)
            .build()

        val movizApi = Movizretrofit.create(MovizApiEndPoints::class.java)

        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(mail.text.toString(),pass.text.toString()).
        addOnCompleteListener { task : Task<AuthResult> ->

            if(task.isSuccessful){

                

            }


        }
    }
}
