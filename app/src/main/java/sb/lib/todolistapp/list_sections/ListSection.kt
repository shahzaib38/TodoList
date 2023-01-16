package sb.lib.todolistapp.list_sections

import android.util.Log
import android.util.SparseIntArray
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import sb.lib.todolistapp.list_sections.PositionType.*
import sb.lib.todolistapp.models.ItemMetaData
import sb.lib.todolistapp.models.Task
import sb.lib.todolistapp.utils.Mode
import sb.lib.todolistapp.utils.OnItemClickListener
import sb.lib.todolistapp.utils.OnSelectionChangedListener


class ListSection< M : Task >  : Section() {


    companion object {

        val TAG = ListSection::class.java.simpleName

    }




    private var dataList = ArrayList<M>()

    private var metaDataList = ArrayList<ItemMetaData>()



    fun add(item:M ){

//        Log.i(TAG,"add Item ${item}")
        add( dataList.size , item )


        Log.i(TAG , "AddItem Section ${item}")
    }


    fun addCount(item:M ){

        addCount( dataList.size , item ) }


   private fun addCount(index :Int , item :M ){

        this.dataList.add(index,item)
        this.metaDataList.add(index, ItemMetaData())
        onInserted(index , 1 )

       onChanged(0, this.dataList.size , COUNT_PAYLOAD)
   }



    /**
     *
     * Add Item in DataList
     * Add Item in MetaDataList
     * notify adapter Item Inserted
     */

       fun add(index :Int , item :M ){

        this.dataList.add(index,item)
        this.metaDataList.add(index, ItemMetaData())
        onInserted(index , 1 ) }


   override  fun size():Int = dataList.size

    /***
     * (1) Create SelectedItems ArrayList
     * (2) Loop through MetaDataList
     * (3)check if Data Item is  selected from MetaDataList
     * (4) If Selected add it to Selected List Items  then increment Position by 1
     *  else increment Position by 1 for next Item
     */
   override fun  getSelectedItems():List<M>{
        val selectedItems  = ArrayList<M>()
        for((itemPosition, itemMetaData) in metaDataList.withIndex()){
            if(itemMetaData.isPdfSelected){
                selectedItems.add(dataList[itemPosition]) } }
        return selectedItems }



    /**
     * Get Size of DataList
     */
    override fun getCount(): Int = dataList.size


    /**
     *  Get item from DataList Adapter
     **/
    override fun getItem(itemPosition: Int) : Task = dataList[itemPosition]


    /**
     * Select Item From Meta DataList
     */
    override fun isItemSelected(itemPosition: Int): Boolean = metaDataList[itemPosition].isPdfSelected


//
//    fun clearSelections(){
//        clearAllSelections()}

    /***********
     * (1) Iterate all Meta Data List
     * (2) check if selected, unselect it
     * (3) notify adapter
     */
    override fun clearAllSelections() {
        var itemPosition = 0
        for(itemMeta in metaDataList){
            if(metaDataList[itemPosition].isPdfSelected){
                metaDataList[itemPosition].isPdfSelected = false
                onChanged(itemPosition,1,SELECTION_PAYLOAD) }

            itemPosition++ } }




    /***
     * remove item from dataList
     * remove item from metaDataList
     * notify Adapter item Removed
     */
    override fun onItemDismiss(itemPosition: Int) {
        val selectedItems = getSelectedItems()


        for( itemMetaData in selectedItems) {

            val indexOfItem =     dataList.indexOf(itemMetaData)
            dataList.removeAt(indexOfItem)
            metaDataList.removeAt(indexOfItem)
            onRemoved(indexOfItem, 1)
        }
    }


     fun onItemDelete(itemPosition: Int) {
        val selectedItems = getSelectedItems()


        for( itemMetaData in selectedItems) {

            val indexOfItem =     dataList.indexOf(itemMetaData)
            dataList.removeAt(indexOfItem)
            metaDataList.removeAt(indexOfItem)
            onRemoved(indexOfItem, 1)
        }

        onChanged(0,this.dataList.size, SELECTION_PAYLOAD)

    }




  fun   onRemove(indexOfItem :Int ){

      dataList.removeAt(indexOfItem)
      metaDataList.removeAt(indexOfItem)
      onRemoved(indexOfItem, 1)
    }




    /***
     * (1) check if size less than or equal to zero no need to clear return call
     * (2 ) get the size of dataList
     * (3) clear dataList items
     * (4) clear MetaDataList items
     * (5) notify adapter itemRemoved()
     */

    fun clear() {

        if(size()<=0){
            return }
        val oldSize = dataList.size
        dataList.clear()

        metaDataList.clear()

        onRemoved(0 , oldSize) }



