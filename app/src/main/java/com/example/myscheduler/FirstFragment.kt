package com.example.myscheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myscheduler.databinding.FragmentFirstBinding
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var realm: Realm  // Realmクラスのプロパティを用意

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()   // getDefaultInstance: Realmクラスのインスタンスを取得
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.list.layoutManager = LinearLayoutManager(context)   // LinearLayoutManager:項目を直列に並べる, RecyclerViewのレイアウトマネジャーとして登録
        val schedules = realm.where<Schedule>().findAll()  // すべてのスケジュールを取得し変数に格納
        val adapter = ScheduleAdapter(schedules)  // ScheduleAdapterのインスタンスを生成
        binding.list.adapter = adapter   // RecyclerViewに設定

        adapter.setOnItemClickListener { id ->   // RecyclerViewの項目がタップされたとき実行
            id?.let{
                val action =
                    FirstFragmentDirections.actionToScheduleEditFragment(it)  // 画面をFirstFragmentからScheduleEditFragmentへ遷移させると同時に、ScheduleのIdを渡す(NavDirectionsインターフェイスオブジェクトを返す)
                findNavController().navigate(action)  // navigate: NavDirectionsを指定して画面遷移する
            }
        }
        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)  // 非表示にしていたfabボタンを表示する
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){  //アクティビティの終了処理
        super.onDestroy()
        realm.close()  // Realmのインスタンスを破棄して、リソースを開放
    }
}