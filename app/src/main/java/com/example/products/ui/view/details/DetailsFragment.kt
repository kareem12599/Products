package com.example.products.ui.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.products.BuildConfig
import com.example.products.R
import com.example.products.data.model.Products
import com.example.products.databinding.DetailsFragmentBinding
import com.example.products.util.setUpActionBar


class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()

    private  var _binding:DetailsFragmentBinding?= null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBar("details")
        setUpView(args.product)
    }

    private fun setUpView(product: Products?) {
        product?.let {
            binding.name.text = it.name
            val productPrice = it.salePrice.amount + it.salePrice.currency
            binding.price.text = productPrice
            Glide.with(requireContext()).load(BuildConfig.BASE_URL + it.url)
                .error(R.drawable.ic_image_not_found)
                .into(binding.image)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}