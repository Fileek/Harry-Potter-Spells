package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SwitchSourceAction.ToRemoteAction
import com.epam.harrypotterspells.feature.main.MainResult.SwitchSourceResult.ToRemoteResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SwitchToRemoteUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: SwitchToRemoteUseCase
    private lateinit var actionComposer: ActionComposer<ToRemoteAction, ToRemoteResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SwitchToRemoteUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that SwitchToRemoteAction calls switchToRemote on the repository`() {
        actionComposer(ToRemoteAction)
        verify { repository.switchToRemote() }
    }

    @Test
    fun `check that SwitchToRemoteAction returns SwitchToRemoteResult`() {
        val testObserver = actionComposer(ToRemoteAction)
        testObserver.assertValue(ToRemoteResult)
    }
}