package app.alanfeng.alarm

import com.tencent.mmkv.MMKV

val kv = MMKV.defaultMMKV()

object Keys {
    const val enabledPlan = "enabledPlan"
}