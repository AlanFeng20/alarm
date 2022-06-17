package app.alanfeng.alarm.rep

import app.alanfeng.alarm.database.Alarm
import app.alanfeng.alarm.database.AlarmRecord
import app.alanfeng.alarm.database.Plan
import app.alanfeng.alarm.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PlanRep {
    suspend fun getAll() = withContext(Dispatchers.IO) {
        return@withContext db.planQueries.selectAll()
    }

    suspend fun planNameExits(name: String) = withContext(Dispatchers.IO) {
        return@withContext db.planQueries.selectNames().executeAsList().contains(name)
    }

    suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        return@withContext db.planQueries.deleteById(id)
    }

    suspend fun insertOrUpdate(plan: Plan) = withContext(Dispatchers.IO) {
        db.planQueries.insertOrUpdatePlan(plan)
    }
}

object AlarmRep {

    suspend fun getByPlanId(planId: String) = withContext(Dispatchers.IO) {
        return@withContext db.alarmQueries.selectByPlanId(planId).executeAsList()
    }

    suspend fun insertOrUpdate(alarm: Alarm) = withContext(Dispatchers.IO) {
        db.alarmQueries.insertOrUpdateAlarm(alarm)
    }

    suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        db.alarmQueries.deleteById(id)
    }

}

object AlarmRecordRep {

    suspend fun getByAlarmId(alarmId: String) = withContext(Dispatchers.IO) {
        return@withContext db.alarmRecordQueries.selectByAlarmId(alarmId).executeAsList()
    }

    suspend fun getAfterTime(time: Long) = withContext(Dispatchers.IO) {
        return@withContext db.alarmRecordQueries.selectAfterTime(time).executeAsList()
    }

    suspend fun insertOrUpdate(record: AlarmRecord) = withContext(Dispatchers.IO) {
        db.alarmRecordQueries.insertAlarmRecord(record)
    }

    suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        db.alarmRecordQueries.deleteById(id)
    }

}