package sb.lib.todolistapp.list_sections

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import sb.lib.todolistapp.enum.SectionPositionType
import sb.lib.todolistapp.item_decoration.Decorator
import sb.lib.todolistapp.models.Task
import sb.lib.todolistapp.notifier.Notifier
import sb.lib.todolistapp.utils.Mode


abstract class Section  : ListUpdateCallback {



    private val decorators = ArrayList<Decorator>()


    companion object  {

        const val SELECTION_PAYLOAD = "selection_payload"
        const val COUNT_PAYLOAD = "total_page_count"

    }


    open  var selectionMode : Mode = Mode.INHERIT


    private var notifier : Notifier?=null

    fun setNotifier(notifier: Notifier){
        this.notifier = notifier }


    override fun onInserted(position: Int, count: Int) {
        onDataSetChanged()
        notifier?.notifySectionRangeInserted(this , position , count) }

    override fun onRemoved(position: Int, count: Int) {
        onDataSetChanged()
        notifier?.notifySectionRangeRemoved(this,position,count) }


    override fun onMoved(fromPosition: Int, toPosition: Int) {
        onDataSetChanged()
        notifier?.notifySectionItemMoved(this,fromPosition, toPosition) }


    override fun onChanged(position: Int, count: Int, payload: Any?) {
        onDataSetChanged()
        notifier?.notifySectionRangeChanged(this,position,count,payload) }


    abstract  fun getCount():Int

    abstract fun getItem(itemPosition :Int ):Task

    abstract fun isItemSelected(itemPosition: Int) : Boolean

    abstract fun clearAllSelections()

    abstract fun onItemDismiss(itemPosition: Int)

    abstract fun onItemClicked(itemPosition: Int)


   open  fun onDataSetChanged(){

        //
    }


     fun getModeToHonour(parentMode : Mode, childMode: Mode): Mode {
         return if(childMode !=Mode.INHERIT)childMode else parentMode }

   abstract fun onItemSelectionToggled(itemPosition: Int, selectionMode: Mode)

    abstract fun  getSelectedItems():List<Task>

    abstract fun selectAllItems()

    abstract fun clearAll()


  open   fun getDecoratorOffsets(itemPosition :Int
                            , outRect:  Rect
                            , view : View,
                            parent : RecyclerView,
                            state : RecyclerView.State ,adapterPosition :Int ) {

        if( decorators.size > 0 ){
            for(decorator in decorators){
                decorator.getItemOffsets(outRect,view , parent,state,itemPosition) } } }



    fun addDecorator(decorator : Decorator ){
        decorators.add(decorator) }



    fun removeDecorator(decorator: Decorator):Boolean{
        return decorators.remove(decorator) }

    fun removeDecorator(index:Int ){
         decorators.removeAt(index) }


    open fun isReverseLayout(layoutManager: LayoutManager?): Boolean {
        return (layoutManager is LinearLayoutManager
                && layoutManager.reverseLayout)
    }

    fun isVerticalLayout( layoutManager : LayoutManager ) : Boolean {
        return (layoutManager is LinearLayoutManager && (layoutManager as LinearLayoutManager).orientation == VERTICAL) }

    abstract fun size(): Int


    abstract fun getPositionType(
        itemPosition: Int,
        adapterPosition: Int,
        layoutManager: LayoutManager?
    ): Int

   open   fun getSectionPositionType(
        itemPosition: Int,
        sectionPosition: Int,
        size: Int
    ): SectionPositionType {

         return if(size -1 ==sectionPosition) SectionPositionType.LAST
         else if(sectionPosition ==0) SectionPositionType.FIRST
         else SectionPositionType.MIDDLE }


}