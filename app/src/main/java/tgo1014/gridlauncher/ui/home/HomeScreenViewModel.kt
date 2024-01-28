package tgo1014.gridlauncher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.data.withoutAccents
import tgo1014.gridlauncher.domain.AddToGridUseCase
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.Direction
import tgo1014.gridlauncher.domain.GetAppListUseCase
import tgo1014.gridlauncher.domain.ItemGridSizeChangeUseCase
import tgo1014.gridlauncher.domain.MoveGridItemUseCase
import tgo1014.gridlauncher.domain.OpenNotificationShadeUseCase
import tgo1014.gridlauncher.domain.RemoveFromGridUseCase
import tgo1014.gridlauncher.domain.TileSize
import tgo1014.gridlauncher.domain.UpdateAppListUseCase
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAppListUseCase: GetAppListUseCase,
    private val openNotificationShadeUseCase: OpenNotificationShadeUseCase,
    private val addToGridUseCase: AddToGridUseCase,
    private val moveGridItemUseCase: MoveGridItemUseCase,
    private val removeFromGridUseCase: RemoveFromGridUseCase,
    private val itemGridSizeChangeUseCase: ItemGridSizeChangeUseCase,
    private val updateAppListUseCase: UpdateAppListUseCase,
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
        viewModelScope.launch {
            delay(200)
            _stateFlow.update { it.copy(goToHome = true) }
            resetState()
        }
        viewModelScope.launch { appsManager.openApp(app) }
    }

    fun onGridItemClicked(gridItem: GridItem) = viewModelScope.launch {
        when {
            !_stateFlow.value.isEditMode -> onOpenApp(gridItem.app)
//            gridItem == _stateFlow.value.itemBeingEdited -> {
//                removeFromGridUseCase(gridItem)
//                    .onSuccess { _stateFlow.update { it.copy(itemBeingEdited = null) } }
//            }
            else -> _stateFlow.update { it.copy(itemBeingEdited = gridItem) }
        }
    }

    fun onGridItemLongClicked(gridItem: GridItem) {
        _stateFlow.update { it.copy(itemBeingEdited = gridItem) }
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
        val appList = fullAppList.filter {
            it.name.withoutAccents.contains(filter.withoutAccents, true)
        }
        _stateFlow.update { it.copy(filterString = filter, appList = appList) }
    }

    fun onFilterCleared() {
        _stateFlow.update { it.copy(filterString = "", appList = fullAppList) }
    }

    fun onEditSheetDismissed() {
        _stateFlow.update { it.copy(itemBeingEdited = null) }
    }

    fun onItemMoved(direction: Direction) = viewModelScope.launch {
        val item = _stateFlow.value.itemBeingEdited ?: return@launch
        moveGridItemUseCase(item.id, direction)
    }

    fun onSizeChanged(tileSize: TileSize) = viewModelScope.launch {
        val item = _stateFlow.value.itemBeingEdited ?: return@launch
        itemGridSizeChangeUseCase(item.id, tileSize)
    }

    fun onRemoveClicked() = viewModelScope.launch {
        val item = _stateFlow.value.itemBeingEdited ?: return@launch
        removeFromGridUseCase(item)
            .onSuccess {
                _stateFlow.update { it.copy(itemBeingEdited = null) }
            }
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

    private fun resetState() {
        _stateFlow.update { it.copy(itemBeingEdited = null) }
        onFilterTextChanged("")
    }

}