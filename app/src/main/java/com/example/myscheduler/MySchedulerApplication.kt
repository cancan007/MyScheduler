package com.example.myscheduler

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

//  アプリケーション実行時に処理を行うためのクラス
class MySchedulerApplication : Application(){   //Applicationクラスを継承
    override fun onCreate(){
        super.onCreate()   // super: 親クラスで「色々と登録」していたとすると、superを呼び出せば「色々と登録」出来ますし、呼ばなければ「色々と登録」を飛ばす事が出来ます。
        Realm.init(this)  // Realmを初期化し、すぐに使用できるデフォルト構成を作成
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true).build()  // allowWritesOnUiThread: UIスレッドでのデータベース書き込みを許可するかを設定
        Realm.setDefaultConfiguration(config)
    }
}