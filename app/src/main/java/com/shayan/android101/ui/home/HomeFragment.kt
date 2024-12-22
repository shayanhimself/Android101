package com.shayan.android101.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shayan.android101.databinding.FragmentHomeBinding
import coil.load
import coil.transform.RoundedCornersTransformation
import com.shayan.android101.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.viewState.observe(viewLifecycleOwner) {
            updateUI(it)
        }

        return root
    }

    private fun updateUI(viewState: HomeViewModel.ViewState) = with(binding) {
        progressBar.visibility = if (viewState.isLoading) View.VISIBLE else View.GONE

        viewState.product?.let { product ->

            val cornerRadius = context?.resources?.getDimension(R.dimen.rounded_corner) ?: 1f

            title.text = product.title
            description.text = product.description
            price.text = getString(R.string.price_formatted, product.price)
            rating.text = getString(R.string.rating_formatted, product.rating.rate, product.rating.count)

            photo.load(product.image) {
                transformations(RoundedCornersTransformation(cornerRadius))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
