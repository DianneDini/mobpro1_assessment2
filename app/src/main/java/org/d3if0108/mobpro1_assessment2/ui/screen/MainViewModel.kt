package org.d3if0108.mobpro1_assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0108.mobpro1_assessment2.database.UserDao
import org.d3if0108.mobpro1_assessment2.model.User

class MainViewModel(dao: UserDao) : ViewModel() {

    val data: StateFlow<List<User>> = dao.getCatatan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}