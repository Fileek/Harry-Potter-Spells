package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.main.MainAction.SwitchToRemoteAction
import com.epam.harrypotterspells.main.MainResult.SwitchToRemoteResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SwitchToRemoteUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: SwitchToRemoteUseCase
    private lateinit var actionComposer: ActionComposer<SwitchToRemoteAction, SwitchToRemoteResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SwitchToRemoteUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that SwitchToRemoteAction calls switchToRemote on the repository`() {
        actionComposer(SwitchToRemoteAction)
        verify { repository.switchToRemote() }
    }

    @Test
    fun `check that SwitchToRemoteAction returns SwitchToRemoteResult`() {
        val testObserver = actionComposer(SwitchToRemoteAction)
        testObserver.assertValue(SwitchToRemoteResult)
    }
}