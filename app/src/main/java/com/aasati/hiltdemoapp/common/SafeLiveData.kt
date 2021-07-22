package com.aasati.hiltdemoapp.common

import androidx.lifecycle.LiveData

/**
 * SafeLiveData
 *
 * Wrapper class for LiveData protects against null-values being posted and retrieved
 */
open class SafeLiveData<T : Any>(initialValue: T) : LiveData<T>() {

    init {
        super.setValue(initialValue)
    }

    override fun getValue() = super.getValue()!!

    override fun setValue(value: T) {
        if (this.value != value) {
            super.setValue(value)
        }
    }

    protected open fun update(f: (T) -> T) {
        value = f(value)
    }

    protected open fun post(f: (T) -> T) {
        postValue(f(value))
    }
}
