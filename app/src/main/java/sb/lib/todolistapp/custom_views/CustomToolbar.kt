package sb.lib.todolistapp.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import sb.lib.todolistapp.utils.DensityUtils

class CustomToolbar @JvmOverloads constructor(context: Context ,attr:AttributeSet?=null ,defStyle:Int = 0) :View(context , attr,defStyle) {



    private val viewHeight = DensityUtils.pxToDp(56f , context.resources).toInt()
    private val toolTextSize = DensityUtils.pxToDp(20f , context.resources)
    private val horizontalMargin = DensityUtils.pxToDp(17f, context.resources).toInt()


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {


        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val measuredWidth = MeasureSpec.makeMeasureSpec(widthSize , MeasureSpec.EXACTLY)
        val measuredHeight = MeasureSpec.makeMeasureSpec(viewHeight , MeasureSpec.AT_MOST)
        setMeasuredDimension(measuredWidth , measuredHeight) }


    private val textPaint = Paint().apply {

        this.color = Color.WHITE
        this.textSize = toolTextSize

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas==null)return



        canvas.drawColor(Color.parseColor("#1D1C21"))



        val centerX = horizontalMargin +0f

        val centerY = (height/2f) - ((textPaint.descent() + textPaint.ascent())/2)

        canvas.drawText("Todo List" , centerX ,centerY , textPaint)


    }



}