package sb.lib.todolistapp.item_decoration



import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sb.lib.todolistapp.adapter.TaskAdapter
import sb.lib.todolistapp.list_sections.PositionType.*

abstract class Decorator constructor ( private val adapter : TaskAdapter) {


    open fun addToRect(outRect: Rect, left: Int, top: Int, right: Int, bottom: Int) {
        outRect.left += left
        outRect.top += top
        outRect.right += right
        outRect.bottom += bottom
    }


    fun getPositionType(adapterPosition: Int , parent: RecyclerView):Int{
        return adapter.getPositionType(parent, adapterPosition) }

    abstract fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView?,  state: RecyclerView.State?, adapterPosition: Int
    )



    fun isFirst(positionType: Int):Boolean {
        return  isItemOnLeftEdge(positionType) }

    fun isRight(positionType: Int):Boolean = isItemOnRightEdge(positionType)


    private fun isItemOnLeftEdge(positionType :Int ):Boolean {
        return positionType and LEFT == LEFT }

    private fun isItemOnRightEdge(positionType: Int): Boolean {
        return positionType and RIGHT == RIGHT }

    fun isTop(adapterPosition: Int):Boolean = isItemOnTopEdge(adapterPosition)

    private fun isItemOnTopEdge( positionType: Int): Boolean {
        return positionType and TOP == TOP
    }

}