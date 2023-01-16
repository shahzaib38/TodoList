package sb.lib.todolistapp.utils

import android.content.res.Resources
import android.util.TypedValue


object DensityUtils {


    fun pxToDp(fl: Float, resources: Resources): Float {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            fl,
            r.displayMetrics
        )
    }

}