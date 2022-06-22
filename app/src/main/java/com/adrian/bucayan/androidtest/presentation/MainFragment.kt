package com.adrian.bucayan.androidtest.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.bucayan.androidtest.R
import com.adrian.bucayan.androidtest.databinding.FragmentMainBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding
    private lateinit var toolbar: MaterialToolbar
    private val viewModel: MainViewModel by  viewModels()

    private var conversionAdapter : ConversionAdapter? = null
    private var mConversionList: List<String>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        toolbar = requireActivity().findViewById(R.id.topAppBar)
        toolbar.title = getString(R.string.app_name)

        initRecyclerview()

        binding?.searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.e("newText $newText")
                var filteredList : List<String>? = null
                if (newText != null) {
                    filteredList = mConversionList?.filter { it -> it.startsWith(newText, true)}
                }
                if (filteredList != null) {
                    Timber.e("filteredList size " + filteredList.size)
                    populateConversionAdapter(filteredList)
                }
                return false
            }
        })

        displayLoading(true)

        viewModel.getSelected().observe(viewLifecycleOwner) { conversionList ->
            displayLoading(false)
            mConversionList = conversionList
            populateConversionAdapter(conversionList)
        }

    }

    private fun initRecyclerview() {
        binding?.rvConversionRates?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            val itemDecoration: ConversionAdapter.TopSpacingDecoration =
                ConversionAdapter.TopSpacingDecoration(5)
            addItemDecoration(itemDecoration)

            conversionAdapter = ConversionAdapter()
            adapter = conversionAdapter
        }
    }

    private fun populateConversionAdapter(conversionList: List<String>?) {
        if(conversionList != null) {
            hideErrorText(true)
            Timber.e("conversionList size " + conversionList.size)
            conversionAdapter!!.submitList(conversionList)
        } else {
            hideErrorText(false)
        }
    }

    private fun displayLoading(isDisplayed: Boolean) {
        if (isDisplayed) {
            binding?.linearProgressBarLoadMore!!.visibility = View.VISIBLE
        } else {
            binding?.linearProgressBarLoadMore!!.visibility = View.GONE
        }
    }

    private fun hideErrorText(isTrue: Boolean) {
        if (isTrue) {
            binding?.rvConversionRates!!.visibility = View.VISIBLE
            binding?.tvFailToDownload!!.visibility = View.GONE
        } else {
            binding?.rvConversionRates!!.visibility = View.GONE
            binding?.tvFailToDownload!!.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}