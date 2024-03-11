package usecase

import repository.CardRepositoryInterface

class ToggleOnOffSyncUseCase(
    private val repository: CardRepositoryInterface
) {
    operator fun invoke(on: Boolean) {
        if (on) {
            repository.resumeCardSync()
        } else {
            repository.pauseCardSync()
        }
    }
}