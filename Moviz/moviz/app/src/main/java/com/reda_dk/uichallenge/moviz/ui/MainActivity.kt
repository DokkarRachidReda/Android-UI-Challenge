package com.reda_dk.uichallenge.moviz.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.RecyclerView
import com.reda_dk.uichallenge.moviz.R
import com.reda_dk.uichallenge.moviz.data.model.User
import com.reda_dk.uichallenge.moviz.data.model.genres.Genres
import com.reda_dk.uichallenge.moviz.data.model.genres.SingleGenre
import com.reda_dk.uichallenge.moviz.data.model.movies.Movies
import com.reda_dk.uichallenge.moviz.data.model.movies.SingleMovie
import com.reda_dk.uichallenge.moviz.data.api.MovizApiEndPoints
import com.reda_dk.uichallenge.moviz.data.api.TmbdEndPoints
import com.reda_dk.uichallenge.moviz.season.UserSeason
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.row_categories.view.*
import kotlinx.android.synthetic.main.row_moviz.view.*
import kotlinx.android.synthetic.main.row_types.view.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

     val baseUrl = "https://api.themoviedb.org/3/"
     val movizBaseUrl = "http://192.168.1.3:3000/"
     val api_key = "2e27645e1938878aee2b80d8a00e81a1"
     val imgBaseUrl = "https://image.tmdb.org/t/p/w500"
     var myHolder: TypesRecyclerAdapter.ViewHolder? = null
     var firstType = true
     var searchVisible = false

    val compositeDisposable = CompositeDisposable()

    var user:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user = UserSeason(this).getUserSeason()

        if( ! user!!.img.equals("")){
            Log.e("user-img-path",movizBaseUrl+user!!.img)
            Picasso.get().load(movizBaseUrl+user!!.img).into(user_img)
        }



        search_icon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                searchbar.visibility = View.VISIBLE
                val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity,
                    R.anim.frombottom
                )
                searchbar.startAnimation(hyperspaceJump)
                searchVisible = true


            }
        })
        /////////////////////////////////////////////////////////
        val list = ArrayList<String>();list.add("In Theater");list.add("Box Office");list.add("In Theater");list.add("Box Office")
        val typesAdapter = TypesRecyclerAdapter(list)

        typeRecycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            setAdapter(typesAdapter)
        }

        ////////////////////////////////////////////////////////


        

    }





    inner class TypesRecyclerAdapter(private var list: ArrayList<String>) :
        RecyclerView.Adapter<TypesRecyclerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var type: TextView
            var dash: LinearLayout


            init {
                type = itemView.type
                dash = itemView.dash
            }



        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var layoutInflater = LayoutInflater.from(this@MainActivity)
            var view = layoutInflater.inflate(R.layout.row_types,null)

            return ViewHolder(view)

        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.type.text = list[position]

            if(firstType && position == 0){
                holder.type.setTextColor(this@MainActivity.resources.getColor(R.color.black))
                holder.dash.visibility = View.VISIBLE
                myHolder = holder
            }

            holder.type.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    myHolder!!.type.setTextColor(this@MainActivity.resources.getColor(
                        R.color.gray
                    ))
                    myHolder!!.dash.visibility = View.GONE

                    holder.type.setTextColor(this@MainActivity.resources.getColor(R.color.black))
                    holder.dash.visibility = View.VISIBLE

                    val anim: Animation = AnimationUtils.loadAnimation(this@MainActivity,
                        R.anim.lefttoright
                    )
                    //holder.dash.startAnimation(anim)

                    myHolder = holder

                    if(searchVisible){
                        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity,
                            R.anim.fromtop
                        )
                        searchbar.startAnimation(hyperspaceJump)
                        searchbar.visibility = View.GONE
                        searchVisible = false
                    }
                }
            })

        }
    }


    inner class GenresRecyclerAdapter(private var list: ArrayList<SingleGenre>) :
        RecyclerView.Adapter<GenresRecyclerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val category: TextView = itemView.cat

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var layoutInflater = LayoutInflater.from(this@MainActivity)
            var view = layoutInflater.inflate(R.layout.row_categories,null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.category.text = list[position].name
            holder.category.tag = "no" // not clicked yet

            holder.category.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    if( holder.category.tag.equals("no")){
                        holder.category.tag = "yes" // clicked
                        holder.category.setTextColor(resources.getColor(R.color.colorPrimary))
                        holder.category.setBackgroundResource(R.drawable.x_rounded_25dp_pinko)
                    }else {
                        holder.category.tag = "no" // clicked
                        holder.category.setTextColor(resources.getColor(R.color.black))
                        holder.category.setBackgroundResource(R.drawable.x_rounded_25dp_stroke_gray)
                    }


                    if(searchVisible){
                        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity,
                            R.anim.fromtop
                        )
                        searchbar.startAnimation(hyperspaceJump)
                        searchbar.visibility = View.GONE
                        searchVisible = false
                    }
                }
                

            })
        }


        fun setResults(list: ArrayList<SingleGenre>){
            this.list = list
            notifyDataSetChanged()
        }

    }


    inner class MovizRecyclerAdapter(var list: ArrayList<SingleMovie>) :
        RecyclerView.Adapter<MovizRecyclerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val img: ImageView = itemView.img
            val title: TextView = itemView.title
            val rating: TextView = itemView.rating


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(this@MainActivity)
            val view = layoutInflater.inflate(R.layout.row_moviz,null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
           return list.size
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {


            holder.title.text = list[position].title
            holder.rating.text = list[position].vote_average.toString()
            Picasso.get().load(imgBaseUrl+list[position].poster_path).into(holder.img)

            holder.img.clipToOutline = true

            val animationY = ObjectAnimator.ofFloat( holder.img, "rotationY", 15f, 0f)
            val animationX = ObjectAnimator.ofFloat( holder.img, "rotationX", 10f, 0f)
            animationY.setDuration(500)
            animationX.setDuration(500)
            animationY.setInterpolator( AccelerateDecelerateInterpolator())
            animationX.setInterpolator( AccelerateDecelerateInterpolator())
            animationY.start()
            animationX.start()


            holder.itemView.img.setOnClickListener(object :View.OnClickListener{
                override fun onClick(v: View?) {
                    var i = Intent(this@MainActivity,
                        DetailsActivity::class.java)
                    i.putExtra("movie",list[position])

                    this@MainActivity.startActivity(i)
                    finish()
                }
            })
            
            
            holder.itemView.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if(searchVisible){
                        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity,
                            R.anim.fromtop
                        )
                        searchbar.startAnimation(hyperspaceJump)
                        searchbar.visibility = View.GONE
                        searchVisible = false
                    }


                    return true
                }
            })

        }


        fun setResults(list: ArrayList<SingleMovie>){
            this.list = list
            notifyDataSetChanged()
        }

    }




}
