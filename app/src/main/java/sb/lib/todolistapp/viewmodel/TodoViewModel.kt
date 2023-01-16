package sb.lib.todolistapp.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import sb.lib.todolistapp.navigators.InfoNavigator
import sb.lib.todolistapp.repositories.TodoRepository
import javax.inject.Inject


@HiltViewModel
class TodoViewModel @Inject constructor(private val todoRepository : TodoRepository) : BaseViewModel<InfoNavigator>(todoRepository) {




    private var  _TodoError = MutableSharedFlow<String>()
    val todoError =_TodoError.asSharedFlow()



    fun signOut() {

        todoRepository.signOut()
    }



}