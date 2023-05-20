package tgo1014.gridlauncher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.GetAppListUseCase
import tgo1014.gridlauncher.domain.models.App
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAppListUseCase: GetAppListUseCase,
    private val appsManager: AppsManager,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(HomeState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        init()
    }

    fun onOpenApp(app: App) {
        appsManager.openApp(app)
    }

    private fun init() = viewModelScope.launch {
        getAppListUseCase().collect { appList ->
            _stateFlow.update { it.copy(appList = appList) }
        }
    }

}