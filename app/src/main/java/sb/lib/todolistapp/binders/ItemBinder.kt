package sb.lib.todolistapp.binders



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import sb.lib.todolistapp.adapter.TaskAdapter
import sb.lib.todolistapp.models.Task
import sb.lib.todolistapp.viewholder.ItemViewHolder


abstract class ItemBinder<out  M : Task,out  VH : ItemViewHolder<M>>() {


    abstract fun canBindData(recyclerItem: Task) : Boolean


    fun onCreateViewHolder(parent: ViewGroup, adapter: TaskAdapter): VH {
        val viewHolder = createViewHolder(parent)
        viewHolder.setAdapter(adapter)
        return viewHolder }


    open  fun getSpanSize():Int {
        return 1 }

    abstract fun createViewHolder(parent: ViewGroup): VH


    open fun inflate( parent: ViewGroup, @LayoutRes layoutResId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutResId, parent, false) }

    fun bindViewHolder(  viewHolder: @UnsafeVariance VH, item:@UnsafeVariance M, payloads: List<*>?){
        bindViewHolder(viewHolder, item) }

    abstract fun bindViewHolder(viewHolder : @UnsafeVariance VH , pdfItem : @UnsafeVariance M )




}