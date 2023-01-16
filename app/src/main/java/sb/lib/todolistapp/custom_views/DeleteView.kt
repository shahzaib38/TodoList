package sb.lib.todolistapp.custom_views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import sb.lib.todolistapp.R

class DeleteView @JvmOverloads constructor(context: Context , attr: AttributeSet,defStyle:Int =0) : View(context , attr ,defStyle){


    init {
        loadImages()
    }

    private lateinit var deleteBitmap : Bitmap

    private fun loadImages() {


        val deleteIcon = ResourcesCompat.getDrawable( context.resources ,
            R.drawable.delete_icon,null )
        deleteBitmap =  NewTaskToolbar.drawableToBitmap(deleteIcon!!)!!
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)





    }



}