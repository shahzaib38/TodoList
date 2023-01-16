package sb.lib.todolistapp.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import sb.lib.todolistapp.BR
import sb.lib.todolistapp.R
import sb.lib.todolistapp.databinding.TodoListDataBinding
import sb.lib.todolistapp.viewmodel.TodoListViewModel


@AndroidEntryPoint
class TodoActivity : BaseActivity<TodoListDataBinding, TodoListViewModel>() {

    private val mViewModel : TodoListViewModel by viewModels()


    private var mTodoDataBinding : TodoListDataBinding? =  null


    override fun getDataBindingVariale(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.todo_list_actiivity
    override fun getViewModel(): TodoListViewModel = mViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mTodoDataBinding  = getDataBinding()



    }






}