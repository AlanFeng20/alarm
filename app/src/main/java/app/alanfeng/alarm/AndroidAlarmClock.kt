package app.alanfeng.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context

object AndroidAlarmClock {
    fun checkPermission() {

    }

    val alarmManager: AlarmManager by lazy { App.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun set(time:Long){
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,)
    }
    fun cancel(intent: PendingIntent){
        alarmManager.cancel(intent)
    }
}