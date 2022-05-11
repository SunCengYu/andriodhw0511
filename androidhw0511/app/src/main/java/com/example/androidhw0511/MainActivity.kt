package com.example.androidhw0511

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    //計數器狀態
    private lateinit var btn_start: Button;
    private lateinit var tv_clock: TextView;
    private var flag = false
    //建立BroadcastReceiver物件
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        //在onReceive()中加入接收廣播後要執行的動作
        override fun onReceive(context: Context, intent: Intent) {
            //解析Intent取得計時資訊
            intent.extras ?.let {
                tv_clock?.text = "%02d:%02d:%02d".format(it.getInt("H"), it.getInt("M"), it.getInt("S"))

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_clock = findViewById(R.id.tv_clock)
        btn_start = findViewById(R.id.btn_start)
        //註冊Receiver來接收有『MyMessage』識別字串的廣播
        registerReceiver(receiver, IntentFilter("MyMessage"))
        //取得Service狀態
        flag = MyService.flag
        btn_start.text = if(flag) "暫停" else "開始"

        btn_start.setOnClickListener {
            flag = !flag
            btn_start.text = if (flag) "暫停" else "開始"
            //啟動Service
            startService(Intent(this, MyService::class.java).putExtra("flag", flag))
            Toast.makeText(this, if(flag)"計時開始" else "計時暫停", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //註銷Receiver
        unregisterReceiver(receiver)
    }
}