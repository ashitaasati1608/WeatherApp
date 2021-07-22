package com.aasati.hiltdemoapp.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged

/**
 * SafeMutableLiveData
 *
 * Wrapper class for SafeLiveData that allows for public facing updates
 */
open class SafeMutableLiveData<T : Any>(initialValue: T) : SafeLiveData<T>(initialValue) {

    public override fun setValue(value: T) {
        super.setValue(value)
    }

    public override fun update(f: (T) -> T) {
        super.update(f)
    }

    public override fun post(f: (T) -> T) {
        super.post(f)
    }

    /**
     * distinct
     *
     * @param lens: Function to focus on a single nested value.
     * @return a LiveData<U> resulting from applying `lens`.
     */
    fun <U> distinct(lens: (T) -> U): LiveData<U> =
        Transformations
            .map(this, lens)
            .distinctUntilChanged()
}
