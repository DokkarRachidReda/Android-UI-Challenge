package com.reda_dk.uichallenge.moviz

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.row_categories.view.*
import kotlinx.android.synthetic.main.row_moviz.view.*
import kotlinx.android.synthetic.main.row_types.view.*

class MainActivity : AppCompatActivity() {
    var myHolder:TypesRecyclerAdapter.ViewHolder? = null
    var firstType = true
    var searchVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        search_icon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                searchbar.visibility = View.VISIBLE
                val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.frombottom)
                searchbar.startAnimation(hyperspaceJump)
                searchVisible = true


            }
        })

      



        /////////////////////////////////////////////////////////
        var list = ArrayList<String>();list.add("In Theater");list.add("Box Office");list.add("In Theater");list.add("Box Office")
        var typesAdapter = TypesRecyclerAdapter(list)

        typeRecycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            setAdapter(typesAdapter)
        }

        ///////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////
        var list1 = ArrayList<String>();list1.add("Action");list1.add("Crime");list1.add("Comedy");list1.add("Drama");list1.add("Comedy");
        var catRecyclerAdapter = CategoriesRecyclerAdapter(list1)


        catRecycler.layoutManager =  LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
        catRecycler.setAdapter(catRecyclerAdapter)

        //////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////

        var mlist = ArrayList<Movie>();
        mlist.add(Movie("Avengers",8.2,R.drawable.i1)); mlist.add(Movie("Ford v Ferrari",8.8,R.drawable.i2))
        mlist.add(Movie("The Joker",7.2,R.drawable.i3)); mlist.add(Movie("Island of Skulls",9.2,R.drawable.i4))
        mlist.add(Movie("Avengers",8.2,R.drawable.i1)); mlist.add(Movie("Ford v Ferrari",8.8,R.drawable.i2))
        mlist.add(Movie("The Joker",7.2,R.drawable.i3)); mlist.add(Movie("Island of Skulls",9.2,R.drawable.i4))

        var movieAdapter = MovizRecyclerAdapter(mlist)

        movizRecycler.layoutManager =  LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
        movizRecycler.setAdapter(movieAdapter)

        ////////////////////////////////////////////////////////




    }



    inner class TypesRecyclerAdapter:RecyclerView.Adapter<TypesRecyclerAdapter.ViewHolder> {

        var list = ArrayList<String>()
        constructor(list:ArrayList<String>){this.list = list}

        inner class ViewHolder:RecyclerView.ViewHolder{

            var type: TextView
            var dash: LinearLayout


            constructor(itemView:View) : super(itemView){

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

                    myHolder!!.type.setTextColor(this@MainActivity.resources.getColor(R.color.gray))
                    myHolder!!.dash.visibility = View.GONE

                    holder.type.setTextColor(this@MainActivity.resources.getColor(R.color.black))
                    holder.dash.visibility = View.VISIBLE

                    val anim: Animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.lefttoright)
                    //holder.dash.startAnimation(anim)

                    myHolder = holder

                    if(searchVisible){
                        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fromtop)
                        searchbar.startAnimation(hyperspaceJump)
                        searchbar.visibility = View.GONE
                        searchVisible = false
                    }
                }
            })

        }
    }


    inner class CategoriesRecyclerAdapter:RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder>{

        var list = ArrayList<String>()
        constructor(list:ArrayList<String>){this.list = list}

        inner class ViewHolder:RecyclerView.ViewHolder{

            var category: TextView

            constructor(itemView:View) : super(itemView){

                category = itemView.cat


            }



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


                    if(searchVisible){
                        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fromtop)
                        searchbar.startAnimation(hyperspaceJump)
                        searchbar.visibility = View.GONE
                        searchVisible = false
                    }
                }
                

            })
        }
    }


    inner class MovizRecyclerAdapter:RecyclerView.Adapter<MovizRecyclerAdapter.ViewHolder>{

        var list = ArrayList<Movie>()
        constructor(list:ArrayList<Movie>){this.list = list}

        inner class ViewHolder:RecyclerView.ViewHolder{

            var img: ImageView
            var title: TextView
            var rating: TextView


            constructor(itemView:View) : super(itemView){

                img = itemView.img
                rating = itemView.rating
                title = itemView.title


            }



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
            holder.rating.text = list[position].rating.toString()
            holder.img.setImageResource(list[position].img)
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
                    var intent  = Intent(this@MainActivity,DetailsActivity::class.java)
                    intent.putExtra("img",list[position].img)
                    intent.putExtra("rating",list[position].rating)
                    intent.putExtra("title",list[position].title)

                    startActivity(intent)
                }
            })
            
            
            holder.itemView.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if(searchVisible){
                        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fromtop)
                        searchbar.startAnimation(hyperspaceJump)
                        searchbar.visibility = View.GONE
                        searchVisible = false
                    }


                    return true
                }
            })

        }
    }

}
