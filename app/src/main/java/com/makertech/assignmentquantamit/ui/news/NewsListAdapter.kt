package com.makertech.assignmentquantamit.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makertech.assignmentquantamit.data.local.entities.NewsArticleEntity
import com.makertech.assignmentquantamit.databinding.ItemNewsCardBinding
import java.util.*

class NewsListAdapter(
    var newsList: List<NewsArticleEntity>,
) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {



    inner class ViewHolder(val binding: ItemNewsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsArticleEntity) {
            binding.apply {
                  Glide.with(itemView)
                      .load(article.urlToImage)
                      .into(itemImage)
                itemSource.text = article.source
                itemTitle.text = article.title
                itemDesc.text = article.description

                val stringDate = article.publishedAt

            }
        }
    }

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return newsList.size
    }
    fun setData(newData: List<NewsArticleEntity>){
        newsList = newData
        notifyDataSetChanged()
    }


}