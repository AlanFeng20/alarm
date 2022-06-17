package app.alanfeng.alarm

object Manager {

    fun decidedNextAlarm(){
        withPlanId {

        }
    }

    inline fun withPlanId(callback:(planId:String)->Unit){
        if(kv.containsKey(Keys.enabledPlan)){
            callback(kv.decodeString(Keys.enabledPlan)!!)
        }
    }
}