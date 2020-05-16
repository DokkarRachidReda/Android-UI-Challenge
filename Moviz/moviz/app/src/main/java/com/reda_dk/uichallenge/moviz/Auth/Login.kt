package com.reda_dk.uichallenge.moviz.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.reda_dk.uichallenge.moviz.MainActivity
import com.reda_dk.uichallenge.moviz.R
import com.reda_dk.uichallenge.moviz.model.ServerResponse
import com.reda_dk.uichallenge.moviz.model.User
import com.reda_dk.uichallenge.moviz.requestInterface.MovizApiEndPoints
import com.reda_dk.uichallenge.moviz.season.UserSeason
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Login : AppCompatActivity() {
    var movizBaseUrl = "http://192.168.1.3:3000/"
    val compositeDisposable = CompositeDisposable()
    val mAuth = FirebaseAuth.getInstance()
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



        login.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                mAuth.signInWithEmailAndPassword(mail.text.toString(),pass.text.toString()).
                addOnCompleteListener { task : Task<AuthResult> ->

                    if(task.isSuccessful){

                        compositeDisposable.add(
                            movizApi.getUser(mAuth.uid!!)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                . subscribe(this@Login::onUserDataResponse, this@Login::onUserDataFailure)
                        )

                    }


                }
            }
        })
    }


    override fun onStart() {
        super.onStart()

        if(mAuth.currentUser != null ){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }



    private fun onUserDataFailure(t: Throwable) {
        Log.e("Moviz-api-retrieve","api call failed  : "+t.toString())
    }

    private fun onUserDataResponse(response: User) {
        Log.e("Moviz-api-retrieve","response : 200")

        UserSeason(this).createUserSeason(response)
        startActivity(Intent(this,MainActivity::class.java))

    }
}
