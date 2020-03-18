package com.reda_dk.uichallenge.taskui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.row_home_tasks.view.*


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        var recyclerAdapter = ProjectsRecyclerAdapter()
        view.projectsRecycler.apply {
            layoutManager = LinearLayoutManager(activity!!.baseContext,
                LinearLayoutManager.HORIZONTAL,false)
               setAdapter(recyclerAdapter)
        }

        return view


    }


    inner class ProjectsRecyclerAdapter:RecyclerView.Adapter<ProjectsRecyclerAdapter.ViewHolder> (){


        inner class ViewHolder:RecyclerView.ViewHolder{

             var num: TextView
             var title: TextView

            constructor(itemView:View) : super(itemView){

                num = itemView.numTasks
                title = itemView.title
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var layoutInflater = LayoutInflater.from(activity!!.applicationContext)
            var view = layoutInflater.inflate(R.layout.row_home_tasks,null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
           return 4
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        }


    }


}
