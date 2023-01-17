package sb.lib.todolistapp.service

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.utils.DateUtils
import java.util.PriorityQueue


interface Scheduler {

    fun add(userID :String )
    fun delete(userId :String )
    fun setUserId(userID: String)

}


class TodoScheduler constructor(private val context: Context, private val todoService: TodoService) : Scheduler{



    private val todoQueue = PriorityQueue<Todo>(comparator)

    companion object {

        const val ONE_SECOND = 1000L

        private val TAG = TodoScheduler::class.java.simpleName

        @Volatile
        private  var INSTANCE  : Scheduler?=null

        fun getInstance(context: Context,service: TodoService): Scheduler =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: TodoScheduler(context ,service).also { INSTANCE = it  } }



        //Comparator
        var comparator = java.util.Comparator<Todo> { todo1, todo2->

            val time1 = todo1.date.date
            val time2 = todo2.date.date

            when {
                time1 < time2 -> {
                    -1

                }
                time1 > time2 -> {

                    1
                }
                else -> {
                    0

                }
            }


        }


    }


    private var  userId : String =""

   override fun setUserId(userID: String){

        this.userId = userID
    }


    private fun execute(userID: String){
        if(countDownTimer!=null && isCountDownStart){

            countDownTimer?.cancel()
        }

        FirebaseFirestore.getInstance().collection(userID)
            .whereEqualTo("complete",false )
            .orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { querySnapShot, error ->

                if(error!=null){

                    return@addSnapshotListener
                }else{

                    if(querySnapShot!=null){

                        if(todoQueue.isNotEmpty()) todoQueue.clear()

                        val todoList  =   querySnapShot.toObjects(Todo::class.java)

                        for(todo in todoList){

                            Log.i(TAG,"Todo Item ${todo.date.date}")
                            todoQueue.add(todo)


                        }

                        schedule()



                    }
                }


            }

    }


    override fun add(userID: String) {

        Log.i(TAG,"Todo Added ")


        execute(userID)

    }

    private fun schedule(){

        val todo = todoQueue.poll()

        if(todo!=null){

            resetCounter(todo )

        }else{

            isCountDownStart =false
            todoService.finish()

        }

    }



    override fun delete(userID: String) {
        Log.i(TAG,"Todo Deleted ")
        execute(userID)

    }

    private var  countDownTimer : CountDownTimer?=null
    private var isCountDownStart = false

  private  fun resetCounter(todo : Todo) {

         isCountDownStart = true



      val timeLeft =   DateUtils.countDownTime(todo)

       countDownTimer =  object : android.os.CountDownTimer(timeLeft, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                val format = DateUtils.convert12HourFormat(millisUntilFinished)
                println("tick" + format)


            }


            override fun onFinish() {

                updateDatabase(todo)

                schedule()

            }
        }.start()


  }

    private fun updateDatabase(todo: Todo) {

        FirebaseFirestore.getInstance()
            .collection(this.userId)
            .document(todo.date.date.toString())
            .update("complete",true )

        todoService.notify(todo )

    }


}