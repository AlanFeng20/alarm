package app.alanfeng.alarm

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import app.alanfeng.alarm.database.Alarm
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


sealed class TimeoutStrategy(val type: String, val data: String?) {
    class Backup(val alarmId: String) : TimeoutStrategy("Backup", alarmId)
    class Repeat(val delayMinutes: Int) : TimeoutStrategy("Repeat", delayMinutes.toString())
}

sealed class Exclude(val type: String, val data: String?) {
    class Specific(val list: List<Long>) : Exclude("Specific", Json.encodeToString(list))
    object Holidays : Exclude("Holidays", null)
}

enum class TTSType {
    None, Mark, General
}

enum class TTSVoice {
    Girl, Boy
}


val db by lazy {
    Database(
        driver = AndroidSqliteDriver(
            Database.Schema, App.context, "app.db",
            callback = object :
                AndroidSqliteDriver.Callback(Database.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        ),
        AlarmAdapter = Alarm.Adapter(
            timeoutAdapter =
            object : ColumnAdapter<TimeoutStrategy, String> {
                override fun decode(databaseValue: String): TimeoutStrategy {
                    val data = Json.decodeFromString<TimeoutStrategy>(databaseValue)
                    return when (data.type) {
                        "Backup" -> TimeoutStrategy.Backup(data.data!!)
                        else -> TimeoutStrategy.Repeat(data.data!!.toInt())
                    }
                }

                override fun encode(value: TimeoutStrategy): String {
                    return Json.encodeToString(value)
                }
            },
            excludeAdapter =
            object : ColumnAdapter<Exclude, String> {
                override fun decode(databaseValue: String): Exclude {
                    val data = Json.decodeFromString<Exclude>(databaseValue)
                    return when (data.type) {
                        "Specific" -> Exclude.Specific(Json.decodeFromString(data.data!!))
                        else -> Exclude.Holidays
                    }
                }

                override fun encode(value: Exclude): String {
                    return Json.encodeToString(value)
                }
            },
            datesAdapter =
            object : ColumnAdapter<List<Int>, String> {
                override fun decode(databaseValue: String): List<Int> {
                    return Json.decodeFromString(databaseValue)
                }

                override fun encode(value: List<Int>): String {
                    return Json.encodeToString(value)
                }
            },
            ttsTypeAdapter = EnumColumnAdapter(),
            ttsVoiceAdapter = EnumColumnAdapter(),
        ),
    )
}