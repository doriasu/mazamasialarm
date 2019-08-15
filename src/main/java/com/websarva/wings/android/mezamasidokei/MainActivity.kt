package com.websarva.wings.android.mezamasidokei

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.app.PendingIntent
import android.content.Context
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.util.Log
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.Button
import android.widget.TimePicker
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun jikkouonclick(view : View){
        var calendar = jikokushutoku()


        val alarmTime=calendar.timeInMillis
        //val alarmTime = System.currentTimeMillis() + 5000

        //Toast.makeText(this,"${alarmTime}",Toast.LENGTH_LONG).show()
        //Intent作成
        val alarmIntent= Intent(this,AlarmReceiver::class.java)

        val pendingintent=PendingIntent.getBroadcast(this,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        //alarmmanagerで指定時間後に処理を設定
        val manager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.set(AlarmManager.RTC_WAKEUP,alarmTime,pendingintent)
        manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmTime,pendingintent)

    }

    fun jikokushutoku() :Calendar{
        val settime=findViewById<TimePicker>(R.id.time_picker)
        val hour=settime.getHour()

        val minute=settime.getMinute()
        val today=getToday()
        //calendarにのせる
        var calendar=Calendar.getInstance()
        calendar.set(today.substring(0,4).toInt(),today.substring(4,6).toInt(),today.substring(6,8).toInt(),hour,minute)
        return calendar




    }

    fun getToday(): String {
        val date = Date()
        val format = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return format.format(date)
    }

}
class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"処理の実行",Toast.LENGTH_LONG).show()
        Log.i("LifeCycleSample","this is called")

    }
}

