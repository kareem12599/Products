package com.example.products.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.products.R
import com.example.products.data.model.Products
import com.example.products.databinding.ProductFragmentBinding
import com.example.products.ui.adapter.ProductAdapter
import com.example.products.ui.viewmodel.ProductsViewModel
import com.example.products.util.unCheckOtherChips
import com.example.products.util.setUpActionBar
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()

    private  var _binding: ProductFragmentBinding ?= null

    private val binding get() = _binding!!

    @Inject
    lateinit var productsAdapter: ProductAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = ProductFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpActionBar("Home", false)
        setupUI()
        setUpViewModelObservers()
    }


    private fun setUpViewModelObservers() {
        viewModel.categories.observe(viewLifecycleOwner, {
            binding.errorView.root.visibility = View.GONE
            updateViewWithCategories(it)
        })

        viewModel.dataLoading.observe(viewLifecycleOwner, { loading ->
            if (!loading && binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false
            binding.progressBar.visibility = if (loading && !binding.swipeRefresh.isRefreshing) View.VISIBLE else View.GONE
        })

        viewModel.products.observe(viewLifecycleOwner, { products ->
            binding.progressBar.visibility = View.GONE
            productsAdapter.submitList(products)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            binding.errorView.apply {
                root.visibility = View.VISIBLE
                errorSubtitleTv.text = errorMessage
            }


        })
    }

    private fun updateViewWithCategories(categories: List<Pair<String, Boolean>>?) {
        if (binding.chipGroup.childCount == categories?.size) return
        categories?.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category_item, null) as Chip).also {
                it.text = category.first
                it.tag = category.first
                it.isChecked = category.second
            }

            binding.chipGroup.addView(chip)
        }

    }


    private fun setupUI() {
        productsAdapter = ProductAdapter().also {
            it.listener = object : ProductAdapter.ProductViewHolder.OnItemClickListener {
                override fun onItemClicked(product: Products) {
                    val action = ProductsFragmentDirections.actionProductsFragmentToDetailsFragment()
                            .setProduct(product)
                    findNavController().navigate(action)
                }
            }

        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = productsAdapter
        }


        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }


        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) return@setOnCheckedChangeListener
            group.unCheckOtherChips(checkedId)
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null && chip.text != null) {
                chip.isChecked = true
                viewModel.fetchProductsWithCategory(chip.text.toString())

            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}