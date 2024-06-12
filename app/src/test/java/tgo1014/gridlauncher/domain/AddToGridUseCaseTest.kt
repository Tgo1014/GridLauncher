package tgo1014.gridlauncher.domain

import org.junit.Before
import org.junit.Test
import tgo1014.gridlauncher.FakeAppsManager
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.domain.usecases.AddToGridUseCase
import tgo1014.gridlauncher.runTurbineTest
import tgo1014.gridlauncher.ui.models.GridItem

class AddToGridUseCaseTest {

    lateinit var appsManager: FakeAppsManager
    lateinit var useCase: AddToGridUseCase

    @Before
    fun init() {
        appsManager = FakeAppsManager()
        useCase = AddToGridUseCase(appsManager)
    }

    @Test
    fun insertWhenGridIsEmpty() = runTurbineTest {
        val gridFlow = appsManager.homeGridFlow.testIn(it.backgroundScope)
        assert(gridFlow.awaitItem().isEmpty())
        useCase(App())
        assert(gridFlow.awaitItem().isNotEmpty())
    }

    @Test
    fun findSlotToFindTileWhenRowIsFull() = runTurbineTest {
        val gridFlow = appsManager.homeGridFlow.testIn(it.backgroundScope)
        val startGrid = listOf(
            GridItem(app = App(), width = 2, height = 2, x = 0, y = 0),
            GridItem(app = App(), width = 2, height = 2, x = 2, y = 0),
            GridItem(app = App(), width = 2, height = 2, x = 4, y = 0),
        )
        appsManager.setGrid(startGrid)
        assert(gridFlow.awaitItem().isEmpty())
        useCase(App())
        val newGrid = gridFlow.expectMostRecentItem()
        assert(newGrid.find { it.y == 2 } != null)
    }

    @Test
    fun findSlotToFindTile() = runTurbineTest {
        val gridFlow = appsManager.homeGridFlow.testIn(it.backgroundScope)
        val startGrid = listOf(
            GridItem(app = App(), width = 2, height = 2, x = 0, y = 0),
            GridItem(app = App(), width = 2, height = 2, x = 5, y = 0)
        )
        appsManager.setGrid(startGrid)
        assert(gridFlow.awaitItem().isEmpty())
        useCase(App())
        val newGrid = gridFlow.expectMostRecentItem()
        assert(newGrid.find { it.x == 2 } != null)
    }

}