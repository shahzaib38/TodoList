package sb.lib.todolistapp.ui.fragments


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import sb.lib.todolistapp.ui.activities.BaseActivity


abstract class BaseFragment<VDB : ViewDataBinding,VM : ViewModel> : Fragment() {

    @LayoutRes
    abstract fun getLayoutId():Int

    private var mDataBinding : VDB?=null
    abstract   fun getBindingVariable():Int
    private var mViewModel :VM?=null
    abstract fun getViewModel():VM

    private var mActivity: BaseActivity<*, *>? = null

    override fun onDetach() {
        mActivity =null
        super.onDetach() }


    fun getDataBinding():VDB?{

        return mDataBinding }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mDataBinding =   DataBindingUtil.inflate(inflater , getLayoutId(),container ,false )

        val  viewModel =if(mViewModel==null) getViewModel() else mViewModel

        mDataBinding?.run {
            this.setVariable(getBindingVariable() ,viewModel)

            this.lifecycleOwner =this@BaseFragment
            this.executePendingBindings() }

        return mDataBinding?.root }


    fun  getBaseActivity(): BaseActivity<*, *>?{
        return mActivity
    }


}