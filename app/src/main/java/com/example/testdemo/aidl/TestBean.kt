package com.example.testdemo.aidl

import android.os.Parcel
import android.os.Parcelable

class TestBean(var name:String?="") :Parcelable {

    override fun toString(): String {
        return "TestBean(name=$name) hashcode=${hashCode()}"
    }

    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    fun readFromParcel(parcel: Parcel) {
        this.name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TestBean> {
        override fun createFromParcel(parcel: Parcel): TestBean {
            return TestBean(parcel)
        }

        override fun newArray(size: Int): Array<TestBean?> {
            return arrayOfNulls(size)
        }
    }
}