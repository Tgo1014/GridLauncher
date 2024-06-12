package tgo1014.gridlauncher.domain.usecases

import javax.inject.Inject

class OpenNotificationShadeUseCase @Inject constructor() {

    operator fun invoke() = runCatching {
        Runtime.getRuntime().exec("service call statusbar 1") // 2 for collapse
    }

}