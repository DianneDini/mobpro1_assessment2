package org.d3if0108.mobpro1_assessment2.util

import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if0108.mobpro1_assessment2.database.UserDao
import org.d3if0108.mobpro1_assessment2.ui.screen.DetailViewModel
import org.d3if0108.mobpro1_assessment2.ui.screen.MainViewModel

class ViewModelFactory (
    private val dao: UserDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(dao) as T
        }
        throw illegalDecoyCallException("Unknown ViewModel class")
    }
}
