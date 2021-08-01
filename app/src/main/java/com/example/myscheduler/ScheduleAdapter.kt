package com.example.myscheduler

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

//import androidx.recyclerview.widget.RecyclerView
//import io.realm.RealmRecyclerViewAdapter

class ScheduleAdapter(data: OrderedRealmCollection<Schedule>):
    RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(data, true){ // RealmRecyclerViewAdapter: 第一引数:RecyclerViewに表示するデータ, 第二引数: trueの場合、表示を自動更新
    // RealmRecyclerViewAdapter<RealmModel, RecyclerViewHolder>: RealmModelはRecyclerViewに表示したい項目、ViewHolderはセルに表示するビューを保持するためのもの

    private var listener: ((Long?) -> Unit)? = null   // Unit: 戻り値がないことを示している  ScheduleのIdを受け取るためにLong型、 後で値を設定したいのでNull許容型である?  (listenerは関数)

    fun setOnItemClickListener(Listener:(Long?) -> Unit){  // 関数型の変数listenerに登録するためのメソッド, つまりこの関数は引数に関数を受け取る
        this.listener = listener
    }

    init {
        setHasStableIds(true)  // データ内の1つの項目を指定する固有IDを使うことを示している(画面全体ではなく更新のあったセルのみを変更するため)
    }

    class ViewHolder(cell: View): RecyclerView.ViewHolder(cell){   // セルに使用するビューを保持するためのもの
        val date: TextView = cell.findViewById(android.R.id.text1)
        val title: TextView = cell.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):   // テーブルのセルを生成(セルが必要になるたんびに呼ばれる)
            ScheduleAdapter.ViewHolder{
        val inflater = LayoutInflater.from(parent.context)  // LayoutInflater: 画面作成に使うクラス  fromでインスタンス化
        val view = inflater.inflate(android.R.layout.simple_list_item_2,   // inflate: 画面を生成
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleAdapter.ViewHolder,    // テーブルのセルに値を生成(指定した位置にデータを呼ぶとき実行)
                                  position: Int){
        val schedule: Schedule? = getItem(position)   // getItem: データベース内の指定された位置にある項目を取得  この場合戻り値はSchedule
        holder.date.text = DateFormat.format("yyyy/MM/dd HH:mm", schedule?.date)   // テキストビューに値をセット(Date型を文字列に変換している)
        holder.title.text = schedule?.title
        holder.itemView.setOnClickListener{   // セルに使用されているビューがタップされたときに実行
            listener?.invoke(schedule?.id)   // invoke: 関数型の変数を実行する
        }
    }

    override fun getItemId(position: Int): Long{  // データ項目のIDを返す(position: 参照するアダプターの位置)
        return getItem(position)?.id ?: 0
    }
}

