package sb.lib.todolistapp.viewmodel

import android.util.Log
import android.widget.Toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.navigators.TodoNavigator
import sb.lib.todolistapp.repositories.TodoRepository
import javax.inject.Inject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sb.lib.todolistapp.models.Date
import sb.lib.todolistapp.models.Time
import sb.lib.todolistapp.models.User
import sb.lib.todolistapp.utils.DateUtils


@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoRepository : TodoRepository) : BaseViewModel<TodoNavigator>(todoRepository) {


    fun addNewTask(name : String) {
        getNavigator().newTask(name) }


    private val _uiTodoInfo = MutableStateFlow<Todo>(Todo())
    val uiTodoState =_uiTodoInfo.asLiveData()

    init {
        updateDate(System.currentTimeMillis())
    }


    companion object {

        private val TAG = TodoListViewModel::class.java.simpleName
        const val    TIME_RANGE ="choose valid time at least 5 minutes from now "
        const val TASK_TITLE = "Task Title must not be empty"
    }

    fun titleTextWatcher(s: CharSequence?, start: Int, before: Int, count: Int ){


        _uiTodoInfo.update {

            it.copy(title = s.toString())
        }
    }



    private var  _TodoError = MutableSharedFlow<String>()
    val todoError =_TodoError.asSharedFlow()


    private fun setErrorMessage(message :String ){

        viewModelScope.launch {
            _TodoError.emit(message)

        }
    }

    fun onDone(user: User ,oldTodo : Todo? ,navigate: () -> Unit) {


    val todo = oldTodo?: uiTodoState.value!!

   val isTitleEmpty =  todo.title.isEmpty()

        if(isTitleEmpty){
            setErrorMessage(TASK_TITLE)
            return
        }

        val date =   DateUtils.countDownTime(todo)
        val isValid =   DateUtils.isValidTime(date)

        if(!isValid){
            setErrorMessage(TIME_RANGE)
            return }


        addTodo(todo,user , navigate  )

    }


    /**** Update Time*********/
    fun updateTime(hours : Int ,minutes :Int , is24HourFormat :Boolean ){
        _uiTodoInfo.update {it ->
            it.copy(time = Time(hours = hours, minutes = minutes, is24Hours = is24HourFormat) ) } }


    fun changeTime(){

        getNavigator().changeTime()
    }

    fun changeDate(){

        getNavigator().changeDate()
    }

    fun updateDate(date: Long) {

        _uiTodoInfo.update {
            it.copy(date = Date((date)))
        }
    }




   private fun addTodo(todo: Todo ,user: User , navigate : ()->Unit ) {

       Log.i("Todo", "Todo Clicked ")

       viewModelScope.launch(Dispatchers.IO) {


           todoRepository.addTodo(todo, user).addOnSuccessListener {

               Log.i("Todo", "Successful ")
               navigate.invoke()

           }.addOnFailureListener {

               Log.i("Todo", "Exception  ")

           }


       }
   }




    fun signOut() {

        todoRepository.signOut()
    }
}