    private var itemClickListener : OnItemClickListener<M>?=null
//
//    fun setItemOnClickListener(itemClickListener : OnItemClickListener<M>){
//        this.itemClickListener = itemClickListener }


    /**
     * Check if call is not null then pass the value
     *
     */

    override fun onItemClicked(itemPosition: Int) {
        val clickedListener = itemClickListener
        if(null !=clickedListener){
            clickedListener.onItemClicked(itemPosition,dataList[itemPosition]) } }


    /***
     * OnItemSelcctionToggled()
     *
     */
    override fun onItemSelectionToggled(itemPosition: Int, selectionMode: Mode) {

        val selectionModeToHonor =  if(itemPosition<getCount() && itemPosition>=0) getModeToHonour(selectionMode,this.selectionMode) else selectionMode

        if(selectionModeToHonor  == Mode.SINGLE){

        }else{

            toggleItemSelection(itemPosition)

        }




    }


    /******
     *
     * Data Set Selectio Toggle
     *
     * check if itemPosition is less than total dataList and itemPosition is greater than 0
     * add it into metaDataList then update adapter has been Changed with PayLoad_SELECTION
     *
     *
     */

   private  fun toggleItemSelection(itemPosition: Int){

       if(itemPosition < getCount() && itemPosition>=0){
           val itemMetaData =  metaDataList[itemPosition]

           itemMetaData.isPdfSelected = !itemMetaData.isPdfSelected

           onChanged(itemPosition,1 , SELECTION_PAYLOAD)

           notifySelectionChanged(itemPosition)
       } }


    override fun selectAllItems(){


            for (itemPosition: Int in 0 until dataList.size step 1) {
                val itemMetaData = metaDataList[itemPosition]

                if(!itemMetaData.isPdfSelected) {
                    itemMetaData.isPdfSelected = true
                    onChanged(itemPosition, 1, SELECTION_PAYLOAD)
                    notifySelectionChanged(itemPosition) } }



         }

    private fun notifySelectionChanged(itemPosition: Int) {

        if(this.onSelectionChangedListener !=null){

            onSelectionChangedListener?.onSelectionChanged(dataList[itemPosition] ,
                metaDataList[itemPosition].isPdfSelected ,getSelectedItems(),dataList)

        }
    }

    private var onSelectionChangedListener : OnSelectionChangedListener<M>?=null

    fun setOnSelectionChangeListener(onSelectionChangedListener: OnSelectionChangedListener<M>){
        this.onSelectionChangedListener = onSelectionChangedListener }


    override fun clearAll() {
        clear()
    }


    override fun getPositionType(
        itemPosition: Int,
        adapterPosition: Int,
        layoutManager: LayoutManager?
    ): Int {

        return getPositionTypeGrid(itemPosition,adapterPosition,layoutManager!! as GridLayoutManager)
    }


    val mSpanIndexCache = SparseIntArray()


    private fun getPositionTypeGrid(itemPosition: Int ,
                            adapterPosition :Int ,
                            layoutManager : GridLayoutManager ):Int {


        val isReverseLayout  =  isReverseLayout(layoutManager)
       val isVertical = isVerticalLayout(layoutManager)

        val maxSpanCount = (layoutManager).spanCount
        val spanSizeLookup = layoutManager.spanSizeLookup


        var itemPositionType: Int = MIDDLE

        val currentSpan = spanSizeLookup.getSpanIndex(adapterPosition,maxSpanCount)

        val positionOffset = adapterPosition - itemPosition

        val isLeft = currentSpan == 0
        val isRight = currentSpan +1  == maxSpanCount

        val currentGroupIndex = spanSizeLookup.getSpanGroupIndex(adapterPosition,maxSpanCount)

        val isTop = itemPosition === 0 || itemPosition <= maxSpanCount && (!isLeft
                && currentGroupIndex - spanSizeLookup.getSpanGroupIndex(
            positionOffset,
            maxSpanCount)  == 0)


        if(isLeft)
            itemPositionType = itemPositionType or if (isVertical) LEFT else TOP

        if (isRight) {
            itemPositionType = itemPositionType or if (isVertical) RIGHT else BOTTOM }

        if (isTop) {
            itemPositionType =
                itemPositionType or if (isVertical) if (isReverseLayout) BOTTOM else TOP else if (isReverseLayout) RIGHT else LEFT }



        return itemPositionType }

    fun addAll(items : List<M>) :Boolean {





        return addAll(this.dataList.size ,items)
    }


   private  fun addAll(index :Int  , items : List<M>):Boolean {
        val result = this.dataList.addAll(index,items)
        if( result ){
            for (i in index until items.size + index) {
                metaDataList.add(ItemMetaData()) }
                onInserted(index, items.size)
        }


        return result }




}