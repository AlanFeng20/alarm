package app.alanfeng.alarm

import android.app.Application
import android.content.Context
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import com.tencent.mmkv.MMKV

class App:Application() {
    companion object{
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
        MMKV.initialize(this)
    }


    private fun logInit() {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
        Log.setLogImp(Xlog())
        Log.setConsoleLogOpen(BuildConfig.DEBUG);
        Log.appenderOpen(
            if (BuildConfig.DEBUG) Xlog.LEVEL_DEBUG else Xlog.LEVEL_INFO,
            Xlog.AppednerModeAsync,
            cacheDir.absolutePath + "/xlog",
            filesDir.absolutePath + "/xlog",
            "log",
            7
        );
        Log.e("", "")
    }

    fun closeLog() {
        Log.appenderClose()
    }
}