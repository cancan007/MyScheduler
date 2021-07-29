package com.example.myscheduler

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.util.*

// ファイルで作ると、複数のクラスを作ることができる

class ConfirmDialog(private val message: String,   // ダイアログのタイトル
                     private val okLabel: String,   // OKボタンのラベル
                     private val okSelected: () -> Unit,   // OKボタンがタップされたときに実行する関数
                     private val cancelLabel: String,  // キャンセルボタンのラベルl;
                     private val cancelSelected: () -> Unit)   // キャンセルボタンがタップされたときに実行
    : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {   // ダイアログが生成されたときに呼び出される
        val builder = AlertDialog.Builder(requireActivity())   // ダイアログの内容を設定するためのインスタンス
        builder.setMessage(message)   // 指定した文字列をダイアログにい表示しています。
        builder.setPositiveButton(okLabel){dialog, which ->   //setPositiveButton: 一つ目のボタンを表示
            okSelected()
        }
        builder.setNegativeButton(cancelLabel){dialog, which ->  //setNegativeButton: 二つ目のボタンを表示
            cancelSelected()
        }
        return builder.create()  // ダイアログのオブジェクトを返す
    }
}

// 日付選択ダイアログ
class DateDialog(private val onSelected: (String) -> Unit)
    : DialogFragment(), DatePickerDialog.OnDateSetListener {  // DialogFragmentを継承のほかに、DatePickerDialog.OnDateSetListenerインターフェイスを実装して、onDateSetメソッドが選択入力(thisで選択できるようになる)できるようになる

    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
        // 現在の日付を初期値として設定している
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, date)   // インスタンスを生成し返している
    }

    override fun onDateSet(view: DatePicker?, year: Int,  //　DatePickerDialogで日時が選択されたら onDateSetメソッドが呼ばれる
                           month: Int, dayOfMonth: Int) {
        onSelected("$year/${month + 1}/$dayOfMonth")   // onSelected: 受け取った処理を実行
    }
}

// 時間選択ダイアログ
class TimeDialog(private val onSelected: (String) -> Unit)
    : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(context, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int){  // TimePickerDialogで時間が設定されたら、呼び出される
        onSelected("%1$02d:%2$02d".format(hourOfDay, minute))
    }
}

