package com.reda_dk.uichallenge.taskui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var actualFragment = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var mFragment = TasksFragment()
        supportFragmentManager.beginTransaction().replace(R.id.host,mFragment).commit()
        tasktxt.visibility = View.VISIBLE
        tasksid.visibility = View.GONE

        
        home.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                bottomNavigation(1,actualFragment)
                actualFragment = 1
            }

        })


        task.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                bottomNavigation(2,actualFragment)
                actualFragment = 2
            }

        })
    }


    fun bottomNavigation(actual:Int,previus:Int){

        when(previus){
            2->{
                tasktxt.visibility = View.GONE
                tasksid.visibility = View.VISIBLE
            }

            1->{
                hometxt.visibility = View.GONE
                homeid.visibility = View.VISIBLE
            }


        }


        when(actual){

            2->{
                var mFragment = TasksFragment()
                supportFragmentManager.beginTransaction().replace(R.id.host,mFragment).commit()
                tasktxt.visibility = View.VISIBLE
                tasksid.visibility = View.GONE
            }

            1->{
                var mFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.host,mFragment).commit()
                hometxt.visibility = View.VISIBLE
                homeid.visibility = View.GONE
            }

        }

    }
}
