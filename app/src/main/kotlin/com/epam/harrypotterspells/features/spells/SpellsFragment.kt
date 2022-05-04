package com.epam.harrypotterspells.features.spells

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.epam.harrypotterspells.R
import com.epam.harrypotterspells.databinding.FragmentSpellsBinding
import com.epam.harrypotterspells.features.spells.adapter.SpellAdapter
import com.epam.harrypotterspells.mvibase.MVIView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

@AndroidEntryPoint
class SpellsFragment : Fragment(), MVIView<SpellsIntent, SpellsViewState> {

    private val spellAdapter = SpellAdapter()
    private var _binding: FragmentSpellsBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding not initialized" }
    private val viewModel: SpellsViewModel by viewModels()
    private val disposables = CompositeDisposable()
    private val errorStub by lazy { getString(R.string.error_placeholder) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpellsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter()
        bindViewModel()
    }

    private fun setAdapter() {
        binding.list.adapter = spellAdapter
    }

    private fun bindViewModel() {
        disposables += viewModel.states().subscribe(this::render)
        viewModel.processIntents(intents())
    }

    override fun render(state: SpellsViewState) {
        binding.progressBar.visibility =
            if (state.isLoading) View.VISIBLE
            else View.GONE
        if (state is SpellsViewState.Success) spellAdapter.submitList(state.spells)
        else if (state is SpellsViewState.Error) showError(state.error)
    }

    override fun intents(): Observable<SpellsIntent> {
        return loadIntent()
    }

    private fun showError(error: Throwable?) = binding.errorMessage.run {
        visibility = View.VISIBLE
        text = error?.message ?: errorStub
    }

    private fun loadIntent(): Observable<SpellsIntent> {
        return Observable.just(SpellsIntent.LoadSpellsIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        _binding = null
    }
}