package app.alanfeng.alarm

import com.squareup.sqldelight.android.AndroidSqliteDriver
import app.alanfeng.alarm.Database


val db = DataBase(driver = AndroidSqliteDriver(Database.Schema, App.context, "app.db"))