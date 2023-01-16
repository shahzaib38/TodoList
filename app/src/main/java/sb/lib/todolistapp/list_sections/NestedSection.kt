package sb.lib.todolistapp.list_sections

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sb.lib.todolistapp.enum.SectionPositionType
import sb.lib.todolistapp.models.Task
import sb.lib.todolistapp.notifier.Notifier
import sb.lib.todolistapp.utils.Mode

class NestedSection  : Section()  , Notifier {

    companion object {
        private  val TAG = NestedSection::class.java.simpleName }

    private val sections = ArrayList<Section>()

    private var count = -1

    fun addSection(section: Section){
        section.setNotifier(this )
        sections.add(section )
        section.onInserted(0,section.getCount())

    }


    override fun getCount(): Int {
        if(count ==-1) {
            var itemCount = 0
            for (section in sections) {
                itemCount += section.getCount() }
            count = itemCount }

        return count }


    override fun getItem(itemPosition: Int): Task {
        var pos = itemPosition

        for(section in sections){
            if(pos>=section.getCount()) {
                pos -= section.getCount() }

            return   section.getItem(pos) }
        throw IllegalStateException("Section is not found") }


    override fun isItemSelected(itemPosition: Int): Boolean {
        var pos = itemPosition

        for(section  in sections){
            if(pos>=section.getCount())
                pos -=section.getCount()
            else
               return  section.isItemSelected(pos) }
        return false }


    override fun clearAllSelections() {
        for(section in sections){
            section.clearAllSelections() }
    }


    override fun onItemDismiss(itemPosition: Int) {

        var pos = itemPosition

        for(section in sections) {
        //    if (pos >= section.getCount())
//                pos -= section.getCount()
//            else {
//                section.onItemDismiss(pos)
//                return
//
//            }
//

                section.onItemDismiss(0)


     }


    }

    override fun onItemClicked(itemPosition: Int) {
        var pos = itemPosition
        for(section in sections){
            if(pos>=section.getCount()){
                pos -= section.getCount()
            }else {
                section.onItemClicked( pos )

                return }
        }
    }


    override fun notifySectionItemMoved(section: Section, fromPosition: Int, toPosition: Int) {
      val fromPosition1 =  getAdapterPosition(section,fromPosition)
        val toPosition1 = getAdapterPosition(section,toPosition)
        onMoved(fromPosition1 , toPosition1) }


    override fun notifySectionRangeChanged(
        section: Section,
        positionStart: Int,
        itemCount: Int,
        payload: Any?
    ) { onChanged(getAdapterPosition(section,positionStart) ,itemCount,payload) }

    override fun notifySectionRangeInserted(section: Section, positionStart: Int, itemCount: Int) {

        val adapterPosition = getAdapterPosition(section , positionStart)
        onInserted(getAdapterPosition(section , positionStart),itemCount)}



    override fun notifySectionRangeRemoved(section: Section, positionStart: Int, itemCount: Int) {
       val adapterPosition = getAdapterPosition(section,positionStart)
        onRemoved(adapterPosition ,itemCount) }


    private fun getAdapterPosition(section : Section ,  position : Int ):Int {

        val sectionIndex = sections.indexOf(section)
        var itemPosition = position

        if(sectionIndex < 0){
            throw IllegalStateException("Section does not exist in Parent!") }

        for( i : Int in  0  until sectionIndex step 1){
            itemPosition += sections[i].getCount() }
        return itemPosition
    }


  override fun onItemSelectionToggled(itemPosition: Int , selectionMode : Mode){


        val selectionModeToHonor =  if(itemPosition < getCount() && itemPosition>=0) getModeToHonour(selectionMode ,this.selectionMode ) else selectionMode

        for(section in sections){
            section.onItemSelectionToggled(itemPosition ,selectionMode)

        }


    }

    override fun getSelectedItems(): List<Task> {
        val pdfSelectedItems = ArrayList<Task>()
        for(section in sections){
            pdfSelectedItems.addAll( section.getSelectedItems()) }
        return pdfSelectedItems }


    override fun onDataSetChanged() {
        super.onDataSetChanged()
        count = -1 }

   override fun selectAllItems() {
        for(section in sections){
           section.selectAllItems() } }


    override fun clearAll() {
        for(section  in sections){
            section.clearAll() } }


    override fun getDecoratorOffsets(
        itemPosition: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        adapterPosition :Int)
    {
        super.getDecoratorOffsets(itemPosition, outRect, view, parent, state ,adapterPosition)

        getChildSectionsOffset(itemPosition, outRect,view,parent,state ,itemPosition)

    }

    private fun getChildSectionsOffset(
        itemPosition: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        itemPosition1: Int
    ) {
        var pos = itemPosition

        for ( section in sections ) {

            if ( pos>=section.getCount() ){
                pos -= section.getCount()

            } else {
                section.getDecoratorOffsets(pos, outRect, view, parent, state ,itemPosition1)
                break }
        }
    }

   override fun size(): Int {

        var itemPosition = 0

        for(section in sections){

            itemPosition+=section.size()

        }

        return itemPosition }

    override fun getPositionType(
        itemPosition: Int,
        adapterPosition: Int,
        layoutManager: RecyclerView.LayoutManager?
    ): Int {

        var pos = itemPosition

        for (section in sections) {
            pos -= if (pos >= section.getCount()) {
                section.getCount()
            } else {
                return section.getPositionType(pos, adapterPosition, layoutManager)
            }
        }
        return PositionType.MIDDLE
    }



    //Section Type

    fun getSectionPositionType(itemPosition: Int) : SectionPositionType {

        var sectionPosition = 0

        var pos = itemPosition

        for(section  in sections){

            if(pos >= section.getCount()){

                pos -=section.getCount()

            }else {

                return section.getSectionPositionType(itemPosition,sectionPosition,sections.size)
            }

            sectionPosition++
        }


        return SectionPositionType.MIDDLE }


    override fun getSectionPositionType(
        itemPosition: Int,
        sectionPosition: Int,
        size: Int
    ): SectionPositionType {

        val parentSectionPositionType = super.getSectionPositionType(itemPosition, sectionPosition, size)
        if(parentSectionPositionType == SectionPositionType.MIDDLE) return SectionPositionType.MIDDLE
        val currentSectionPositionType = getSectionPositionType(itemPosition)

      return   if(parentSectionPositionType == currentSectionPositionType) currentSectionPositionType
               else SectionPositionType.MIDDLE }




}