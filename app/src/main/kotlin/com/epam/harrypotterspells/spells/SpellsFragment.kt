package com.epam.harrypotterspells.spells

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.epam.harrypotterspells.mvibase.MVIView
import com.epam.harrypotterspells.spells.adapter.SpellAdapter
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.FragmentSpellsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class SpellsFragment : Fragment(R.layout.fragment_spells), MVIView<SpellsIntent, SpellsViewState> {

    private val spellAdapter by lazy { SpellAdapter(requireContext()) }
    private var binding: FragmentSpellsBinding? = null
    private val viewModel: SpellsViewModel by viewModels()
    private val disposables = CompositeDisposable()
    private val errorStub by lazy { getString(R.string.error_placeholder) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindBinding(view)
        setAdapter()
        bindViewModel()
    }

    override fun onStop() {
        super.onStop()
        disposables.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun render(state: SpellsViewState) {
        binding?.progressBar?.visibility =
            if (state.isLoading) View.VISIBLE
            else View.GONE
        if (state is SpellsViewState.Success) spellAdapter.submitList(state.spells)
        else if (state is SpellsViewState.Error) showError(state.error)
    }

    override fun intents(): Observable<SpellsIntent> {
        return loadIntent().cast(SpellsIntent::class.java)
    }

    private fun bindBinding(view: View) {
        binding = FragmentSpellsBinding.bind(view)
    }

    private fun setAdapter() {
        binding?.list?.adapter = spellAdapter
    }

    private fun bindViewModel() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    private fun showError(error: Throwable?) = binding?.errorMessage?.run {
        visibility = View.VISIBLE
        text = error?.message ?: errorStub
    }

    private fun loadIntent(): Observable<SpellsIntent.LoadSpellsIntent> {
        return Observable.just(SpellsIntent.LoadSpellsIntent)
    }
}