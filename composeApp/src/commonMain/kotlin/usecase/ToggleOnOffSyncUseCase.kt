package usecase

import repository.CardRepository

class ToggleOnOffSyncUseCase(
    private val repository: CardRepository
) {
    operator fun invoke(on: Boolean) {
        if (on) {
            repository.resumeCardSync()
        } else {
            repository.pauseCardSync()
        }
    }
}