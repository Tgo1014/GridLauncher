package tgo1014.gridlauncher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.domain.AddToGridUseCase
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.GetAppListUseCase
import tgo1014.gridlauncher.domain.OpenNotificationShadeUseCase
import tgo1014.gridlauncher.domain.RemoveFromGridUseCase
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAppListUseCase: GetAppListUseCase,
    private val openNotificationShadeUseCase: OpenNotificationShadeUseCase,
    private val addToGridUseCase: AddToGridUseCase,
    private val removeFromGridUseCase: RemoveFromGridUseCase,
    private val appsManager: AppsManager,
) : ViewModel() {

    private var fullAppList: List<App> = emptyList()

    private val _stateFlow = MutableStateFlow(HomeState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        init()
    }

    fun onGoToHome() {
        _stateFlow.update { it.copy(goToHome = true) }
    }

    fun onOpenApp(app: App) {
        onGoToHome()
        appsManager.openApp(app)
    }

    fun onGridItemClicked(gridItem: GridItem) = viewModelScope.launch {
        if (!_stateFlow.value.isEditMode) {
            onOpenApp(gridItem.app)
        } else {
            removeFromGridUseCase(gridItem)
        }
    }

    fun onGridItemLongClicked(gridItem: GridItem) {
        val editMode = _stateFlow.value.isEditMode
        _stateFlow.update { it.copy(isEditMode = !editMode) }
    }

    fun openNotificationShade() {
        openNotificationShadeUseCase()
    }

    fun onSwitchedToHome() {
        _stateFlow.update { it.copy(goToHome = false) }
    }

    fun onAddToGrid(app: App) = viewModelScope.launch {
        addToGridUseCase(app)
    }

    fun uninstallApp(app: App) {
        appsManager.uninstallApp(app)
    }

    fun onFilterTextChanged(filter: String) {
        if (filter.isBlank()) {
            onFilterCleared()
            return
        }
        val appList = fullAppList.filter { it.name.contains(filter, true) }
        _stateFlow.update { it.copy(filterString = filter, appList = appList) }
    }

    fun onFilterCleared() {
        _stateFlow.update { it.copy(filterString = "", appList = fullAppList) }
    }

    private fun init() = viewModelScope.launch {
        getAppListUseCase()
            .onEach { appList ->
                fullAppList = appList
                _stateFlow.update { it.copy(appList = appList) }
            }
            .launchIn(this)
        appsManager.homeGridFlow
            .onEach { grid -> _stateFlow.update { it.copy(grid = grid) } }
            .launchIn(this)
    }

}