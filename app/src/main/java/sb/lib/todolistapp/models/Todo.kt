package sb.lib.todolistapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Todo  constructor(val timeId :Int =0 ,
                        var  title:String ="" ,
                        val description :String ="",val  date :Date = Date() ,val time :Time =Time() ,var isComplete : Boolean  = false    ) : Task , Parcelable