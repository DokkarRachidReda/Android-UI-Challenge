package com.reda_dk.uichallenge.moviz.Auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.reda_dk.uichallenge.moviz.MainActivity
import com.reda_dk.uichallenge.moviz.R
import com.reda_dk.uichallenge.moviz.model.ServerResponse
import com.reda_dk.uichallenge.moviz.model.User
import com.reda_dk.uichallenge.moviz.requestInterface.MovizApiEndPoints
import com.reda_dk.uichallenge.moviz.season.UserSeason
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit




class Register : AppCompatActivity() {
    var movizBaseUrl = "http://192.168.1.3:3000/"
    val compositeDisposable = CompositeDisposable()
    val mAuth = FirebaseAuth.getInstance()

    lateinit var movizApi:MovizApiEndPoints

    var imageUri:Uri? = null

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

         movizApi = Movizretrofit.create(MovizApiEndPoints::class.java)


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



        user_image.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                Dexter.withContext(this@Register)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object:PermissionListener{
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            var intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.setType("image/*")
                            startActivityForResult(intent, 1000)
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?, p1: PermissionToken?) {}

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                            Toast.makeText(this@Register,
                                "this permission is needed to choose an image",Toast.LENGTH_SHORT).show()
                        }
                    })
                    .check()
            }
        });
    }







    private fun onCreateFailure(t: Throwable) {

        Log.e("Moviz-api-create-user","api call failed  : "+t.toString())
    }

    private fun onCreateResponse(response: ServerResponse) {
        Log.e("Moviz-api-create-user","response : ${response.status}" )

        if(response.status.equals("200")){

                if(imageUri != null){
                    val file = File((imageUri!!.path!!).removeRange(0,4))  //(imageUri!!.path!!).removeRange(0,4)

                    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("img", file.name, requestBody)

                    compositeDisposable.add(
                        movizApi.uploadUserImage(mAuth.uid!!,part)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            . subscribe(this@Register::onUploadResponse, this@Register::onUploadFailure)
                    )
                }else {
                    Log.e("user-image","no image has been selected")
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }


        }else{
            Toast.makeText(this,"an error has occurred !!",Toast.LENGTH_SHORT).show()
            mAuth.currentUser!!.delete()
        }


    }



    private fun onUploadFailure(t: Throwable) {

        Log.e("Moviz-api-upload","api call failed  : "+t.toString())
    }

    private fun onUploadResponse(response: ServerResponse) {
        Log.e("Moviz-api-upload","response : ${response.status}" )

        if(! response.status.equals("400")){
                Log.e("user-image-path",response.status)
                UserSeason(this).setUserImage(response.status)
                startActivity(Intent(this,MainActivity::class.java))
                finish()

        }else{
            Toast.makeText(this,"an error has occurred !!",Toast.LENGTH_SHORT).show()
            mAuth.currentUser!!.delete()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode==1000 && resultCode== Activity.RESULT_OK && data != null){

             imageUri = data.data!!

            user_image.setImageURI(imageUri)


        }else{
            Log.e("image","there is a freaking error")
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Login::class.java))
        finish()
    }
}
