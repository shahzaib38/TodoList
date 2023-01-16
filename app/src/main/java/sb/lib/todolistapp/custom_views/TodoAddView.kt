package sb.lib.todolistapp.custom_views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.XmlRes
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toRectF
import sb.lib.todolistapp.R
import sb.lib.todolistapp.models.BottomBarItem
import sb.lib.todolistapp.models.BottomBarParser
import sb.lib.todolistapp.utils.BitmapUtils
import sb.lib.todolistapp.utils.DensityUtils


class TodoAddView @JvmOverloads constructor(context:Context , attr : AttributeSet?=null ,defStyle:Int =0) : View(context , attr , defStyle ){


    private val circleButtonSize = DensityUtils.pxToDp(56f, context.resources).toInt()
    private val centerMargin = DensityUtils.pxToDp(20f, context.resources).toInt()
    private val mainViewWidth  : Int = DensityUtils.pxToDp(126f, context.resources).toInt()
    private val mainViewHeight  : Int = DensityUtils.pxToDp(38f, context.resources).toInt()
    private val verticalMargin = DensityUtils.pxToDp(12f  ,context.resources).toInt()


    private val radius = DensityUtils.pxToDp(9f , context.resources)
    private val contentTextSize = DensityUtils.pxToDp(14f , context.resources)
    private var leftMargin =  DensityUtils.pxToDp(8f ,context.resources)
    private var alpha =  0


    companion object {

        private const val INVALID_RES = -1
        private val TAG = TodoAddView::class.java.simpleName

    }

    private var items = listOf<BottomBarItem>()


    @XmlRes
    private var _itemMenuRes: Int = INVALID_RES

    var itemMenuRes: Int
        @XmlRes get() = _itemMenuRes
        set(@XmlRes value) {
            _itemMenuRes = value
            if (value != INVALID_RES) {

                items = BottomBarParser(context, value).parse()
                invalidate()
            }
        }



    init {

        initAttribute(context,attr ,defStyle)

        loadImages()

    }



    private lateinit var addBitmap : Bitmap
    private lateinit var clearBitmap : Bitmap
    private lateinit var tickBitmap : Bitmap

    private var isVisible = false


    private fun loadImages(){



        // Home Active Bitmap
        val addIcon  = ResourcesCompat.getDrawable(context.resources ,R.drawable.add_icon , null )!!
         addBitmap   =  BitmapUtils.drawableToBitmap(addIcon  )!!

        val clearIcon  = ResourcesCompat.getDrawable(context.resources ,R.drawable.clear_icon , null )!!
        clearBitmap   =  BitmapUtils.drawableToBitmap(clearIcon  )!!



        val tickIcon  = ResourcesCompat.getDrawable(context.resources ,R.drawable.tick_icon , null )!!
        tickBitmap   =  BitmapUtils.drawableToBitmap(tickIcon  )!!

    }

    private var mainContentViewHeight : Int =0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        mainContentViewHeight =  (items.size * mainViewHeight) + (verticalMargin * (items.size -1))

               val totalHeight = mainContentViewHeight +  centerMargin + circleButtonSize

               val measuredWidth  = MeasureSpec.makeMeasureSpec(mainViewWidth , MeasureSpec.AT_MOST)
               val measuredHeight =     MeasureSpec.makeMeasureSpec(totalHeight,MeasureSpec.AT_MOST)

               setMeasuredDimension(measuredWidth,measuredHeight) }


    private val itemRect : Array<Rect> = Array<Rect>(items.size){Rect(0,0,0,0)}


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)



        val measuredHeight   =  mainContentViewHeight / items.size

        var top = 0

        for(i in items.indices step 1){


            val rect = Rect(0,top ,mainViewWidth,top + measuredHeight)

            itemRect[i] =  rect


            top += verticalMargin + measuredHeight

        }


    }


    private fun initAttribute(context: Context, attr: AttributeSet?, defStyle: Int) {

        try {

            val typedArray = context.obtainStyledAttributes(attr, R.styleable.TodoAddView, defStyle, 0)


            itemMenuRes =         typedArray.getResourceId(R.styleable.TodoAddView_edit_menu ,itemMenuRes)

            typedArray.recycle()

        }catch (e :Exception ){

        }

    }


    private val backgroundPaint = Paint().apply {
        this.color = Color.parseColor("#3D3F47")
    }

    private val textPaint = Paint().apply {

        this.textSize = contentTextSize
        this.color = Color.WHITE

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canvas == null)return



        for(i in itemRect.indices step 1 ){
            val rect = itemRect[i]

            backgroundPaint.alpha =alpha
            textPaint.alpha =alpha

            canvas.drawRoundRect(rect.toRectF() ,radius,radius , backgroundPaint)
            val centerY =  rect.centerY() -((textPaint.descent() + textPaint.ascent())/2)

            canvas.drawText(items[i].title ,leftMargin  ,centerY , textPaint)



     val iconX  =  textPaint.measureText(items[i].title) + leftMargin + leftMargin +leftMargin

            val iconY = rect.centerY() - (tickBitmap.height/2f)
            canvas.drawBitmap(tickBitmap , iconX,iconY , backgroundPaint )
        }


        drawCircle(canvas )




    }


    private val circlePaint = Paint().apply {
        this.color = Color.parseColor("#DB4542") }



    private val circleRect = Rect()


    private val bitmapPaint = Paint().apply {


    }




    private fun drawCircle(canvas: Canvas) {


        // DrawCircle
        val top = mainContentViewHeight + centerMargin
        circleRect.set(0,top , mainViewWidth , top + circleButtonSize)
        val centerY = circleRect.centerY().toFloat()
        val centerX = circleRect.centerX().toFloat()
        canvas.drawCircle(centerX , centerY ,  circleButtonSize/2f ,circlePaint,)



    //Draw Bitmap

        if(isVisible){

        val halfX = circleRect.centerX() - (addBitmap.width/2f)
        val halfY = circleRect.centerY() - (addBitmap.height/2f)
        canvas.drawBitmap(clearBitmap , halfX, halfY , bitmapPaint)

        }else{
            val halfX = circleRect.centerX() - (addBitmap.width/2f)
            val halfY = circleRect.centerY() - (addBitmap.height/2f)
            canvas.drawBitmap(addBitmap , halfX, halfY , bitmapPaint)

        } }



    private fun setAlpha(from :Int , to :Int ){

        ValueAnimator.ofInt(from , to).apply {

            addUpdateListener {

                alpha = it.animatedValue as Int

                invalidate() }


        }.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val action = event?:return false

        when(action.action) {

            MotionEvent.ACTION_DOWN ->{

                return true }

            MotionEvent.ACTION_UP ->{

                if(isVisible){

                    isVisible = false
                    setAlpha(255,0)

                } else {

                    isVisible = true

                    setAlpha(0 ,255)

                    }




                for(i in 0 until  items.size step 1){

                    val rect = itemRect[i]
                    if(rect.contains(event.x.toInt(),event.y.toInt() )){


                        Log.i(TAG , "Clicked  ${items[i].title}")

                        selectListener?.omSelect(i )
                        break
                    }



                }



                return true

            }

        }


        return false }



    private var selectListener : SelectListener?=null
    fun setListener(selectListener: SelectListener){
        this.selectListener = selectListener }

    interface  SelectListener {
        fun omSelect(i :Int ) }



    enum class SelectFeature{

        NEW_TASK ,
        SIGN_OUT

    }


}



