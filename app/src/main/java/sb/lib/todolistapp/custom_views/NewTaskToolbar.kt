package sb.lib.todolistapp.custom_views

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import sb.lib.todolistapp.R
import sb.lib.todolistapp.utils.DensityUtils.pxToDp

class NewTaskToolbar @JvmOverloads constructor(context: Context, attr:AttributeSet?=null, defStyle:Int =0) : View(context , attr ,defStyle){


    private var count : Int = 0
    private var animatedY : Float  = ANIMATED_DEFAULT_VALUE
    private lateinit var cancelBitmap : Bitmap
    private var homeAlpha : Int = 255
    private var allAlpha = 0
    private var innerAlpha = 0
    private var mCircleLeft : Float  = 0f
    private var mCircleWidth :Float = 0f
    private var cancelLeftBorder = 0f
    private var cancelBitmapWidth = 0
    private var mSelection  = Selection.UNSELECT

    enum class Selection {
        UNSELECT ,
        SELECT }



    init {
        createBitmap() }


    private val homePaint = Paint().apply {
        this.color = Color.WHITE
        this.textSize = pxToDp(20f ,context.resources)

    }


    private var text : String = "New Task"

    private val textPaint = Paint().apply{

        this.textSize = pxToDp(20f,context.resources)
        this.color = Color.parseColor("#FFFFFF")

    }

    fun setText(text:String ){
        this.text = text
        invalidate() }


    private val radioStrokePaint = Paint().apply {
        this.isAntiAlias = true
        this.isDither = true
        this.color = Color.WHITE
        this.style = Paint.Style.STROKE }


    private val paintText = Paint().apply {
        this.isAntiAlias = true
        this.isDither = true
        this.textSize = pxToDp(SELECT_TEXT_SIZE,context.resources)
        this.color = Color.WHITE
        this.typeface =  ResourcesCompat.getFont(context , R.font.roboto_medium); }

    private val paintAll = Paint().apply {
        this.isAntiAlias = true
        this.isDither = true
        this.textSize = pxToDp(ALL_TEXT_SIZE,context.resources)
        this.color = Color.parseColor("#EC9B21")
        this.typeface =  ResourcesCompat.getFont(context , R.font.roboto_medium)
    }

    /**
     *Draw All String Button at Right Canvas Border
     */
    private val innerPaint = Paint().apply {

        this.alpha = innerAlpha
        this.isDither = true
        this.isAntiAlias = true
        this.color = Color.WHITE
        this.style = Paint.Style.FILL

    }


    /***
     * Cancel Icon
     *  Setting Margin and Sizes
     */

    private val cancelPaint = Paint().apply{

    }



    private fun createBitmap(){
        val cancelVector = ResourcesCompat.getDrawable( context.resources ,
            R.drawable.cross_icon,null )
        cancelBitmap =  drawableToBitmap(cancelVector!!)!!

    }







    private val leftMargin =pxToDp(20f,context.resources)

    private val leftMarginText = pxToDp(36f,context.resources)

    private  var arrowBitmap : Bitmap

    init {

        val drawableImage  =  ResourcesCompat.getDrawable(context.resources, R.drawable.arrow ,null )
        arrowBitmap =   drawableToBitmap(drawableImage!!)!!

    }



    companion object {

        const val Radio_STROKE_SIZE = 20f
        const val SELECT_TEXT_SIZE = 20f
        const val ALL_TEXT_SIZE = 14f
        const val SELECT_LEFT_X = 31f
        const val LEFT_CANCEL = 21f
        const val INITIAL_ANIMATION =-100f
        const val END_ANIMATION = 0f
        const val ANIMATED_DEFAULT_VALUE = 0f
        private  val TAG = NewTaskToolbar::class.java.simpleName
        private const val DEFAULT_ANIMATION_DURATION = 500L
        private const val DEFAULT_TEXT_DISTANCE = 8f
        private const val TRANSPARENT = 0
        private const val OPAQUE = 255

        fun drawableToBitmap(drawable: Drawable): Bitmap? {
            var bitmap: Bitmap? = null
            if (drawable is BitmapDrawable) {
                val bitmapDrawable = drawable
                if (bitmapDrawable.bitmap != null) {
                    return bitmapDrawable.bitmap
                }
            }
            bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                Bitmap.createBitmap(
                    1,
                    1,
                    Bitmap.Config.ARGB_8888
                ) // Single color bitmap will be created of 1x1 pixel
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize =  MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = (pxToDp(22f,context.resources) * 2).toInt() + cancelBitmap.height
        val measuredHeight = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.AT_MOST)
        setMeasuredDimension(widthMeasureSpec,measuredHeight)
    }


    /**
     * Draw Cancel Icon
     * Draw Selected Text with Number Text
     * Draw Radio Button
     */
    override fun onDraw(canvas: Canvas?) {

        if(canvas==null) return

     //   drawBitmap(canvas)
        drawText(canvas)


    }



