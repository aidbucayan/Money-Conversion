package com.adrian.bucayan.androidtest.presentation

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrian.bucayan.androidtest.databinding.ConversionItemsBinding
import com.adrian.bucayan.androidtest.presentation.adapter.BaseRecyclerViewAdapter
import com.adrian.bucayan.androidtest.presentation.adapter.BaseViewHolder
import com.adrian.bucayan.androidtest.presentation.adapter.ViewHolderInitializer

class ConversionAdapter : BaseRecyclerViewAdapter<String, ConversionItemsBinding>() ,
    ViewHolderInitializer<String, ConversionItemsBinding> {

    init { viewBindingInitializer = this }

    class TopSpacingDecoration(private val padding: Int): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            if (parent.getChildAdapterPosition(view) > 0) {
                outRect.top = padding
            }
        }
    }

    override fun generateViewHolder(parent: ViewGroup): BaseViewHolder<String, ConversionItemsBinding> {

        val itemBinding : ConversionItemsBinding = ConversionItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ConversionAdapterViewHolder(itemBinding)
    }

}

class ConversionAdapterViewHolder(
    viewBinding: ConversionItemsBinding) : BaseViewHolder<String, ConversionItemsBinding>(viewBinding) {

    private val conversionText : TextView = viewBinding.tvConversion
    @SuppressLint("SetTextI18n")
    override fun setViews(item: String) {
        super.setViews(item)
        conversionText.text = item
    }

}