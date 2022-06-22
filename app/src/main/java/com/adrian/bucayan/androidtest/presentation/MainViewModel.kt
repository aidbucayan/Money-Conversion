package com.adrian.bucayan.androidtest.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.URL
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    init {
        kotlinx.coroutines.GlobalScope.launch {
            val url = URL(MainViewModel.url)
            val buffer = ByteArray(1024)
            var bufPos: Int
            val input = url.openConnection().getInputStream()
            val builder = StringBuilder("")

            while (input.read(buffer).also {
                    bufPos = it
                } != -1) builder.append(String(buffer, 0, bufPos))
            input.close()

            Timber.e("number = %s", builder.toString())

            val search = builder.toString().trim('\n').split("\n")
            Timber.e("search = %s", search.size)
            setSelected(search)
        }
    }

    private val conversionLiveData: MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    fun setSelected(conversionList: List<String>?) {
        conversionLiveData.postValue(conversionList!!)
    }

    fun getSelected(): MutableLiveData<List<String>> {
        return conversionLiveData
    }

    companion object {
        val url: String = "https://wm0.mobimate.com/content/worldmate/currencies/currency2008.dat"
    }
}