package com.example.myscheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myscheduler.databinding.FragmentSecondBinding
import io.realm.Realm
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener{
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        //binding.buttonSecond.setOnClickListener {
            //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)
        binding.list.layoutManager = LinearLayoutManager(context)
        var dateTime = Calendar.getInstance().apply {
            timeInMillis = binding.calendarView.date   // 現在カレンダーで選択中の日付を取得
        }
        findSchedule( // findSchedule: カレンダーで選択されている日付の助ぢゅーるだけを取得し、RecyclerViewに表示
            dateTime.get(Calendar.YEAR),
            dateTime.get(Calendar.MONTH),
            dateTime.get(Calendar.DAY_OF_MONTH)
        )
        binding.calendarView
            .setOnDateChangeListener { view, year, month, dayOfMonth ->  //setOnDateChangeListener: カレンダーで選択中の日付が変更されたときに実行
                findSchedule(year, month, dayOfMonth)
            }
    }

    private fun findSchedule(
        year: Int,
        month: Int,
        dayOfMonth: Int
    ){
        // 選択された日付より時刻を切り捨てたデータを作成
        var selectDate = Calendar.getInsatance().apply{
            clear()
            set(year, month, dayOfMonth)
        }
        // 00:00:00~23:59:59.999までを選択する
        val schedules = realm.where<Schedule>()
            .between(  // between: 上限値と下限値を指定してデータを作成
                "date",
                selectDate.time,
                selectDate.apply{
                    add(Calendar.DAY_OF_MONTH, 1)
                    add(Calendar.MILLISECOND, -1)
                }.time
            ).findAll().sort("date")  // sortでデータを日付の昇順で並べ替える
        val adapter = ScheduleAdapter(schedules)
        binding.list.adapter = adapter   // クエリ結果はRecyclerViewに設定

        // SecondFragmentからScheduleEditFragmentに画面遷移する設定
        adapter.setOnItemClickListener{id ->
            id?.let{
                val action = SecondFragmentDirections
                    .actionToScheduleEditFragment(it)
                findNavController().navigate(action)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){
        super.onDestroy()
        realm.close()   // データベースを閉じる
    }
}