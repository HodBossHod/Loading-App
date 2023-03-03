package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText=resources.getString(R.string.download_button)
    private var buttonBackgroundUnloaded=0
    private var buttonBackgroundWithLoading=0
    private var textColor=0
    private var circleColor=0
    private var progress=0.0f

    private val valueAnimator=ValueAnimator.ofFloat(0.0f,1.0f).setDuration(2000)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        textSize =resources.getDimension(R.dimen.default_text_size)
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create( "", Typeface.BOLD)
    }


    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading->{
                valueAnimator.start()
                buttonText=resources.getString(R.string.button_loading)
                invalidate()
            }
            else->{
                valueAnimator.cancel()
                progress=0.0f
                buttonText=resources.getString(R.string.download_button)
                invalidate()
            }
        }
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackgroundUnloaded = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_CircleColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            buttonBackgroundWithLoading = getColor(R.styleable.LoadingButton_buttonLoadingColor, 0)
        }
        buttonState=ButtonState.Completed
        valueAnimator.apply {
            addUpdateListener {
                progress=it.animatedValue as Float
                invalidate()
            }
            repeatCount=ValueAnimator.INFINITE
            repeatMode=ValueAnimator.RESTART
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            paint.color=buttonBackgroundUnloaded
            drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(),paint)
            paint.color=buttonBackgroundWithLoading
            val progressWidthLoading=progress*widthSize.toFloat()
            drawRect(0f,0f,progressWidthLoading,heightSize.toFloat(),paint)
            paint.color=textColor
            drawText(buttonText,((widthSize.toFloat())/2.0f),((height.toFloat()+height*.2f)/2.0f),paint)
            val progressCircle=progress*360f
            paint.color=circleColor
            drawArc(widthSize*0.8f,heightSize*0.2f,widthSize*0.95f,heightSize*0.8f,0.0f,progressCircle,true,paint)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minimumWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minimumWidth, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}