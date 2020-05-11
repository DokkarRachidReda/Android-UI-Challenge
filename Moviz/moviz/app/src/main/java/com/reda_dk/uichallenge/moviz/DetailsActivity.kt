package com.reda_dk.uichallenge.moviz

import android.content.Intent
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
import com.reda_dk.uichallenge.moviz.model.castcrew.CastCrew
import com.reda_dk.uichallenge.moviz.model.castcrew.MovieWorker
import com.reda_dk.uichallenge.moviz.model.movies.SingleMovie
import com.reda_dk.uichallenge.moviz.requestInterface.TmbdEndPoints
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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


        /////////////////////Movie Details///////////////////////////////////


        var movie = intent.getSerializableExtra("movie") as SingleMovie

        Log.e("movie-details","id : "+movie.id)

        movie_title.text = if(movie.title != "") movie.title else "NA"
        movie_overview.text = if(movie.overview != "") movie.overview else "NA"
        movie_releaseyear.text = if(movie.release_date.subSequence(0,4).trim() != "") movie.release_date.subSequence(0,4).trim() else "NA"
        movie_rating.text = if(movie.vote_average.toString() != "") movie.vote_average.toString()else "NA"

        Picasso.get().load(imgBaseUrl+movie.poster_path).into(movie_img)




        ////////////////////////////////Genres /////////////////////////////////////////////



        var list1 = ArrayList<String>();list1.add("Action");list1.add("Crime");list1.add("Comedy");list1.add("Drama");
        var catRecyclerAdapter = CategoriesRecyclerAdapter(list1)


        catRecycler.layoutManager =  LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        catRecycler.setAdapter(catRecyclerAdapter)



        ////////////////////////Crew Cast//////////////////////////
        compositeDisposable.add(
            apiservices.getCastCrew(movie.id.toString(),api_key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                . subscribe(this::onCastCrewResponse, this::onCastCrewFailure)
        )
    }



    private fun onCastCrewFailure(t: Throwable) {

        Log.e("Tmdb-api-movies","api call failed  : "+t.toString())
    }

    private fun onCastCrewResponse(response: CastCrew){
        Log.e("Tmdb-api-cast-succeed","cast size : "+response.cast.size+"  crew size : "+response.crew.size)

        var list = ArrayList<MovieWorker>()
        var i = 0
        for ( cast in response.cast){

            if(cast.profile_path != null) {
                list.add(
                    MovieWorker(
                        cast.name,cast.character,cast.profile_path
                    ))

                i++
            }

            if (i>20) break
        }

        for ( crew in response.crew){

            if(crew.profile_path != null){
                list.add(MovieWorker(
                    crew.name,crew.job,crew.profile_path
                ))
                i++
            }


            if (i>20) break
        }


        Log.e("crew-recycler","size : "+list.size)
        val crewAdapter = CastCrewRecyclerAdapter(list)

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

        var list = ArrayList<MovieWorker>()
        constructor(list:ArrayList<MovieWorker>){this.list = list}

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

            Picasso.get().load(imgBaseUrl+list[position].img).into(holder.img)

            /*if(list[position].img != null) {
                Picasso.get().load(list[position].img).into(holder.img)
                Log.e("cast-crew-img", "image path  on posittion $position is null")
            }*/

        }
    }



    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this,MainActivity::class.java))
    }

}
