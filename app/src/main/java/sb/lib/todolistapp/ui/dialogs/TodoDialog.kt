package sb.lib.todolistapp.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sb.lib.todolistapp.BR
import sb.lib.todolistapp.R
import sb.lib.todolistapp.custom_views.EditTopView
import sb.lib.todolistapp.databinding.TodoDialogDataBinding
import sb.lib.todolistapp.viewmodel.TodoListViewModel

@AndroidEntryPoint
class TodoDialog  : BaseDialog<TodoDialogDataBinding, TodoListViewModel>() ,EditTopView.SelectionListener{

     private val mViewModel : TodoListViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.todo_dialog
    override fun getBindingVariable(): Int  = BR.viewModel
    override fun getViewModel(): TodoListViewModel = mViewModel


    private var mTodoDialogDataBinding : TodoDialogDataBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mTodoDialogDataBinding = getDataBinding()

        mTodoDialogDataBinding?.taskEditId?.setOnSelectionListener(this )

    }

    override fun onSelect(index: Int) {

        when(EditTopView.SelectionFeature.values()[index]){


            EditTopView.SelectionFeature.TICK->{

                val task = mTodoDialogDataBinding!!.nameId!!.text
                mViewModel.addNewTask(task.toString())

                dismiss()
            }


            EditTopView.SelectionFeature.CROSS ->{

                dismiss()

            }

        }

    }


}