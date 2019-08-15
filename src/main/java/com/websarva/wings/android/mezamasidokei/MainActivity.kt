package com.websarva.wings.android.mezamasidokei
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.app.PendingIntent
import android.content.Context
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.*
import java.io.IOException
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
public var _player: MediaPlayer?=null
public var adapter: ArrayAdapter<String>?=null
public var count=0
class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        adapter=ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ListViewオブジェクトをイジイジ
        val listview=findViewById<ListView>(R.id.list_view)
        listview.adapter=adapter


        //メディアプレーヤーフィールド
        _player= MediaPlayer()
        //音声ファイルのURI文字列の生成
        val mediaFileUriStr="android.resource://${packageName}/${R.raw.butterfly}"
        val mediaFileUri= Uri.parse(mediaFileUriStr)
        try{
            //メディアプレーヤーに音声ファイルを設定
            _player?.setDataSource(applicationContext,mediaFileUri)
            //非同期でのメディア再生準備が完了したリスナを設定
           // _player?.setOnPreparedListener(PlayerPreparedListener())
            //メディア再生が終了したときのリスナの設定
            //_player?.setOnCompletionListener(PlayerCompletionListener())
            //非同期でメディア再生を準備
            _player?.prepareAsync()
        }
        catch(ex: IllegalArgumentException){
            Log.e("MediaSample","メディアプレーヤー準備時の例外発生",ex)
        }
        catch(ex: IOException){
            Log.e("MediaSample","メディアプレーヤー準備時の例外発生",ex)

        }


    }
    fun onSaisei(view:View){
        _player?.let{
            Log.i("LifeCycleSample","mediaplayer")
            it.start()
        }


    }
    //listviewのクリックに関する処理を記述
    fun listclick(view :View){

    }
    fun jikkouonclick(view : View){
        var calendar = jikokushutoku()

        val alarmTime=calendar.timeInMillis
        //val alarmTime = System.currentTimeMillis() + 5000

        //Toast.makeText(this,"${alarmTime}",Toast.LENGTH_LONG).show()
        //Intent作成
        val alarmIntent= Intent(this,AlarmReceiver::class.java)

        var moji=calendar.get(Calendar.HOUR).toString()+"時"+calendar.get(Calendar.MINUTE).toString()+"分"
        adapter?.add(moji)
        //以下時間指定操作


        val pendingintent=PendingIntent.getBroadcast(this,count,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        count++

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
        //javaの月は0indexに注意
        calendar.set(today.substring(0,4).toInt(),today.substring(5,6).toInt()-1,today.substring(6,8).toInt(),hour,minute,0)

        //log
        Log.i("LifeCycleSample",today.substring(0,4))
        Log.i("LifeCycleSample",(today.substring(4,6).toInt()-1).toString())
        Log.i("LifeCycleSample",today.substring(6,8))
        Log.i("LifeCycleSample",hour.toString())
        Log.i("LifeCycleSample",minute.toString())


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
        Toast.makeText(context,"おきてー",Toast.LENGTH_LONG).show()



        _player?.let {
            if(it.isPlaying){
                it.stop()
            }
            //it.start()
        }
        _player=MediaPlayer.create(context,R.raw.butterfly)
        _player?.let{
            it.start()
        }
        Log.i("LifeCycleSample","this is called")

    }

}

