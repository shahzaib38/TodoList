package sb.lib.todolistapp.binders

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import org.w3c.dom.Text
import sb.lib.todolistapp.R
import sb.lib.todolistapp.custom_views.CustomCheckBox
import sb.lib.todolistapp.models.Task
import sb.lib.todolistapp.models.Todo
import sb.lib.todolistapp.utils.DateUtils
import sb.lib.todolistapp.viewholder.ItemViewHolder


class TodoBinder : ItemBinder<Todo, TodoBinder.TodoItemBinderViewHolder>() {



    companion object {

        private val TAG = TodoBinder::class.java.simpleName

    }


    class TodoItemBinderViewHolder ( itemView : View) : ItemViewHolder<Todo>(itemView){


        val title = itemView.findViewById<TextView>(R.id.titleId)
        val date = itemView.findViewById<TextView>(R.id.textView8)
        val time = itemView.findViewById<TextView>(R.id.textView10)
        val checkBox= itemView.findViewById<CustomCheckBox>(R.id.customCheckBox)
        val deleteOverlay = itemView.findViewById<ConstraintLayout>(R.id.deleteincludeId)
        val imageView = itemView.findViewById<ImageView>(R.id.deleteID)



        init {


            imageView.setOnClickListener {
                onItemDeleted()
                }

            itemView.setOnClickListener {
                onItemSelected() }

            itemView.setOnLongClickListener {
                onItemSelectionToggle()
                true } }

    }



    override fun createViewHolder(parent: ViewGroup): TodoItemBinderViewHolder {
        val view = inflate(parent  , R.layout.todo_item)
        return    TodoItemBinderViewHolder(view) }

    override fun canBindData(recyclerItem: Task): Boolean {
        return recyclerItem is Todo }



    override fun bindViewHolder(
        viewHolder : TodoItemBinderViewHolder,
        todoItem: Todo
    ) {


         viewHolder.deleteOverlay.isVisible = viewHolder.isItemSelected()

        viewHolder.title.text = todoItem.title

        bindDate(viewHolder,todoItem)
        bindTime(viewHolder,todoItem)

        bindCheckBox(viewHolder,todoItem)


    }

    private fun bindCheckBox(        viewHolder : TodoItemBinderViewHolder
                                     ,todoItem: Todo) {

        if(todoItem!=null){


            viewHolder.checkBox.check(todoItem.isComplete)
        }
    }


    private fun bindDate(
        viewHolder : TodoItemBinderViewHolder
                                  ,todo: Todo){
        val time = todo.time
        if(todo.time !=null){

            val dateString = DateUtils.convertDateIntoFormat(todo.date.date)

            val dateFormat = String.format("%s", dateString, time.hours, time.minutes)

            viewHolder.date.text = dateFormat

        }

    }






   private  fun bindTime(viewHolder : TodoItemBinderViewHolder
                 ,todo: Todo){
val time = todo.time
        val date = todo.date
        if(time!=null  && date!=null ) {
            val formatTime =  DateUtils.constructTime(time)
            val formatDate  =   DateUtils.convertDateIntoFormat(date.date)
            val dateFormat = String.format("%s ", formatTime)

            viewHolder.time.text = dateFormat }

    }

}