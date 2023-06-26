package tgo1014.gridlauncher

import app.cash.turbine.TurbineContext
import app.cash.turbine.turbineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

fun runTurbineTest(testBody: suspend TurbineContext.(TestScope) -> Unit) {
    runTest {
        turbineScope { testBody(this@runTest) }
    }
}