package sb.lib.todolistapp.viewholder


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sb.lib.todolistapp.adapter.TaskAdapter

open class ItemViewHolder<out M> constructor(private val itemView : View) : RecyclerView.ViewHolder(itemView) {

    private var adapter : TaskAdapter?=null

    fun  setAdapter(adapter : TaskAdapter ) {
        this.adapter = adapter
    }

    fun getAdapter():TaskAdapter? = adapter


    fun  onItemSelectionToggle(){
        adapter?.onItemSelectionToggled(adapterPosition)

    }

    fun onItemSelected(){

        adapter?.selectItem(adapterPosition)

    }


     fun onItemDeleted() {
        adapter?.deleteItem(adapterPosition)

    }




    fun isItemSelected():Boolean{
        return adapter!!.isItemSelected(adapterPosition) }


    fun onClickListener(){

        adapter?.onClick(adapterPosition)

    }

    private var item:M?=null
    fun setItem( item : @UnsafeVariance  M) {
        this.item = item }


    fun getItem():M?{
        return item }

}