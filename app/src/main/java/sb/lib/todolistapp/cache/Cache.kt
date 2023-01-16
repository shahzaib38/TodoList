package sb.lib.todolistapp.cache

interface Cache {


    fun append(pdfKey :Int , pdfValue :Int )
    fun get(pdfKey:Int , valueIfKeyNotFound :Int ):Int
    fun clear()

}