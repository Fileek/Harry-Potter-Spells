package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.main.MainAction.SwitchToLocalAction
import com.epam.harrypotterspells.main.MainResult.SwitchToLocalResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SwitchToLocalUseCaseTest {

    @MockK
    lateinit var repository: Repository

    private lateinit var useCase: SwitchToLocalUseCase
    private lateinit var actionComposer: ActionComposer<SwitchToLocalAction, SwitchToLocalResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SwitchToLocalUseCase(repository)
        actionComposer = ActionComposer(useCase)
    }

    @Test
    fun `check that SwitchToLocalAction calls switchToLocal on the repository`() {
        actionComposer(SwitchToLocalAction)
        verify { repository.switchToLocal() }
    }

    @Test
    fun `check that SwitchToLocalAction returns SwitchToLocalResult`() {
        val testObserver = actionComposer(SwitchToLocalAction)
        testObserver.assertValue(SwitchToLocalResult)
    }
}