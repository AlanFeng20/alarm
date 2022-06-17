package app.alanfeng.alarm

import app.alanfeng.alarm.rep.PlanRep
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object Manager {
    private val scope=MainScope()
    private inline fun withPlanId(crossinline callback:suspend (planId:String)->Unit){
        if(kv.containsKey(Keys.enabledPlan)){
            scope.launch {
                callback(kv.decodeString(Keys.enabledPlan)!!)
            }
        }
    }

    fun decidedNextAlarm(){
        withPlanId {
            val plan=PlanRep.getById(it)

        }
    }

}