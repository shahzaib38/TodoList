package sb.lib.todolistapp.custom_views




import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.XmlRes
import androidx.core.content.res.ResourcesCompat
import sb.lib.todolistapp.R
import sb.lib.todolistapp.models.BottomBarItem
import sb.lib.todolistapp.models.BottomBarParser
import sb.lib.todolistapp.utils.BitmapUtils.drawableToBitmap
import sb.lib.todolistapp.utils.DensityUtils


class EditTopView @JvmOverloads constructor(context :Context , attr:AttributeSet?=null ,defStyle :Int =0 )  : View(context , attr ,defStyle){




    companion object {
        private const val INVALID_RES = -1
        private val TOTAL_COUNT = 2

    }


    private var items = listOf<BottomBarItem>()

    init {

        obtainAttribute(context,attr,defStyle)

    }


    @XmlRes
    private var _itemMenuRes: Int =  INVALID_RES

    var itemMenuRes: Int
        @XmlRes get() = _itemMenuRes
        set(@XmlRes value) {
            _itemMenuRes = value
            if (value != INVALID_RES) {

                items = BottomBarParser(context, value).parse()
                invalidate()
            }
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val changedWidth = width /TOTAL_COUNT
        var left = 0

        for(i in itemRects.indices step 1){
            val rect = Rect(left ,0,left + changedWidth , height)
            itemRects[i] = rect
            left +=changedWidth }

        loadImages()


    }


    private fun obtainAttribute(context: Context, attr: AttributeSet?, defStyle: Int) {


        try {


            val typedArray = context.obtainStyledAttributes(
                attr,
                R.styleable.EditTopView,
                defStyle, 0
            )


            itemMenuRes =         typedArray.getResourceId(R.styleable.EditTopView_task_menu ,itemMenuRes)

            typedArray.recycle()
        }catch (e :Exception){

        }

    }

    private var heightOfView = DensityUtils.pxToDp(40f, context.resources).toInt()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val measureWidthSize =  MeasureSpec.makeMeasureSpec(widthSize ,MeasureSpec.EXACTLY)
        val measuredHeightSize = MeasureSpec.makeMeasureSpec(heightOfView , MeasureSpec.AT_MOST)
        setMeasuredDimension(measureWidthSize , measuredHeightSize) }

    private lateinit var crossBitmap : Bitmap
    private var itemRects = Array<Rect>(TOTAL_COUNT){ Rect(0,0,0,0) }
    private var bitmaps : Array<Bitmap>?=null


    private fun loadImages() {



        val cropIcon  = ResourcesCompat.getDrawable(context.resources , R.drawable.clear_icon , null )!!
        crossBitmap  =    drawableToBitmap(cropIcon)!!

        bitmaps =  Array<Bitmap>(TOTAL_COUNT){crossBitmap}

        val arrayBitmap = bitmaps ?: return

        for(i in items.indices step 1){

            val tickIcon   = items[i].icon // ResourcesCompat.getDrawable(context.resources , R.drawable.delete_icon , null )!!
            val tickBitmap   =    drawableToBitmap(tickIcon)!!
            arrayBitmap[1] = tickBitmap

        }


        invalidate()


    }





    private val bitmapPaint = Paint().apply {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas == null) return
        if(bitmaps ==null) return

        for(i in itemRects.indices step 1){

            val bitmap =        bitmaps!![i]

            val rect = itemRects[i]
            val centerX = rect.centerX().toFloat()
            val centerY =  rect.centerY().toFloat()

            val bitmapX = bitmap.width/2
            val bitmapY = bitmap.height/2

            canvas.drawBitmap(bitmap  , centerX - bitmapX , centerY - bitmapY ,bitmapPaint )
        } }

    private var onSelectionListener : SelectionListener?=null

    fun setOnSelectionListener( editListener : SelectionListener){
        this.onSelectionListener = editListener }

    enum class SelectionFeature{
        CROSS,
        TICK

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event==null) return false

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true }

            MotionEvent.ACTION_UP -> {

                for(index in itemRects.indices step 1) {
                    if (itemRects[index].contains(event.x.toInt() ,event.y.toInt())) {
                        onSelectionListener?.onSelect(index)
                        break }
                }
            }
        }

        return super.onTouchEvent(event)
    }
    interface SelectionListener{

        fun onSelect(index :Int )

    }






}