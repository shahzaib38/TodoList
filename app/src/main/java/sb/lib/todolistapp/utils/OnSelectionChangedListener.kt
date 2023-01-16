package sb.lib.todolistapp.utils


interface OnSelectionChangedListener<M> {

    fun onSelectionChanged ( pdfItem : M,  isPdfSelected : Boolean,  pdfSelectedItems : List<M>, pdfDataList :List<M> )


}