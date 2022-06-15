package app.alanfeng.alarm

import androidx.room.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*


@Database(entities = [Plan::class, Alarm::class, AlarmRecord::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun PlanDao(): PlanDao
    abstract fun AlarmDao(): AlarmDao
    abstract fun AlarmRecordDao(): AlarmRecordDao

    companion object {
        val db by lazy {
            Room.databaseBuilder(
                App.context,
                AppDatabase::class.java, "app"
            ).build()
        }
    }
}

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)

    @Delete
    fun delete(vararg obj: T)
}

/*
plan
    name
    alarms
*/
@Entity
data class Plan(
    @PrimaryKey
    val name: String,
    val alarms: List<String>,
    val create: Long
)

@Dao
interface PlanDao : BaseDao<Plan> {
    @Query("select * from `plan` order by `create` desc")
    fun getAll(): List<Plan>
}

/*
Alarm
    id
    hour
    minute
    铃声
    振动
    超时措施：替换为备用方案、重复响铃、
    日期：周几
    排除：节假日
    备注
    TTS播放:备注、通用提醒
    TTS声音
*/
@Entity
data class Alarm(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val hour: Int,
    val minute: Int,
    val ringtone: String,
    val vibration: Boolean,
    val timeout: TimeoutStrategy,
    val dates: List<Int>,
    val exclude: Exclude,
    val marks: String,
    val ttsType: TTSType,
    val ttsVoice: TTSVoice
) {

}

sealed class TimeoutStrategy(val type: String, val data: String?) {
    class Backup(val alarmId: String) : TimeoutStrategy("Backup", alarmId)
    class Repeat(val delayMinutes: Int) : TimeoutStrategy("Repeat", delayMinutes.toString())
}

sealed class Exclude(val type: String, val data: String?) {
    class Specific(val list: List<Long>) : Exclude("Specific", Json.encodeToString(list))
    class Holidays : Exclude("Holidays", null)
}

enum class TTSType {
    None, Mark, General
}

enum class TTSVoice {
    Girl, Boy
}


@Dao
interface AlarmDao : BaseDao<Alarm> {
    @Query("select * from `alarm` order by hour,minute desc")
    fun getAll(): List<Alarm>
}

/*
记录
    id
    Alarm id
    时间
    状态

 */
@Entity
data class AlarmRecord(
    @PrimaryKey
    val id: Int = 0,
    val alarmId: String,
    val time: Long,
    val state: String
) {

}


@Dao
interface AlarmRecordDao : BaseDao<AlarmRecord> {
    @Query("select * from `alarmrecord` order by time desc")
    fun getAll(): List<Alarm>
}


class Converters {
    @TypeConverter
    fun fromListInt(list: List<Int>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toListInt(string: String): List<Int> {
        return Json.decodeFromString(string)
    }
}