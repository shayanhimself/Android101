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

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cornerRadius = context?.resources?.getDimension(R.dimen.rounded_corner) ?: 1f

        with(binding) {
            title.text = "This Jacket That Makes You Cooler Than the Weather"
            description.text = "Step into ultimate style and comfort with this versatile jacket. Whether you're braving chilly winds or just pretending it’s cold enough to look this good, this jacket's got you covered—literally. Bonus: It has pockets. Yes, REAL ones. \uD83D\uDD25"
            price.text = "$ 12.90"
            rating.text = "4.5 (340 ratings)"
            photo.load("https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg") {
                transformations(RoundedCornersTransformation(cornerRadius))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}