    private fun drawText( canvas : Canvas ) {

        homePaint.alpha = homeAlpha
        val left = leftMargin
        val textHeight = (textPaint.descent() + textPaint.ascent()) / 2
        val height = height/2 -textHeight
        canvas.drawText( text ,left , height ,homePaint)
    }


    private fun drawBitmap ( canvas : Canvas ) {

        homePaint.alpha = homeAlpha

        val heightMargin =  height/2 - arrowBitmap.height/2f
        canvas.drawBitmap( arrowBitmap , leftMargin , heightMargin ,homePaint  ) }


    private var leftAllText :Float = 0f
    private var allTextWidth : Float = 0f
    private var textCenter :Float = 0f



    /***
     * Animate Alpha
     *
     *
     *
     *
     */

    private fun animateHomeAlpha( startAlpha :Int  ,endAlpha : Int  ){

        ValueAnimator.ofInt(startAlpha,endAlpha).apply{

            this.duration = 500
            this.interpolator = LinearInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Int
                homeAlpha = value

            }





        }.start()

    }




    private fun animateAllAlpha( startAlpha :Int  , endAlpha : Int   ){

        ValueAnimator.ofInt(startAlpha ,endAlpha).apply{

            this.duration = 500
            this.interpolator = LinearInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Int
                allAlpha = value

            }





        }.start()

    }


    /***
     * Animated Y from -100 to 0
     * Animated Text Count By 1
     */

    private fun animateText(itemCount :Int  ){

        ValueAnimator.ofFloat(INITIAL_ANIMATION , END_ANIMATION).apply {

            this.duration = DEFAULT_ANIMATION_DURATION
            this.interpolator = LinearInterpolator()

            addUpdateListener {
                if(itemCount > 0 ) {
                    animatedY = it.animatedValue as Float

                }

                invalidate() }

            addListener(object  :AnimatorListener{
                override fun onAnimationStart(animation: Animator) {

                    this@NewTaskToolbar.count = 0
                    this@NewTaskToolbar.count = itemCount

                }

                override fun onAnimationEnd(animation: Animator) {

                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }


            })



        }.start()



    }


    private fun isAllTextSelected(x :Float , y :Float ):Boolean {
        val left = leftAllText
        val right = leftAllText +  allTextWidth
        val top = height/2 - textCenter
        val bottom = height/2 + textCenter
        return  (x >= left && x<=right && y>=top && y <= bottom)
    }




    private  fun isCircleSelected(x : Float, y : Float ):Boolean{

        val left =   mCircleLeft
        val right = mCircleLeft + mCircleWidth
        val half = mCircleWidth/2
        val top = height/2 - half
        val bottom = height/2 +half

        return   (x >= left && x<=right && y>=top && y <= bottom) }


    private  fun isCancelTouch(x : Float , y :Float ):Boolean{

        val iconWidth = cancelBitmapWidth
        val left =      cancelLeftBorder

        val right = left + iconWidth

        val half = iconWidth/2

        val top = height/2 - half
        val bottom = height/2 + half

        return   (x >= left && x<=right && y>=top && y <=bottom)
    }

    private fun isVisible():Boolean = count > 0


    private fun selection() {

        if(isVisible()) {

            when( mSelection ) {

                Selection.SELECT -> {
                    selectAllListenenr?.unselectAll()
                    innerAlpha = TRANSPARENT
                    mSelection = Selection.UNSELECT
                    invalidate() }

                Selection.UNSELECT -> {
                    selectAllListenenr?.selectAll()
                    innerAlpha = OPAQUE
                    invalidate()
                    mSelection = Selection.SELECT }
            }
        }


    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) return false


        when(event.action){

            MotionEvent.ACTION_DOWN ->{
                val x = event.x
                val y = event.y





                if(isClicked( x , y )){

                    if(cancelPaint.alpha !=  OPAQUE) {
                        onBackButtonClicked?.onBack()

                    }



                }



            }


            MotionEvent.ACTION_UP ->{

            }

            MotionEvent.ACTION_MOVE ->{


            }



        }

        return false

    }



    private var mClearListener  : ClearListener?=null

    fun setClearListener(clearListener : ClearListener){
        this.mClearListener = clearListener }

    private var selectAllListenenr : SelectAllListener?=null
    fun setSelectAllListener(selectAllListener: SelectAllListener){
        this.selectAllListenenr = selectAllListener }


    interface ClearListener {
        fun clear() }


    interface  SelectAllListener{

        fun selectAll()

        fun unselectAll()

    }

    interface OnBackButtonClickedListener {

        fun onBack()

    }

    private var onBackButtonClicked : OnBackButtonClickedListener?=null
    fun setOnBackButtonListener( onBackButtonClickedListener : OnBackButtonClickedListener){
        this.onBackButtonClicked = onBackButtonClickedListener }





    private fun isClicked(x: Float, y: Float) : Boolean {

        val left = leftMargin
        val  right  = leftMargin + arrowBitmap.width
        val top = height/2 - arrowBitmap.height / 2
        val bottom = height/2 + arrowBitmap.height/2

        return (x>=left && x <=right  && y >=top && y <=bottom) }


}
