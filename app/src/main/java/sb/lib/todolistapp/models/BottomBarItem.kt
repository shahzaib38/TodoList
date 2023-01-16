package sb.lib.todolistapp.models

import android.graphics.RectF
import android.graphics.drawable.Drawable


data class BottomBarItem (
    var title: String,
    var contentDescription : String,
    val icon: Drawable,
    var rect: RectF = RectF(),
    var alpha: Int,
    var id:Int
)