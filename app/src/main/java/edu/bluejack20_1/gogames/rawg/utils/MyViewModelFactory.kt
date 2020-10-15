package edu.bluejack20_1.gogames.rawg.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModelFactory <T>(val creator:()->T) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}