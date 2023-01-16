package sb.lib.todolistapp.ui.activities



import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<VDB : ViewDataBinding , VM : ViewModel >   : AppCompatActivity(){

    private var mViewDataBinding :VDB?=null
    private var mViewModel :VM?=null
    abstract  fun getDataBindingVariale() :Int

    fun getDataBinding() : VDB? {
        return mViewDataBinding }


    @LayoutRes
    abstract fun getLayoutId():Int

    private  fun performDataBinding(){

        mViewDataBinding =  DataBindingUtil.setContentView(this ,getLayoutId())

        val viewModel = if(mViewModel==null)getViewModel()else mViewModel

        mViewDataBinding?.let {
            it.setVariable(getDataBindingVariale() ,viewModel)
            it.executePendingBindings() }
    }


    abstract fun getViewModel(): VM?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding() }
//
//    fun File.toUri(context: Context ): Uri {
//        val authority = context.getString(R.string.authority)
//        return  FileProvider.getUriForFile(context, authority, this) }
//
//    fun sharePdf(files: List<File?>, context: Context) {
//        val uris: ArrayList<Uri> = ArrayList()
//        for (file in files) {
//            uris.add(file!!.toUri(this))
//        }
//        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
//        val type = context.resources.getString(R.string.type)
//        intent.type = type
//        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
//        context.startActivity(
//            Intent.createChooser(
//                intent, null
//            ))
//
//        toast(R.string.sharing , context)
//
//    }

    private fun toast(value : Int  ,context: Context ){
        Toast.makeText(context,value,Toast.LENGTH_LONG).show()

    }




}