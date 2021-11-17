package com.lib.illabank_test_vanshika.scrollUtil

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lib.iilabank_neo.databinding.ReclistLayoutBinding
import java.util.*

class CommonRecycleListAdapter (private val context: Context,
                                private var listItems: MutableList<PagerData>): RecyclerView.Adapter<CommonRecycleListAdapter.CommonViewHolder>(),Filterable {
    var listItemsFilterList = ArrayList<PagerData>()

    lateinit var mContext: Context



    init {
        listItemsFilterList = listItems as ArrayList<PagerData>
    }
    class CommonViewHolder(var rectItemPaymentTypeBinding: ReclistLayoutBinding) :
        RecyclerView.ViewHolder(rectItemPaymentTypeBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val binding = ReclistLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        mContext=parent.context
        return CommonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.rectItemPaymentTypeBinding.tvListTitle.text=listItemsFilterList[position].pagerTitle
        holder.rectItemPaymentTypeBinding.imgListItem
            .setImageDrawable(
                ContextCompat.getDrawable(
                    context, // Context
                    listItemsFilterList[position].PagerImage // Drawable
                )
            )
    }

    override fun getItemCount(): Int {
        return listItemsFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    listItemsFilterList = listItems as ArrayList<PagerData>
                } else {
                    val resultList = ArrayList<PagerData>()
                    for (row in listItems) {
                        if (row.pagerTitle.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    listItemsFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listItemsFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listItemsFilterList = results?.values as ArrayList<PagerData>
                notifyDataSetChanged()
            }

        }
    }
}