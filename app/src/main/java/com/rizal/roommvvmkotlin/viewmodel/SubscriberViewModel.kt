package com.rizal.roommvvmkotlin.viewmodel

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizal.roommvvmkotlin.db.Subscriber
import com.rizal.roommvvmkotlin.db.SubscriberRepository
import com.rizal.roommvvmkotlin.helper.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    var deleteAllOrClearText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Simpan"
        deleteAllOrClearText.value = "Reset"
    }

    fun saveOrUpdate() {

        if (inputName.value == null) {
            statusMessage.value = Event("Nama masih kosong!")
        } else if (inputEmail.value == null){
            statusMessage.value = Event("Email masih kosong!")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Email tidak sesuai!!")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!

                update(subscriberToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!

                insert(Subscriber(0, name, email))

                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    private fun insert(subscriber: Subscriber): Job = viewModelScope.launch {
        val rowInsertId: Long = repository.insert(subscriber)

        if (rowInsertId >- 1) {
            statusMessage.value = Event("Subscriber id $rowInsertId berhasil di tambahkan")
        } else {
            statusMessage.value = Event("Gagal menambahkan subscriber")
        }
    }

    private fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        val rowUpdateId: Int = repository.update(subscriber)

        if (rowUpdateId > 0) {
            inputName.value = null
            inputEmail.value = null

            isUpdateOrDelete = false

            saveOrUpdateButtonText.value = "Simpan"
            deleteAllOrClearText.value = "Reset"

            statusMessage.value = Event("$rowUpdateId berhasil di update")
        } else {
            statusMessage.value = Event("Gagal di update")
        }
    }

    private fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        val rowDeleteId: Int = repository.delete(subscriber)

        if (rowDeleteId > 0) {
            inputName.value = null
            inputEmail.value = null

            isUpdateOrDelete = false

            saveOrUpdateButtonText.value = "Simpan"
            deleteAllOrClearText.value = "Reset"

            statusMessage.value = Event("$rowDeleteId berhasil di hapus")
        } else {
            statusMessage.value = Event("Gagal di hapus")
        }
    }

    private fun clearAll(): Job = viewModelScope.launch {
        val rowClearId: Int = repository.deleteAll()

        if (rowClearId > 0) {
            statusMessage.value = Event("$rowClearId subscriber berhasil di reset")
        } else {
            statusMessage.value = Event("Gagal reset data")
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email

        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber

        saveOrUpdateButtonText.value = "Update"
        deleteAllOrClearText.value = "Hapus"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}