package pl.mobite.sample.security.ui.custom

import android.os.Parcel
import android.os.Parcelable


class NavItem(val itemId: Int, val fragmentTag: String, val titleResId: Int): Parcelable {

    override fun describeContents() = 0

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()!!,
            source.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(itemId)
        writeString(fragmentTag)
        writeInt(titleResId)
    }

    companion object {
        val PARCEL_KEY = NavItem.toString()

        @JvmField
        val CREATOR: Parcelable.Creator<NavItem> = object : Parcelable.Creator<NavItem> {
            override fun createFromParcel(source: Parcel): NavItem = NavItem(source)
            override fun newArray(size: Int): Array<NavItem?> = arrayOfNulls(size)
        }
    }
}