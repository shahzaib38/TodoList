package sb.lib.todolistapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sb.lib.todolistapp.R
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.ui.activities.TodoActivity
import sb.lib.todolistapp.utils.DateUtils

class TodoService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var todoScheduler: Scheduler
    private var isServiceStarted  = false
    private   var    notificationBuilder : NotificationCompat.Builder?=null
    private var serviceScope = CoroutineScope(Dispatchers.Main)
    private var  notificationManager : NotificationManager?=null

    companion object{
        const  val MESSAGE_ID: String ="messageId"
        const val USER_ID:String ="userId"
        const val ADD_SERVICE = "sb.spp.message_scheduler.add"
        const val REBOOT ="sb.app.message_scheduler.reboot"
        const val DELETE_SERVICE ="sb.spp.message_scheduler.delete"
        const val Add_kEY ="ADD_KEY"
        private  const val channelId = "default_notification_channel_id"
        private  const val completeId = "complete_notification_channel_id"

        private const val NOTIFICATION_ID = 1995
        private const val COMPLETE_NOTIFICATION_ID =20032
        private const val SCHEDULE_NOTIFICATION ="Schedule"
        private const val SCHEDULE_NOTIFICATION_DESCRIPTION ="notification for scheduling sms"
    }



    fun isServiceRunning():Boolean{
        return isServiceStarted }



    override fun onCreate() {
        super.onCreate()

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }


        Log.i("SmsService","Service created")
       todoScheduler =  TodoScheduler.getInstance(this,this  )

        val intent = Intent( this.applicationContext,TodoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        notificationBuilder  = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Todo Task Scheduled")
            .setContentIntent(pendingIntent)

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        showNotification()

        if(intent!=null) {
            val action = intent?.action


            when(action){

                    ADD_SERVICE ->{


                    val userId = intent.getStringExtra(Add_kEY)

                        if(userId!=null) {
                            todoScheduler.setUserId(userId)
                            add(userId)
                        }

                }



                else ->{

//                    Log.i("Boot","Service boot ")
//                    serviceScope.launch(Dispatchers.IO) {
//                        smsScheduler.reSchedule()
//                    }

                }


            }



        }

        return START_STICKY }


     fun add(userId :String ){
        todoScheduler.add(userId)
    }

    private fun showNotification() {


        notificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, SCHEDULE_NOTIFICATION, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = SCHEDULE_NOTIFICATION_DESCRIPTION
            notificationManager?.createNotificationChannel(channel)
        }

        this.startForeground(NOTIFICATION_ID ,notificationBuilder!!.build())

    }

    override fun onBind(intent: Intent?): IBinder? {

        return LocalBinder()
    }


        fun finish() {

            stopForeground(true)
            stopSelf()
        }



    inner class LocalBinder : Binder(){
        fun getService(): TodoService = this@TodoService }



    fun notify(todo : Todo) {

       val  notificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(completeId, SCHEDULE_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH)
            channel.description = SCHEDULE_NOTIFICATION_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent( this.applicationContext,TodoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
     val    notificationBuilder  = NotificationCompat.Builder(this, completeId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Reminder")
            .setContentIntent(pendingIntent)


        notificationBuilder.setContentText(todo.title)

        notificationManager.notify(COMPLETE_NOTIFICATION_ID,notificationBuilder.build())
    }




    fun delete( userId : String){
        todoScheduler.delete(userId) }
}