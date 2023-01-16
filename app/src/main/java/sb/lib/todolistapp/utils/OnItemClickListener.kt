package sb.lib.todolistapp.utils


interface OnItemClickListener<M> {

    fun onItemClicked( pdfPosition : Int , pdfItem: M)

}