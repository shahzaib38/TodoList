package sb.lib.todolistapp.adapter

import sb.lib.todolistapp.list_sections.NestedSection
import sb.lib.todolistapp.list_sections.Section
import sb.lib.todolistapp.models.Task
import sb.lib.todolistapp.notifier.Notifier



import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sb.lib.todolistapp.binders.ItemBinder
import sb.lib.todolistapp.cache.SparseIntArrayCache
import sb.lib.todolistapp.utils.Mode
import sb.lib.todolistapp.utils.OnItemClickListener
import sb.lib.todolistapp.viewholder.ItemViewHolder


class TaskAdapter  :  RecyclerView.Adapter<ItemViewHolder<Task>>(){

    private val nestedSection = NestedSection()
    private val itemBinders = ArrayList<ItemBinder<Task, ItemViewHolder<Task>>>()
    private val viewTypeCache = SparseIntArrayCache()


    fun registerBinders(itemBinder : ItemBinder< Task , ItemViewHolder<Task> >){
        itemBinders.add(itemBinder) }


    fun addSection(section : Section){
        nestedSection.addSection(section) }

    private val notifier =  object  : Notifier {
        override fun notifySectionItemMoved(section: Section, fromPosition: Int, toPosition: Int) {
            onDataSetChanged()
            notifyItemMoved(fromPosition, toPosition) }

        override fun notifySectionRangeChanged(
            section: Section,
            positionStart: Int,
            itemCount: Int,
            payload: Any?
        ) {
            Log.i("changed","viewHolder ${itemCount}  position $positionStart")

            onDataSetChanged()
            notifyItemRangeChanged(positionStart,itemCount, payload)

        }

        override fun notifySectionRangeInserted(
            section: Section,
            positionStart: Int,
            itemCount: Int
        ) {

            Log.i("changed","INSERTED ${itemCount}  position $positionStart")

            onDataSetChanged()
            notifyItemRangeInserted(positionStart, itemCount); }

        override fun notifySectionRangeRemoved(
            section: Section,
            positionStart: Int,
            itemCount: Int
        ) {
            onDataSetChanged()
            notifyItemRangeRemoved(positionStart, itemCount)
        }


    }


    private fun onDataSetChanged(){
        viewTypeCache.clear() }


    init { nestedSection.setNotifier(notifier) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<Task> {

        val binder = itemBinders[viewType].onCreateViewHolder(parent , this )

        return binder }


    override fun onBindViewHolder (
        holder: ItemViewHolder<Task>,
        position: Int,
        payloads: MutableList<Any>
    ) {

        val itemBinder =  itemBinders[getItemViewType(position)]
        holder.setItem(getItem(position))

        if(payloads.size == 0 )
            itemBinder.bindViewHolder(holder  , holder.getItem()!!)
        else
            itemBinder.bindViewHolder(holder,holder.getItem()!!,payloads)
    }


    override fun getItemViewType(position: Int): Int {
        var  viewType = viewTypeCache.get(position, -1)
        if(viewType ==-1){
            val recyclerItem = getItem(position)
            viewType = getItemBinderPositionForItem(recyclerItem)

            viewTypeCache.append(position,viewType) }
        return viewType }

    private fun getItemBinderPositionForItem(recyclerItem: Task): Int {
        var binderPosition  = 0

        for(itemBinder in itemBinders){
            if(itemBinder.canBindData(recyclerItem)){
                return binderPosition }
            binderPosition++ }

        throw IllegalStateException("ItemBinder not found for position item = ${recyclerItem}") }

    fun getItem(position: Int): Task = nestedSection.getItem(position)

    override fun getItemCount(): Int  {
        val itemCount =  nestedSection.getCount()
        return itemCount }

    fun getCount():Int = nestedSection.size()


    override fun onBindViewHolder(holder: ItemViewHolder<Task>, position: Int) {
        val item = getItem(position)
        itemBinders[getItemViewType(position)].bindViewHolder(holder,item ,null ) }


    fun onClick(itemPosition  : Int ) {
        nestedSection.onItemClicked(itemPosition) }


    fun onItemSelectionToggled(adapterPosition :Int ){
        nestedSection.onItemSelectionToggled(adapterPosition, Mode.MULTIPLE) }

    fun isItemSelected(adapterPosition: Int): Boolean {
        return nestedSection.isItemSelected(adapterPosition) }

    fun clearAllSelections() {
        nestedSection.clearAllSelections() }

    fun selectAllItems()  {
        nestedSection.selectAllItems()
    }


    fun getAllSelectedItems():List<Task> = nestedSection.getSelectedItems()

    fun dismissAll() {
        nestedSection.onItemDismiss(0) }

    fun selectItem ( adapterPosition : Int ) {
        val selectedItem = getAllSelectedItems()

        if ( selectedItem.size > 0 ) {
            onItemSelectionToggled(adapterPosition)

        } else {
            onItemClickListener?.onItemClicked(adapterPosition,getItem(adapterPosition))
        }

    }


    private var onItemClickListener : OnItemClickListener<Task>?=null


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<Task>){
        this.onItemClickListener = onItemClickListener }


    fun getDecorationOffset(
        outRect: Rect ,
        view: View ,
        parent: RecyclerView ,
        state: RecyclerView.State ,
        adapterPosition : Int ) {
        nestedSection.getDecoratorOffsets(adapterPosition , outRect , view , parent , state , adapterPosition) }


    fun getSpanCount(adapterPosition: Int):Int {

        var viewType = viewTypeCache.get(adapterPosition, -1)

        if (viewType == -1) {
            val recyclerItem = getItem(adapterPosition)
            viewType = getItemBinderPositionForItem(recyclerItem)
            viewTypeCache.append(adapterPosition, viewType) }

        return itemBinders[viewType].getSpanSize() }


    fun clearAllSections() {
        nestedSection.clearAll() }


    fun getPositionType(parent: RecyclerView, adapterPosition: Int): Int {

        return nestedSection.getPositionType(
            adapterPosition, adapterPosition,
            parent.layoutManager
        )
    }


    interface OnItemDeleteListener {

      fun   onItemDelete(i :Int  ,task :Task)
    }

    private var  onItemDeleteListener : OnItemDeleteListener?=null

    fun setOnDeleteListener(onItemDeleteListener : OnItemDeleteListener){
        this.onItemDeleteListener = onItemDeleteListener
    }

    fun deleteItem(adapterPosition: Int) {

        this.onItemDeleteListener?.onItemDelete( adapterPosition,getItem(adapterPosition))
    }


}