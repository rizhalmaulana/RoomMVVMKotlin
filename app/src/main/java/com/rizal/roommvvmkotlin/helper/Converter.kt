package com.rizal.roommvvmkotlin.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rizal.roommvvmkotlin.db.Subscriber

class Converter {
    @TypeConverter
    fun fromSubscriber(actor: List<Subscriber>):String{
        val type = object : TypeToken<List<Subscriber>>() {}.type
        return Gson().toJson(actor,type)
    }

    @TypeConverter
    fun toSubscriber(actorString: String): List<Subscriber>{
        val type = object : TypeToken<List<Subscriber>>() {}.type
        return Gson().fromJson(actorString,type)
    }
}