package sb.lib.todolistapp.cache

import android.util.SparseIntArray

class SparseIntArrayCache : Cache {

    private val cache = SparseIntArray()

    override fun append( pdfKey: Int, pdfValue: Int) {
        cache.append(pdfKey,pdfValue) }

    override fun get(pdfKey: Int, valueIfKeyNotFound: Int) : Int = cache.get(pdfKey ,valueIfKeyNotFound)


    override fun clear() {
        cache.clear() }

}