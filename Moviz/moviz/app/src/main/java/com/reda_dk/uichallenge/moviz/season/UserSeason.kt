package com.reda_dk.uichallenge.moviz.season

import android.content.Context
import android.content.SharedPreferences
import com.reda_dk.uichallenge.moviz.model.User

class UserSeason (var context:Context){


    fun createUserSeason(user: User){
        val pref = context.getSharedPreferences("user_data",0)
        val editor = pref.edit()

        editor.putString("user_id",user.id)
        editor.putString("user_email",user.email)
        editor.putString("user_name",user.username)
        editor.putString("user_img",user.img)

        editor.apply()
    }


    fun getUserSeason():User{
        val pref = context.getSharedPreferences("user_data",0)
        val user = User(
            pref.getString("user_id","")!! ,
            pref.getString("user_email","")!! ,
            pref.getString("user_name","") !!,
            pref.getString("user_img","") !!
        )

        return user
    }

}