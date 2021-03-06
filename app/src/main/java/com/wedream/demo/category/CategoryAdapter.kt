package com.wedream.demo.category

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.common.CommonAdapter

class CategoryAdapter(context: Context) : CommonAdapter<Category, CategoryAdapter.Holder>(context) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tv)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_category_list
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        super.onBindViewHolder(holder, position)
        val data = getData()[position]
        holder.textView.text = data.name
    }

    override fun getViewHolder(view: View): Holder {
        return Holder(view)
    }
}