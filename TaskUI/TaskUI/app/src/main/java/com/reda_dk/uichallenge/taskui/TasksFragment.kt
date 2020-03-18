package com.reda_dk.uichallenge.taskui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reda_dk.uichallenge.taskui.model.CalendarModel
import com.reda_dk.uichallenge.taskui.model.Task
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks.view.*
import kotlinx.android.synthetic.main.row_calendar.view.*
import kotlinx.android.synthetic.main.row_tasks.view.*

class TasksFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tasks, container, false)

        // making some tasks

        val task1 = Task("Sending the documents to the client","9:00",true)
        val task2 = Task("Send the app to the test","9:20",false)
        val task3 = Task("Visit the new shop","9:40",false)
        val task4 = Task("Play with the kids","10:00",false)
        val task5 = Task("Visit the new shop","9:40",false)
        val task6 = Task("Play with the kids","10:00",false)

        var list = ArrayList<Task>();list.add(task1);list.add(task2);list.add(task3);list.add(task4);list.add(task5);list.add(task6);

        var adapter = ListAdapter(list)
        v.listview.adapter = adapter


        // fake calendar list
        var listCalendar = ArrayList<CalendarModel>()
        listCalendar.add(CalendarModel(7,"Fri"));listCalendar.add(CalendarModel(8,"sat"));listCalendar.add(CalendarModel(8,"sat"));listCalendar.add(CalendarModel(8,"sat"));listCalendar.add(CalendarModel(8,"sat"));listCalendar.add(CalendarModel(8,"sat"));listCalendar.add(CalendarModel(8,"sat"))

        var recyclerAdapter = MyRecyclerAdapter(listCalendar)
        v.calendarRecycler.apply {
            layoutManager = LinearLayoutManager(activity!!.baseContext,LinearLayoutManager.HORIZONTAL,false)
            setAdapter(recyclerAdapter)

            addItemDecoration(DividerItemDecoration(activity!!.baseContext,DividerItemDecoration.HORIZONTAL))
        }


        return v
    }



    inner class MyRecyclerAdapter : RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

        var list =ArrayList<CalendarModel>()
        constructor(list:ArrayList<CalendarModel>){this.list = list}


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var layoutInflater = LayoutInflater.from(activity!!.applicationContext)
            var view = layoutInflater.inflate(R.layout.row_calendar,null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.num.text = list[position].num.toString()
            holder.day.text = list[position].day
            if(position==0){
                holder.itemView.container.setBackgroundResource(R.drawable.x_background_calendar)
            }
        }


        inner class ViewHolder : RecyclerView.ViewHolder{

            lateinit var num:TextView
            lateinit var day:TextView

            constructor(itemView:View) : super(itemView){
                num = itemView.num
                day = itemView.day
            }

        }
    }

    inner class ListAdapter : BaseAdapter {

       var list = ArrayList<Task>()

        constructor(list:ArrayList<Task>){
            this.list = list
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var layoutInflater = LayoutInflater.from(activity!!.applicationContext)
            var view = layoutInflater.inflate(R.layout.row_tasks,null)

            view.content.text = list[position].content
            view.time.text = list[position].time

            if(list[position].status){
                view.status.setBackgroundResource(R.drawable.row_checked_icon)
                view.content.setBackgroundResource(R.drawable.x_background_row_task_orange)
                view.content.setTextColor(activity!!.baseContext.resources.getColor(R.color.white))
            }else {
                view.status.setBackgroundResource(R.drawable.row_later_icon)
                view.content.setBackgroundResource(R.drawable.x_background_row_task_white)
                view.content.setTextColor(activity!!.baseContext.resources.getColor(R.color.hardGray))
            }


            var animation = AnimationUtils.loadAnimation(activity!!.baseContext,R.anim.global_anim)


            view.startAnimation(animation)
            return view

        }

        override fun getItem(position: Int): Any {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
           return position.toLong()
        }

        override fun getCount(): Int {
            return list.size
        }


    }





}
