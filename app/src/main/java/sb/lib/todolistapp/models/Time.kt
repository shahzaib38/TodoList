package sb.lib.todolistapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Time(var hours :Int=0,
var  minutes :Int=0,
val meridiem :Meridiem =Meridiem.AM,
val is24Hours :Boolean = false ) : Parcelable