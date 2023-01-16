package sb.lib.todolistapp.database.network

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.models.User
import sb.lib.todolistapp.utils.DateUtils
import java.lang.Exception

class TodoRemoteService : RemoteService , OnSuccessListener<DocumentReference> , OnFailureListener {



    companion object {

        private val TAG = TodoRemoteService::class.java.simpleName

        @Volatile
        private  var INSTANCE  : TodoRemoteService?=null

        fun getInstance(): TodoRemoteService =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: TodoRemoteService().also { INSTANCE = it  } } }


    private var mTodoDataBase : FirebaseFirestore = FirebaseFirestore.getInstance()



  override  fun addTodo(todo: Todo,user : User):Task<Void>{


      return  mTodoDataBase
            .collection(user.userId!!)
           .document(todo.date.date.toString())
          .set(todo).addOnSuccessListener {




          }
  }










    fun delete(todo: Todo){

        mTodoDataBase
            .collection("engineer.shahzaib@Gmail.com")
            .document(todo.date.date.toString())
            .delete()

    }

    override fun onSuccess( onSucess : DocumentReference?) {

    }

    override fun onFailure(p0: Exception) {

    }


}