package sb.lib.todolistapp.custom_views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import sb.lib.todolistapp.R
import sb.lib.todolistapp.utils.DensityUtils

class CustomCheckBox @JvmOverloads constructor(context: Context,attr:AttributeSet?=null ,defStyle:Int= 0) : View(context ,attr,defStyle) {


    private val viewHeight = DensityUtils.pxToDp(16f, context.resources).toInt()


    init {
        obtainAttribute(context,attr,defStyle)



        loadImages()
    }

    private lateinit var checkBitmap : Bitmap

    private fun loadImages() {

        val tickIcon = ResourcesCompat.getDrawable( context.resources ,
            R.drawable.tod0_check_icon,null )
        checkBitmap =  NewTaskToolbar.drawableToBitmap(tickIcon!!)!!

    }

    private fun obtainAttribute(context: Context, attr: AttributeSet?, defStyle: Int) {




    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMeasuredSize = MeasureSpec.makeMeasureSpec(viewHeight , MeasureSpec.AT_MOST)
        val heightMeasuresSize  =  MeasureSpec.makeMeasureSpec(viewHeight ,  MeasureSpec.AT_MOST)

        setMeasuredDimension(widthMeasuredSize,heightMeasuresSize) }

    private val completedTextSize = DensityUtils.pxToDp(5f, context.resources)



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas ==null )return

        drawRect(canvas)

        drawBitmap(canvas)


    }


    private var bitmapAlpha :Int = 0

    private val bitmapPaint = Paint().apply {

    }

    private fun drawBitmap(canvas: Canvas) {


        bitmapPaint.alpha = bitmapAlpha
        val halfWidth = (width/2f) - (checkBitmap.width/2)
        val halfHeight = (height/2f) - (checkBitmap.height/2)

        canvas.drawBitmap(checkBitmap , halfWidth,halfHeight , bitmapPaint)

    }

    private val rect = Rect()


    private val squarePaint = Paint().apply {

        this.color = android.graphics.Color.RED
        this.style = Paint.Style.STROKE
        this.strokeWidth =DensityUtils.pxToDp(4f,context.resources)
    }


    private fun drawRect(canvas: Canvas) {
        rect.set(0,0,width ,height )
        canvas.drawRect(rect ,squarePaint)


    }


    fun check(isChecked :Boolean){

        bitmapAlpha = if(isChecked){

            255
        }else{
            0
        }

        invalidate()
    }


}