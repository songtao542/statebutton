package com.lolii.filter

import android.text.InputType
import android.util.ArrayMap
import android.view.View
import androidx.annotation.IntDef
import cn.lolii.picker.address.Address
import java.util.*

/**
 * Author:         songtao
 * CreateDate:     2020/9/12 17:58
 */
interface Filter {

    companion object {
        const val TYPE_GROUP = 0
        const val TYPE_DATE = 1
        const val TYPE_DATE_RANGE = 2
        const val TYPE_TEXT = 3
        const val TYPE_CHECKABLE = 4
        const val TYPE_NUMBER = 5
        const val TYPE_NUMBER_RANGE = 6
        const val TYPE_EDITABLE = 7
        const val TYPE_EDITABLE_RANGE = 8
        const val TYPE_ADDRESS = 9

        val TYPE_MAP: ArrayMap<Int, Int> = ArrayMap<Int, Int>().apply {
            put(TYPE_GROUP, R.layout.filter_text)
            put(TYPE_DATE, R.layout.filter_label)
            put(TYPE_DATE_RANGE, R.layout.filter_date_range)
            put(TYPE_TEXT, R.layout.filter_text)
            put(TYPE_CHECKABLE, R.layout.filter_checkable)
            put(TYPE_NUMBER, R.layout.filter_label)
            put(TYPE_NUMBER_RANGE, R.layout.filter_number_range)
            put(TYPE_EDITABLE, R.layout.filter_editable)
            put(TYPE_EDITABLE_RANGE, R.layout.filter_editable_range)
            put(TYPE_NUMBER_RANGE, R.layout.filter_number_range)
            put(TYPE_ADDRESS, R.layout.filter_label)
        }
    }

    @Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
    @IntDef(TYPE_GROUP,
            TYPE_DATE,
            TYPE_DATE_RANGE,
            TYPE_TEXT,
            TYPE_CHECKABLE,
            TYPE_NUMBER,
            TYPE_NUMBER_RANGE,
            TYPE_EDITABLE,
            TYPE_EDITABLE_RANGE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class FilterType

    /**
     * 标签类型
     */
    fun getType(): @FilterType Int
}


interface FilterItem : Filter {
    /**
     * 标签名称
     */
    fun getText(): CharSequence

    /**
     * 标签提示语
     */
    fun getHint(): CharSequence {
        return getText()
    }
}

interface RangeFilterItem : Filter {
    fun getStartText(): CharSequence
    fun getStartHint(): CharSequence {
        return getStartText()
    }

    fun getEndText(): CharSequence
    fun getEndHint(): CharSequence {
        return getEndText()
    }
}

interface FilterGroup : FilterItem {
    fun getChildren(): List<Filter>
    fun isSingleChoice() = false
    override fun getType(): Int {
        return Filter.TYPE_GROUP
    }
}

/**
 * 日期范围限定
 */
interface Boundary<T> {
    fun getMin(): T
    fun getMax(): T
}

interface DateFilterItem : FilterItem, Boundary<Date> {
    fun setDate(date: Date?)
    fun getDate(): Date?
    override fun getType(): Int {
        return Filter.TYPE_DATE
    }
}

interface DateRangeFilterItem : RangeFilterItem, Boundary<Date> {
    fun setStartDate(date: Date?)
    fun getStartDate(): Date?

    fun setEndDate(date: Date?)
    fun getEndDate(): Date?

    override fun getType(): Int {
        return Filter.TYPE_DATE_RANGE
    }
}

interface CheckableFilterItem : FilterItem {
    fun setChecked(checked: Boolean)

    /**
     * 是否被选中
     */
    fun isChecked(): Boolean = false

    override fun getType(): Int {
        return Filter.TYPE_CHECKABLE
    }
}

interface NumberFilterItem : FilterItem, Boundary<Int> {
    fun setNumber(number: Int?)
    fun getNumber(): Int?

    override fun getType(): Int {
        return Filter.TYPE_NUMBER
    }
}

interface NumberRangeFilterItem : RangeFilterItem, Boundary<Int> {
    fun setStartNumber(number: Int?)
    fun getStartNumber(): Int?

    fun setEndNumber(number: Int?)
    fun getEndNumber(): Int?

    override fun getType(): Int {
        return Filter.TYPE_NUMBER_RANGE
    }
}

interface EditableFilterItem : FilterItem {

    fun setText(text: CharSequence)

    /**
     * {see android.text.InputType}
     */
    fun getInputType(): Int {
        return InputType.TYPE_CLASS_TEXT
    }

    override fun getType(): Int {
        return Filter.TYPE_EDITABLE
    }
}

interface EditableRangeFilterItem : RangeFilterItem {
    fun setStartText(text: CharSequence)

    fun setEndText(text: CharSequence)

    /**
     * {see android.text.InputType}
     */
    fun getInputType(): Int {
        return InputType.TYPE_CLASS_TEXT
    }

    override fun getType(): Int {
        return Filter.TYPE_EDITABLE_RANGE
    }
}

interface AddressFilterItem : FilterItem {

    fun getAddress(): Address?

    fun setAddress(address: Address?)

    override fun getType(): Int {
        return Filter.TYPE_ADDRESS
    }
}

interface FilterAdapter {
    /**
     * 布局文件
     */
    fun getLayoutResource(): Map<Int, Int> {
        return Filter.TYPE_MAP
    }

    /**
     * 配置显示
     */
    fun configure(filterType: Int, itemView: View, filter: Filter) {
    }
}