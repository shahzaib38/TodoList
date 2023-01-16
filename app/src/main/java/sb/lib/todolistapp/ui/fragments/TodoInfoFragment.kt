package sb.lib.todolistapp.ui.fragments

import android.os.Bundle
import android.system.SystemCleaner
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sb.lib.todolistapp.BR
import sb.lib.todolistapp.R
import sb.lib.todolistapp.custom_views.NewTaskToolbar
import sb.lib.todolistapp.databinding.TodoInfoDataBinding
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.navigators.TodoNavigator
import sb.lib.todolistapp.utils.DateUtils
import sb.lib.todolistapp.viewmodel.TodoListViewModel

@AndroidEntryPoint
class TodoInfoFragment : BaseFragment<TodoInfoDataBinding, TodoListViewModel>() , TodoNavigator ,NewTaskToolbar.OnBackButtonClickedListener {

    private val mViewModel : TodoListViewModel by viewModels()
    private var mTodoInfoDataBinding : TodoInfoDataBinding?=null

    override fun getLayoutId(): Int = R.layout.todo_info_layout
    override fun getBindingVariable(): Int  = BR.viewModel
    override fun getViewModel(): TodoListViewModel  = mViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTodoInfoDataBinding = getDataBinding()


        lifecycleScope.launch {
            launch {
               mViewModel.setNavigator(this@TodoInfoFragment)
               mViewModel.uiTodoState.observe(viewLifecycleOwner) {
                   mTodoInfoDataBinding?.todo = it } }

            launch {
                init() } }

    }


    private val todoInfoFragmentArgs by  navArgs<TodoInfoFragmentArgs>()
    private  var firestore : FirebaseFirestore= FirebaseFirestore.getInstance()
    private var documentReference : DocumentReference?=null

  private  fun  init() {

      val userId =   todoInfoFragmentArgs.userId
      val todo = todoInfoFragmentArgs.todo
      Log.i(TAG,"Todo List ${todo}")

      if(todo!=null && userId!= null ) {
       documentReference =   firestore.collection(userId.userId!!)
              .document(todo.date.date.toString()) }

      mTodoInfoDataBinding?.newTaskToolbar?.setOnBackButtonListener(this )
      mTodoInfoDataBinding?.doneView?.setOnClickListener {
          mViewModel.onDone(userId,todo){
              Toast.makeText(requireContext() , "Todo Successfully Added",Toast.LENGTH_LONG).show()
              findNavController().popBackStack() }
      }


  }




    override fun newTask(name: String) {

    }

    companion object{

        private val TAG = TodoInfoFragment::class.java.simpleName

    }
    override fun changeDate() {

        Log.i(TAG ,"Change Date ")
        val datePicker =  MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener {date:Long ->
            mViewModel.updateDate(date)

        }
        datePicker.show(childFragmentManager ,"DatePicker")


    }

    override fun changeTime() {

        Log.i(TAG ,"Change Time  ")

        val is24HourFormat = is24HourFormat(requireContext())
        val timeFormat = if (is24HourFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setHour(12).setMinute(30).setTimeFormat(timeFormat).build()

        picker.addOnPositiveButtonClickListener {
              mViewModel.updateTime(picker.hour , picker.minute  ,is24HourFormat = is24HourFormat)

        }
            picker.addOnCancelListener { }

            picker.show(childFragmentManager, "timePicker")
    }


    private var listenerRegistration : ListenerRegistration?=null

    override fun onStart() {
        super.onStart()

        Log.i(TAG,"ONSTART")

        listenerRegistration = documentReference?.addSnapshotListener { documentSnapshot, error ->

            if(error!=null){

                return@addSnapshotListener
            }else {


                val todo =     documentSnapshot?.toObject(Todo::class.java)

                if(todo!=null) {

                    mTodoInfoDataBinding?.todo = todo


                }
            }



        }




    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"ONSTOP ")

        listenerRegistration?.remove()
    }

    override fun onBack() {
        findNavController().popBackStack()
    }

}