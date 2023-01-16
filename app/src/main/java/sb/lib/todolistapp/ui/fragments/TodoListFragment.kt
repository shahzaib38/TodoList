package sb.lib.todolistapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sb.lib.todolistapp.BR
import sb.lib.todolistapp.R
import sb.lib.todolistapp.adapter.TaskAdapter
import sb.lib.todolistapp.binders.TodoBinder
import sb.lib.todolistapp.custom_views.TodoAddView
import sb.lib.todolistapp.databinding.TodoFragmentDataBinding
import sb.lib.todolistapp.item_decoration.TodoListItemDecorator
import sb.lib.todolistapp.item_decoration.TodoSectionDecorator
import sb.lib.todolistapp.list_sections.ListSection
import sb.lib.todolistapp.list_sections.NestedSection
import sb.lib.todolistapp.models.Task
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.navigators.TodoNavigator
import sb.lib.todolistapp.utils.OnItemClickListener
import sb.lib.todolistapp.utils.OnSelectionChangedListener
import sb.lib.todolistapp.viewmodel.TodoListViewModel

@AndroidEntryPoint
class TodoListFragment : BaseFragment<TodoFragmentDataBinding, TodoListViewModel>() ,
    TodoAddView.SelectListener ,TodoNavigator , TaskAdapter.OnItemDeleteListener{

    companion object {

        private val TAG  = TodoListFragment::class.java.simpleName

    }



    private val todoListFragmentArgs by  navArgs<TodoListFragmentArgs>()

    private val mViewModel : TodoListViewModel by activityViewModels()
    private var gridLayoutManager : GridLayoutManager?= null
    private var mAdapter = TaskAdapter()
    private var mTodoListFragmentDataBinding : TodoFragmentDataBinding?=null


    override fun getLayoutId(): Int = R.layout.todo_list_fragment
    override fun getBindingVariable(): Int  = BR.viewModel
    override fun getViewModel(): TodoListViewModel = mViewModel


    private  var collectionRefs : CollectionReference?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTodoListFragmentDataBinding = getDataBinding()


        lifecycleScope.launch {

            launch {
                init()
            }

            launch {
                setupRecyclerview()
            }
        }



    }




    private var listSection = ListSection<Todo>()


    private val onItemClickListener = object : OnItemClickListener<Task>{
        override fun onItemClicked(pdfPosition: Int, task: Task) {

            Log.i(TAG ,"onItem Clicked ${task }")

            if(task is Todo) {

                val userId = todoListFragmentArgs.userId
                val action = TodoListFragmentDirections.actionTodoListFragmentToTodoInfoFragment(
                    task
                ,userId)
                findNavController().navigate(action)
            }
        }


    }

    private fun setupRecyclerview(){


        gridLayoutManager = GridLayoutManager(requireActivity() , 1)
        mAdapter.registerBinders( TodoBinder() )
        mAdapter.setOnItemClickListener(onItemClickListener)

        val nestedList = NestedSection()
        nestedList.addSection(listSection)
        mAdapter.addSection(nestedList)

        mAdapter.setOnDeleteListener(this )
        listSection.addDecorator(TodoSectionDecorator(requireContext(),mAdapter))
        mTodoListFragmentDataBinding?.todoRecyclerviewId?.addItemDecoration(TodoListItemDecorator(mAdapter))
        mTodoListFragmentDataBinding?.todoRecyclerviewId?.layoutManager = gridLayoutManager
        mTodoListFragmentDataBinding?.todoRecyclerviewId?.adapter = mAdapter




    }

    private  suspend fun init(){

     val userId =   todoListFragmentArgs.userId


        collectionRefs =  FirebaseFirestore.getInstance().collection(userId.userId.toString())

        mViewModel.setNavigator(this )

        mTodoListFragmentDataBinding?.todoAddId?.setListener(this )


        mViewModel.todoError.collect{
            Toast.makeText(requireContext() , it, Toast.LENGTH_LONG).show()
        }

    }

    override fun omSelect(i: Int) {


        when(TodoAddView.SelectFeature.values()[i]){

            TodoAddView.SelectFeature.NEW_TASK -> {

                val userId = todoListFragmentArgs.userId
                val action = TodoListFragmentDirections.actionTodoListFragmentToTodoInfoFragment(null,userId)
                findNavController().navigate(action )

            }

            TodoAddView.SelectFeature.SIGN_OUT -> {

                mViewModel.signOut()

                Toast.makeText(requireContext(), "Signed out",Toast.LENGTH_LONG).show()
                requireActivity().finish()


            }



        }

    }

    override fun newTask(name: String) {


    }

    override fun changeDate() {

    }

    override fun changeTime() {

    }

    override fun onResume() {
        super.onResume()

        readDataFromServer()
    }

    override fun onPause() {
        super.onPause()

        listSection.clear()
    }


    private fun readDataFromServer(){


        collectionRefs?.get()?.addOnSuccessListener {

        val snapShotDocuments =    it.documents


            for(snapShoutDocument in snapShotDocuments){



                if(snapShoutDocument !=null && snapShoutDocument.exists()) {

               val todo =     snapShoutDocument.toObject(Todo::class.java)


                    if(todo!=null) {
                        listSection.add(todo)
                    }
                }

            }

        }?.addOnFailureListener {



        }

    }

    override fun onItemDelete(i: Int, task: Task) {

        mAdapter.dismissAll()


        val todo = task as  Todo
        collectionRefs?.document(todo.date.date.toString())?.delete()?.addOnSuccessListener {

            Log.i("Delete","onSuccess")

        }?.addOnFailureListener {
            Log.i("Delete","onFailure")

        }


    }


}