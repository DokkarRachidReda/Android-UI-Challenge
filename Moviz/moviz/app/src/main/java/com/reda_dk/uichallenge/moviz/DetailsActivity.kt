package com.reda_dk.uichallenge.moviz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reda_dk.uichallenge.moviz.model.CastCrew
import com.reda_dk.uichallenge.moviz.model.movies.SingleMovie
import com.reda_dk.uichallenge.moviz.requestInterface.TmbdEndPoints
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.row_categories.view.*
import kotlinx.android.synthetic.main.row_crew.view.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DetailsActivity : AppCompatActivity() {
    final var baseUrl = "https://api.themoviedb.org/3/"
    final var api_key = "2e27645e1938878aee2b80d8a00e81a1"
    final var imgBaseUrl = "https://image.tmdb.org/t/p/w500"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)



        ////////////////Retrofit conf////////////////////////////

        val client = OkHttpClient
            .Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        val apiservices = retrofit.create(TmbdEndPoints::class.java)
        val compositeDisposable = CompositeDisposable()


        ////////////////////////////////////////////////////////


        var movie = intent.getSerializableExtra("movie") as SingleMovie
        Log.e("movie",movie.title)



        var list1 = ArrayList<String>();list1.add("Action");list1.add("Crime");list1.add("Comedy");list1.add("Drama");
        var catRecyclerAdapter = CategoriesRecyclerAdapter(list1)


        catRecycler.layoutManager =  LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        catRecycler.setAdapter(catRecyclerAdapter)



        //////////////////////////////////////////////////
        var mlist = ArrayList<CastCrew>();mlist.add(
            CastCrew(
                "James Mangold",
                "Director",
                R.drawable.circle1
            )
        );mlist.add(
            CastCrew(
                "Matt Damon",
                "Carroll",
                R.drawable.circle2
            )
        )

        mlist.add(
            CastCrew(
                "Christian Bale",
                "Ken Miles",
                R.drawable.circle3
            )
        );mlist.add(
            CastCrew(
                "James Mangold",
                "Director",
                R.drawable.circle1
            )
        );mlist.add(
            CastCrew(
                "Matt Damon",
                "Carroll",
                R.drawable.circle2
            )
        )

        var crewAdapter = CastCrewRecyclerAdapter(mlist)

        crewRecycler.layoutManager =  LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        crewRecycler.setAdapter(crewAdapter)


    }



    inner class CategoriesRecyclerAdapter: RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder>{

        var list = ArrayList<String>()
        constructor(list:ArrayList<String>){this.list = list}

        inner class ViewHolder: RecyclerView.ViewHolder{

            var category: TextView

            constructor(itemView: View) : super(itemView){

                category = itemView.cat


            }



        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var layoutInflater = LayoutInflater.from(this@DetailsActivity)
            var view = layoutInflater.inflate(R.layout.row_categories,null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.category.text = list[position]
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

                }


            })
        }
    }


    inner class CastCrewRecyclerAdapter: RecyclerView.Adapter<CastCrewRecyclerAdapter.ViewHolder>{

        var list = ArrayList<CastCrew>()
        constructor(list:ArrayList<CastCrew>){this.list = list}

        inner class ViewHolder: RecyclerView.ViewHolder{

            var name: TextView
            var function: TextView
            var img: ImageView

            constructor(itemView: View) : super(itemView){

                name = itemView.name
                function = itemView.function
                img = itemView.img


            }



        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var layoutInflater = LayoutInflater.from(this@DetailsActivity)
            var view = layoutInflater.inflate(R.layout.row_crew,null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = list[position].name
            holder.function.text = list[position].function
            holder.img.setImageResource(list[position].img)

        }
    }



}
