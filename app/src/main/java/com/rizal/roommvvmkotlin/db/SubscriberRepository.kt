package com.rizal.roommvvmkotlin.db

class SubscriberRepository(private val dao: SubscriberDAO) {

    val subscribers = dao.getAllSubscribers()

    fun insert(subscriber: Subscriber): Long {
        return dao.insertSubscriber(subscriber)
    }

    fun update(subscriber: Subscriber): Int {
        return dao.updateSubscriber(subscriber)
    }

    fun delete(subscriber: Subscriber): Int {
        return dao.deleteSubscriber(subscriber)
    }

    fun deleteAll(): Int {
        return dao.deleteAll()
    }
}