package com.example.myscheduler

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// データを格納するモデルを作成するためのクラス
open class Schedule : RealmObject(){  // このクラスを他で継承できるようにopenを記述
    @PrimaryKey   // idを一意にして、idで指定できるようにするため
    var id: Long = 0   // スケジュール一つ一つを連番で管理するためのプロパティ
    var date: Date = Date()   // 日付
    var title: String = ""
    var detail: String = ""
}