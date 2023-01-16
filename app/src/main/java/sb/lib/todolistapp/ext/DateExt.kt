package sb.lib.todolistapp.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import sb.lib.todolistapp.models.Time
import sb.lib.todolistapp.utils.DateUtils


object DateExt {
    @BindingAdapter("app:updateDate", "app:updateTime")
    @JvmStatic
    fun dateFormat(textView: TextView, date: Long , time: Time?) {

        if(time !=null){

            val dateString = DateUtils.convertDateIntoFormat(date)

            val dateFormat = String.format("%s", dateString, time.hours, time.minutes)

            textView.text = dateFormat

        }

    }



    @BindingAdapter("app:updateTodoDate" ,"app:updateTodoTime")
    @JvmStatic
    fun todoTime(textView : TextView , date :Long?  ,time :Time?){


        if(time!=null  && date!=null ) {
            val formatTime =  DateUtils.constructTime(time)
            val formatDate  =   DateUtils.convertDateIntoFormat(date)
            val dateFormat = String.format("%s ", formatTime)

            textView.text = dateFormat }

    }






}



