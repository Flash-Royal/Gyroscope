package com.example.accelerometr

import android.os.Parcel
import android.os.Parcelable

class CoordParc(coordX : Int, coordY: Int, coordZ: Int) : Parcelable{
    var x : Int = coordX
    var y : Int = coordY
    var z : Int = coordZ
    constructor(parcel: Parcel) : this(
        coordX = parcel.readInt(),
        coordY = parcel.readInt(),
        coordZ = parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(x)
        parcel.writeInt(y)
        parcel.writeInt(z)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoordParc> {
        override fun createFromParcel(parcel: Parcel): CoordParc {
            return CoordParc(parcel)
        }

        override fun newArray(size: Int): Array<CoordParc?> {
            return arrayOfNulls(size)
        }
    }
}