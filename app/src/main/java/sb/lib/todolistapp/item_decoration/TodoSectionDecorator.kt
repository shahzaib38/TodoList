package sb.lib.todolistapp.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sb.lib.todolistapp.adapter.TaskAdapter
import sb.lib.todolistapp.utils.DensityUtils


class TodoSectionDecorator constructor(context: Context,
                                           private val adapter : TaskAdapter) : Decorator(adapter ){


    private val twentyTwoDensity  = DensityUtils.pxToDp(22f ,context.resources).toInt()
    private val twoDensity  = DensityUtils.pxToDp(10f ,context.resources).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView?,
        state: RecyclerView.State?,
        adapterPosition: Int
    ) {


        if(parent!=null) {

            val positionType = getPositionType(adapterPosition, parent)

            if (isFirst(positionType)) {
                outRect.set(twoDensity, twentyTwoDensity, twoDensity, twoDensity)
            } else {

                outRect.set(twoDensity, 0, twoDensity, twoDensity)

            }

        }



    }


}