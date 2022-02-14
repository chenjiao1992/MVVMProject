package com.sencent.dm

import android.os.Parcel
import android.os.Parcelable

/**
 *  Create by chenjiao at  2022/2/14 11:19
 *  描述：
 */
class RoomBean() : Parcelable {
    var name: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomBean> {
        override fun createFromParcel(parcel: Parcel): RoomBean {
            return RoomBean(parcel)
        }

        override fun newArray(size: Int): Array<RoomBean?> {
            return arrayOfNulls(size)
        }
    }
}