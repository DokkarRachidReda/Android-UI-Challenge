package com.reda_dk.uichallenge.moviz.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.reda_dk.uichallenge.moviz.R
import com.reda_dk.uichallenge.moviz.model.ServerResponse
import com.reda_dk.uichallenge.moviz.model.User
import com.reda_dk.uichallenge.moviz.requestInterface.MovizApiEndPoints
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Register : AppCompatActivity() {
    final var movizBaseUrl = "http://192.168.1.8:3000/"
    val compositeDisposable = CompositeDisposable()
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


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


        signUp.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                mAuth.createUserWithEmailAndPassword(s_mail.text.toString(), s_pass.text.toString())
                    .addOnCompleteListener { task: Task<AuthResult> ->

                        if(task.isSuccessful){
                            // create user in db
                            val user = User(mAuth.uid!!,s_mail.text.toString(),s_username.text.toString(),"")
                            compositeDisposable.add(
                                movizApi.createUser(user)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    . subscribe(this@Register::onCreateResponse, this@Register::onCreateFailure)
                            )



                        }

                    }

            }
        })
    }







    private fun onCreateFailure(t: Throwable) {

        Log.e("Moviz-api-create-user","api call failed  : "+t.toString())
    }

    private fun onCreateResponse(response: ServerResponse) {
        Log.e("Moviz-api-create-user","response : ${response.status}" )

        if(response.status.equals("200")){

        }else{
            Toast.makeText(this,"an error has occurred !!",Toast.LENGTH_SHORT).show()
            mAuth.currentUser!!.delete()
        }


    }
}
