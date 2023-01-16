package sb.lib.todolistapp.utils

import sb.lib.todolistapp.models.Meridiem
import sb.lib.todolistapp.models.Time
import sb.lib.todolistapp.models.Todo
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateFormat

object DateUtils {


    const val MIDDAY =12
    const val TIME_FORMAT ="%02d:%02d %s"
    const val AM ="AM"
    const val PM ="PM"
    const val DATE_FORMAT="dd/MM/yyyy"
    const val VALID_TIME= 60000

    fun convertDateIntoFormat(long  :Long ):String{
        val date =  java.util.Date(long)
        val timeFormatter = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        val format =    timeFormatter.format(date)
        return format}


    fun convert12HourFormat(long  :Long ):String{
        val date =  java.util.Date(long)
        val timeFormatter = SimpleDateFormat("hh:mm a")
        val format =    timeFormatter.format(date)
        return format }


    fun  isAmPm(hoursOfDay:Int )
            : Meridiem =  if(hoursOfDay < MIDDAY){ Meridiem.AM
    } else
    { Meridiem.PM }

    fun twelveHourFormat(hours :Int ,minutes :Int ) : String{
        return if(isAmPm(hoursOfDay = hours) == Meridiem.AM){
            String.format(TIME_FORMAT,hours ,minutes , AM) }
        else { String.format(TIME_FORMAT,twelveHourTime(hours) ,minutes , PM) } }

    fun twelveHourTime(hoursOfDay: Int):Int  = hoursOfDay - MIDDAY

    fun convert24HourFormat(long  :Long ):String{
        val date =  java.util.Date(long)
        val timeFormatter = SimpleDateFormat("HH:mm")
        val format =    timeFormatter.format(date)
        return format }

    fun constructTime(time: Time):String{

        return    if(time.is24Hours){

            String.format("%02d:%02d",time.hours ,time.minutes)

        }else{

          twelveHourFormat(hours = time.hours, time.minutes) }

    }




    fun formatTodoDate(smsUiState: Todo):java.util.Date{
        val time =     smsUiState.time!!
        val date =java.util.Date(smsUiState.date.date)

        val day =   DateFormat.format("dd",date )
        val month  =   DateFormat.format("MM",date )
        val year =   DateFormat.format("yyyy",date )




        val calender = Calendar.getInstance()
        calender.set(Calendar.DAY_OF_MONTH ,day.toString().trim().toInt() )
        calender.set(Calendar.MONTH,month.toString().trim().toInt()-1)
        calender.set(Calendar.YEAR ,year.toString().trim().toInt())
        calender.set(Calendar.HOUR_OF_DAY ,time.hours)
        calender.set(Calendar.MINUTE ,time.minutes)

        return calender.time }



    fun countDownTime(todo : Todo): Long {

        val date =   formatTodoDate(todo)

        return date.time - System.currentTimeMillis()}



    fun isValidTime(currentTime :Long ):Boolean = currentTime > VALID_TIME
    fun generateId() :Long {

        return System.currentTimeMillis() }


}