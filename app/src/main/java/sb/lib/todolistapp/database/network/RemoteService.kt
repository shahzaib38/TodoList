package sb.lib.todolistapp.database.network

import com.google.android.gms.tasks.Task
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.models.User

interface RemoteService {

    fun addTodo(todo: Todo,user :User ) : Task<Void>
}