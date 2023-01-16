package sb.lib.todolistapp.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import sb.lib.todolistapp.navigators.TodoNavigator
import sb.lib.todolistapp.repositories.TodoRepository
import javax.inject.Inject

@HiltViewModel
class TodoFragmentViewModel @Inject constructor(private val todoRepository: TodoRepository) : BaseViewModel<TodoNavigator>(todoRepository) {




}
