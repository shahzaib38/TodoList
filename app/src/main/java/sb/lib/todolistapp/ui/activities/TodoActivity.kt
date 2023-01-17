package sb.lib.todolistapp.ui.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import sb.lib.todolistapp.BR
import sb.lib.todolistapp.R
import sb.lib.todolistapp.service.TodoService
import sb.lib.todolistapp.databinding.TodoListDataBinding
import sb.lib.todolistapp.viewmodel.TodoListViewModel
import sb.lib.todolistapp.viewmodel.TodoViewModel


@AndroidEntryPoint
class TodoActivity : BaseActivity<TodoListDataBinding,TodoViewModel >() {

    private val mViewModel : TodoViewModel by viewModels()
    private var mTodoDataBinding : TodoListDataBinding? =  null
    override fun getDataBindingVariale(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.todo_list_actiivity
    override fun getViewModel(): TodoViewModel = mViewModel


    private lateinit var mService: TodoService
    private var  mBound : Boolean =false


    companion object{


        private val TAG = TodoActivity::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mTodoDataBinding  = getDataBinding()




    }



    fun add(){

        if(!mService.isServiceRunning()){

            val intent = Intent(this , TodoService::class.java)
            intent.action = TodoService.ADD_SERVICE
            intent.putExtra(TodoService.Add_kEY, "")

            ContextCompat.startForegroundService(this.applicationContext ,intent)


            Log.i(TAG ,"Seervice ")

        }else{

            Log.i(TAG ,"Seervice ")

        }



    }


    private  val mConnection  = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder  =service as TodoService.LocalBinder
            mService =       binder.getService()
            mBound =true }
        override fun onServiceDisconnected(name: ComponentName?) {
            mBound =false }
    }



    override fun onStart() {
        super.onStart()
        val intent = Intent(this , TodoService::class.java)
        bindService(intent , mConnection  , Context.BIND_AUTO_CREATE )
    }

    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
        mBound = false }



}