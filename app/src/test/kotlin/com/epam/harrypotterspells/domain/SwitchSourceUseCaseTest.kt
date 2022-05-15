package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SwitchSourceAction
import com.epam.harrypotterspells.feature.main.MainResult.SwitchSourceResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SwitchSourceUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: SwitchSourceUseCase
    private lateinit var actionComposer: ActionComposer<SwitchSourceAction, SwitchSourceResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SwitchSourceUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that ToRemoteAction calls switchToRemote on the repository`() {
        actionComposer(SwitchSourceAction.ToRemoteAction)
        verify { repository.switchSourceToRemote() }
    }

    @Test
    fun `check that ToRemoteAction returns ToRemoteResult`() {
        val testObserver = actionComposer(SwitchSourceAction.ToRemoteAction)
        testObserver.assertValue(SwitchSourceResult.ToRemoteResult)
    }

    @Test
    fun `check that ToLocalAction calls switchToLocal on the repository`() {
        actionComposer(SwitchSourceAction.ToLocalAction)
        verify { repository.switchSourceToLocal() }
    }

    @Test
    fun `check that ToLocalAction returns SwitchToLocalResult`() {
        val testObserver = actionComposer(SwitchSourceAction.ToLocalAction)
        testObserver.assertValue(SwitchSourceResult.ToLocalResult)
    }
}