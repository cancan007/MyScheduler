package com.example.myscheduler

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

//import androidx.recyclerview.widget.RecyclerView
//import io.realm.RealmRecyclerViewAdapter

class ScheduleAdapter(data: OrderedRealmCollection<Schedule>) :
    RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(data, true){ // RealmRecyclerViewAdapter: 第一引数:RecyclerViewに表示するデータ, 第二引数: trueの場合、表示を自動更新
    // RealmRecyclerViewAdapter<RealmModel, RecyclerViewHolder>: RealmModelはRecyclerViewに表示したい項目、ViewHolderはセルに表示するビューを保持するためのもの
    init {
        setHasStableIds(true)
    }

    class ViewHolder(cell: View): RecyclerView.ViewHolder(cell){
        val date: TextView = cell.findViewById(android.R.id.text1)
        val title: TextView = cell.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ScheduleAdapter.ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2,
                                    parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleAdapter.ViewHolder,
                                  position: Int){
        val schedule: Schedule? = getItem(position)
        holder.date.text = DataFormat.format("yyyy/MM/dd HH:mm", schedule?.date)
        holder.title.text = schedule?.title
    }

    override fun getItemId(position: Int): Long{
        return getItem(position)?.id ?: 0
    }
}