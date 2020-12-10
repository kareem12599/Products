package com.example.products.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.products.BuildConfig
import com.example.products.R
import com.example.products.data.model.Products
import com.example.products.databinding.ProductListItemBinding
import javax.inject.Inject

class ProductAdapter @Inject constructor() :
    ListAdapter<Products, ProductAdapter.ProductViewHolder>(DiffCallBack()) {

    lateinit var listener: ProductViewHolder.OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.setOnClickListener {
            listener.onItemClicked(getItem(position))
        }
    }


    class ProductViewHolder(val binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Products?) {
            binding.product.text = item?.name
            Glide.with(binding.root.context).load(BuildConfig.BASE_URL + item?.url)
                .error(R.drawable.ic_image_not_found)
                .into(binding.thumbnail)
        }

        companion object {
            fun create(parent: ViewGroup): ProductViewHolder {
                return ProductViewHolder(
                    ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }

        interface OnItemClickListener {
            fun onItemClicked(product: Products)
        }


    }

    class DiffCallBack : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == oldItem.id
        }

    }

}