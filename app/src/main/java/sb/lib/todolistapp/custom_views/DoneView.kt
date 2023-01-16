package sb.lib.todolistapp.custom_views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import sb.lib.todolistapp.R
import sb.lib.todolistapp.utils.DensityUtils

class DoneView @JvmOverloads constructor(context: Context ,attr :AttributeSet?=null ,defStyle:Int=0) : View(context,attr,defStyle) {



    private val viewHeight = DensityUtils.pxToDp(56f , context.resources).toInt()


    init{

        loadImages()
    }

    private lateinit var tickBitmap : Bitmap
    private fun loadImages(){

        val tickIcon = ResourcesCompat.getDrawable( context.resources ,
            R.drawable.tick_icon,null )
        tickBitmap =  NewTaskToolbar.drawableToBitmap(tickIcon!!)!!

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val measuredSized = MeasureSpec.makeMeasureSpec(viewHeight , MeasureSpec.AT_MOST)
        setMeasuredDimension(measuredSized,measuredSized)
    }


    private val backgroundPaint = Paint().apply {

        this.color = Color.RED

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas ==null) return
        drawCircle(canvas)
    }

    private val bitmapPaint = Paint().apply {

    }

    private fun drawCircle(canvas: Canvas) {

        val radius = height/2f
        canvas.drawCircle(radius,radius,radius, backgroundPaint)


        val centerX = (width/2f) - (tickBitmap.width/2f)
        val centerY  = (height/2f)-(tickBitmap.height/2f)


        canvas.drawBitmap(tickBitmap , centerX,centerY ,bitmapPaint)

    }


}