package com.example.myscheduler

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myscheduler.databinding.FragmentScheduleEditBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ScheduleEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentScheduleEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()   // realmのインスタンスを生成
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val args: ScheduleEditFragmentArgs by navArgs()  // 引数を取得

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        if(args.scheduleId != -1L){   // 更新時に実行
            val schedule = realm.where<Schedule>()
                .equalTo("id", args.scheduleId).findFirst()    // args.scheduleIdと同じidのレコードを取得
            binding.dateEdit.setText(
                DateFormat.format("yyyy/MM/dd",
                                                        schedule?.date))
            binding.titleEdit.setText(schedule?.title)
            binding.detailEdit.setText(schedule?.detail)
        }
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)  // MainActivityのsetFabVisibleを用いて、fabボタンを非表示にしています
        binding.save.setOnClickListener{saveSchedule(it)}   // 保存ボタンを押したときにsaveScheduleを実行
    }

    private fun saveSchedule(view: View){  //データベースへの保存処理
        when (args.scheduleId){
            -1L -> {    // 新規
                realm.executeTransaction { db: Realm ->  // executeTransaction:データベースの書き込み時にはトランザクションする必要があり、それの開始、終了、キャンセルを自動で行う
                    val maxId = db.where<Schedule>().max("id")   // Scheduleのidフィールドの最大値を取得
                    val nextId = (maxId?.toLong()
                        ?: 0L) + 1L   // idの最大値+1を、次に新規登録するモデルのidとする  maxIdがnullの時0を取得しそれに+1
                    val schedule =
                        db.createObject<Schedule>(nextId)  // createObject:モデルクラスのデータを生成  データを1行追加するが、この時1増やした後のIDを持つScheduleクラスのインスタンスを受け取るので、各フィールドに値を設定するとデータの追加が完了
                    val date =
                        "${binding.dateEdit.text} ${binding.timeEdit.text}".toDate()   // 下で拡張関数.toDateを定義しているので、それを使う
                    // scheduleオブジェクトに値を設定, これによりscheduleオブジェクトがデータベースに書き込まれる
                    if (date != null) schedule.date = date
                    schedule.title = binding.titleEdit.text.toString()
                    schedule.detail = binding.detailEdit.text.toString()
                }
                // Snackbar: 一定期間メッセージを表示する機能で、アクションも設定できる
                Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)   // make: スナックバーを作成
                    .setAction("戻る") { findNavController().popBackStack() }  // ボタンを押すとひとつ前の画面に戻る
                    .setActionTextColor(Color.YELLOW)  //文字列の色を設定
                    .show()   // 作成したスナックバーを表示
            }
            else -> {  // 更新
                realm.executeTransaction{db: Realm ->
                    val schedule = db.where<Schedule>()
                        .equalTo("id", args.scheduleId).findFirst()
                    val date = ("${binding.dateEdit.text} " +
                            "${binding.timeEdit.text}").toDate()
                    if(date != null) schedule?.date = date
                    schedule?.title = binding.titleEdit.text.toString()
                    schedule?.detail = binding.detailEdit.text.toString()
                }
                Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)   // スナックバーに更新時のメッセージを表示
                    .setAction("戻る"){findNavController().popBackStack()}
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){
        super.onDestroy()
        realm.close()  // データベースを閉じる
    }

    private fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date?{  // toDateという拡張関数を定義
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch(e: IllegalArgumentException){  // catch: 予想される例外時に実行
            return null
        } catch(e: ParseException){
            return null
        }
    }
}