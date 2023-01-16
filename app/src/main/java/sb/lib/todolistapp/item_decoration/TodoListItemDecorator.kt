package sb.lib.todolistapp.item_decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sb.lib.todolistapp.adapter.TaskAdapter


class TodoListItemDecorator constructor (private val adapter : TaskAdapter ) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {


        val adapterPosition  = parent.getChildAdapterPosition(view)

        if( adapterPosition < 0 ) return

        adapter.getDecorationOffset( outRect , view , parent , state , adapterPosition)


    }


}