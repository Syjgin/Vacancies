package me.example.vacancies.utils

import org.greenrobot.eventbus.EventBus

object EventBusUtils {
    fun registerIfNeeded(subject: Any) {
        if(!EventBus.getDefault().isRegistered(subject)) {
            EventBus.getDefault().register(subject)
        }
    }

    fun unregisterIfNeeded(subject: Any) {
        if(EventBus.getDefault().isRegistered(subject)) {
            EventBus.getDefault().unregister(subject)
        }
    }
}