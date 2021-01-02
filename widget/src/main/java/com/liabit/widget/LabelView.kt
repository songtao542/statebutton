package com.liabit.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt

/**
 * Author:         songtao
 * CreateDate:     2020/9/21 11:39
 */
class LabelView : LinearLayout {

    private lateinit var mLabelTextView: TextView
    private lateinit var mEditTextView: EditText
    private lateinit var mTextView: TextView
    private lateinit var mRightTextView: TextView
    private lateinit var mRightArrowView: ImageView
    private lateinit var mStartIconView: ImageView
    private lateinit var mEndIconView: ImageView
    private var mLabelWithColon = true
    private var mEditable = false

    private var mText: CharSequence = ""
    private var mHint: CharSequence = ""
    private var mRightText: CharSequence = ""
    private var mLabelText: CharSequence = ""
    private var mLabelTextSize: Int = 0
    private var mTextSize: Int = 0
    private var mRightTextSize: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleAttr)
    }

    @SuppressLint("SetTextI18n")
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        LayoutInflater.from(context).inflate(R.layout.label_view, this, true)
        isClickable = true
        mLabelTextView = findViewById(R.id.label)
        mTextView = findViewById(R.id.textView)
        mEditTextView = findViewById(R.id.editText)
        mRightTextView = findViewById(R.id.rightText)
        mRightArrowView = findViewById(R.id.rightArrow)
        mStartIconView = findViewById(R.id.startIcon)
        mEndIconView = findViewById(R.id.endIcon)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.orientation))
            val ori = ta.getInt(0, HORIZONTAL)
            ta.recycle()

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, defStyleRes)
            mEditable = typedArray.getBoolean(R.styleable.LabelView_android_editable, false)
            if (mEditable) {
                mEditTextView.visibility = View.VISIBLE
                mTextView.visibility = View.GONE
            } else {
                mTextView.visibility = View.VISIBLE
                mEditTextView.visibility = View.GONE
            }
            mLabelWithColon = typedArray.getBoolean(R.styleable.LabelView_labelWithColon, false)
            mLabelText = typedArray.getText(R.styleable.LabelView_android_label) ?: ""
            if (mLabelWithColon) {
                mLabelTextView.text = "$mLabelText${context.resources.getString(R.string.colon)}"
            } else {
                mLabelTextView.text = mLabelText
            }

            mText = typedArray.getText(R.styleable.LabelView_android_text) ?: ""
            textView.text = mText

            mHint = typedArray.getText(R.styleable.LabelView_android_hint) ?: ""
            textView.hint = mHint

            mLabelTextSize = typedArray.getDimensionPixelSize(R.styleable.LabelView_labelTextSize, 0)
            if (mLabelTextSize > 0) {
                mLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelTextSize.toFloat())
            }
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.LabelView_android_textSize, 0)
            if (mTextSize > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }
            mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.LabelView_rightTextSize, 0)
            if (mRightTextSize > 0) {
                mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize.toFloat())
            }

            typedArray.getColorStateList(R.styleable.LabelView_android_textColor)?.let {
                textView.setTextColor(it)
            }
            typedArray.getColorStateList(R.styleable.LabelView_android_textColorHint)?.let {
                textView.setHintTextColor(it)
            }
            typedArray.getColorStateList(R.styleable.LabelView_labelTextColor)?.let {
                mLabelTextView.setTextColor(it)
            }
            typedArray.getColorStateList(R.styleable.LabelView_rightTextColor)?.let {
                mRightTextView.setTextColor(it)
            }
            mRightText = typedArray.getText(R.styleable.LabelView_rightText) ?: ""
            mRightTextView.text = mRightText

            if (typedArray.getBoolean(R.styleable.LabelView_showRightArrow, false)) {
                mRightArrowView.visibility = View.VISIBLE
            } else {
                mRightArrowView.visibility = View.GONE
            }

            val rightArrowIcon = typedArray.getDrawable(R.styleable.LabelView_rightArrow)
            if (rightArrowIcon != null) {
                mRightArrowView.setImageDrawable(rightArrowIcon)
            }

            val rightArrowColor = typedArray.getColorStateList(R.styleable.LabelView_rightArrowColor)
            if (rightArrowColor != null) {
                mRightArrowView.imageTintList = rightArrowColor
            }

            val rightArrowPadding = typedArray.getDimension(R.styleable.LabelView_rightArrowPadding, -1f)
            if (rightArrowPadding >= 0) {
                val lp = mRightArrowView.layoutParams ?: LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
                (lp as? MarginLayoutParams)?.let {
                    if (ori == VERTICAL) {
                        it.topMargin = rightArrowPadding.toInt()
                    } else {
                        it.marginStart = rightArrowPadding.toInt()
                    }
                }
                mRightArrowView.layoutParams = lp
            }

            val rightArrowSize = typedArray.getDimension(R.styleable.LabelView_rightArrowSize, -1f)
            if (rightArrowSize > 0) {
                val lp = mRightArrowView.layoutParams ?: LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
                lp.width = rightArrowSize.toInt()
                lp.height = rightArrowSize.toInt()
                mRightArrowView.layoutParams = lp
            }

            val startIcon = typedArray.getDrawable(R.styleable.LabelView_startIcon)
            if (startIcon != null) {
                mStartIconView.visibility = View.VISIBLE
                mStartIconView.setImageDrawable(startIcon)
                val lp = mStartIconView.layoutParams ?: LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
                val startIconSize = typedArray.getDimension(R.styleable.LabelView_startIconSize, -1f)
                if (startIconSize > 0) {
                    lp.width = startIconSize.toInt()
                    lp.height = startIconSize.toInt()
                }
                val startIconPadding = typedArray.getDimension(R.styleable.LabelView_startIconPadding, 0f)
                if (startIconPadding > 0) {
                    (lp as? MarginLayoutParams)?.let {
                        if (ori == VERTICAL) {
                            it.bottomMargin = startIconPadding.toInt()
                        } else {
                            it.marginEnd = startIconPadding.toInt()
                        }
                    }
                }
                mStartIconView.layoutParams = lp
            }

            val endIcon = typedArray.getDrawable(R.styleable.LabelView_endIcon)
            if (endIcon != null) {
                mEndIconView.setImageDrawable(endIcon)
                mEndIconView.visibility = View.VISIBLE
                val lp = mEndIconView.layoutParams ?: LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
                val endIconSize = typedArray.getDimension(R.styleable.LabelView_endIconSize, -1f)
                if (endIconSize > 0) {
                    lp.width = endIconSize.toInt()
                    lp.height = endIconSize.toInt()
                }
                val endIconPadding = typedArray.getDimension(R.styleable.LabelView_endIconPadding, 0f)
                if (endIconPadding > 0) {
                    (lp as? MarginLayoutParams)?.let {
                        if (ori == VERTICAL) {
                            it.topMargin = endIconPadding.toInt()
                        } else {
                            it.marginStart = endIconPadding.toInt()
                        }
                    }
                }
                mEndIconView.layoutParams = lp
            }

            mLabelTextView.gravity = typedArray.getInt(R.styleable.LabelView_labelTextGravity, Gravity.CENTER_VERTICAL)
            val textGravity = typedArray.getInt(R.styleable.LabelView_textGravity, Gravity.CENTER_VERTICAL)
            mTextView.gravity = textGravity
            mEditTextView.gravity = textGravity
            mRightTextView.gravity = typedArray.getInt(R.styleable.LabelView_rightTextGravity, Gravity.CENTER_VERTICAL)

            if (ori == VERTICAL) {
                val llp = textView.layoutParams ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                llp.width = LayoutParams.MATCH_PARENT
                llp.height = LayoutParams.WRAP_CONTENT
                (llp as? LayoutParams)?.weight = 0f
                (llp as? MarginLayoutParams)?.let {
                    it.marginStart = 0
                    it.marginEnd = 0
                }
                mLabelTextView.layoutParams = llp

                val tlp = textView.layoutParams ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                tlp.width = LayoutParams.MATCH_PARENT
                tlp.height = LayoutParams.WRAP_CONTENT
                (tlp as? LayoutParams)?.weight = 0f
                (tlp as? MarginLayoutParams)?.let {
                    it.marginStart = 0
                    it.marginEnd = 0
                }
                textView.layoutParams = tlp

                val rlp = textView.layoutParams ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                rlp.width = LayoutParams.MATCH_PARENT
                rlp.height = LayoutParams.WRAP_CONTENT
                (rlp as? LayoutParams)?.weight = 0f
                (rlp as? MarginLayoutParams)?.let {
                    it.marginStart = 0
                    it.marginEnd = 0
                }
                mRightTextView.layoutParams = rlp

                if (mRightText.isBlank()) {
                    mRightTextView.visibility = View.GONE
                }
            }

            val textPaddingStart = typedArray.getDimension(R.styleable.LabelView_textPaddingStart, 0f)
            val textPaddingEnd = typedArray.getDimension(R.styleable.LabelView_textPaddingEnd, 0f)
            val textPaddingTop = typedArray.getDimension(R.styleable.LabelView_textPaddingTop, 0f)
            val textPaddingBottom = typedArray.getDimension(R.styleable.LabelView_textPaddingBottom, 0f)
            val textPaddingHorizontal = typedArray.getDimension(R.styleable.LabelView_textPaddingHorizontal, 0f)
            val textPaddingVertical = typedArray.getDimension(R.styleable.LabelView_textPaddingVertical, 0f)

            val start = if (textPaddingHorizontal > 0) textPaddingHorizontal else textPaddingStart
            val end = if (textPaddingHorizontal > 0) textPaddingHorizontal else textPaddingEnd
            val top = if (textPaddingVertical > 0) textPaddingVertical else textPaddingTop
            val bottom = if (textPaddingVertical > 0) textPaddingVertical else textPaddingBottom

            textView.setPaddingRelative(start.toInt(), top.toInt(), end.toInt(), bottom.toInt())

            typedArray.recycle()
        }
    }

    private val textView: TextView get() = if (mEditable) mEditTextView else mTextView

    fun setOnLabelClickListener(listener: OnClickListener?) {
        mLabelTextView.setOnClickListener(listener)
    }

    fun setOnTextClickListener(listener: OnClickListener?) {
        textView.setOnClickListener(listener)
    }

    fun setOnRightTextClickListener(listener: OnClickListener?) {
        mRightTextView.setOnClickListener(listener)
    }

    fun setOnStartIconClickListener(listener: OnClickListener?) {
        mStartIconView.setOnClickListener(listener)
    }

    fun setOnEndIconClickListener(listener: OnClickListener?) {
        mEndIconView.setOnClickListener(listener)
    }

    fun setOnArrowClickListener(listener: OnClickListener?) {
        mRightArrowView.setOnClickListener(listener)
    }

    fun setLabelWithColon(withColon: Boolean) {
        mLabelWithColon = withColon
    }

    @SuppressLint("SetTextI18n")
    fun setLabel(label: CharSequence) {
        if (mLabelWithColon) {
            mLabelTextView.text = "$label${context.resources.getString(R.string.colon)}"
        } else {
            mLabelTextView.text = label
        }
    }

    fun setText(text: CharSequence) {
        mText = text
        textView.text = mText
    }

    fun setHint(hint: CharSequence) {
        mHint = hint
        textView.hint = mHint
    }

    fun setRightText(text: CharSequence) {
        mRightText = text
        mRightTextView.text = mRightText
    }

    fun setRightTextColor(@ColorInt textColor: Int) {
        mRightTextView.setTextColor(ColorStateList.valueOf(textColor))
    }

}