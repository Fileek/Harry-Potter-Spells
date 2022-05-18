package com.epam.harrypotterspells.feature.spells

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.epam.harrypotterspells.R
import com.epam.harrypotterspells.databinding.FragmentSpellsBinding
import com.epam.harrypotterspells.feature.spells.adapter.SpellAdapter
import com.epam.harrypotterspells.mvibase.MVIView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

@AndroidEntryPoint
class SpellsFragment : Fragment(), MVIView<SpellsIntent, SpellsViewState> {

    private var _binding: FragmentSpellsBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is not initialized" }

    private val spellAdapter = SpellAdapter()

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
        viewModel.processIntents(getIntents())
        disposables += viewModel.getStates()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render)
    }

    override fun getIntents(): Observable<SpellsIntent> {
        return Observable.just(SpellsIntent.LoadIntent)
    }

    override fun render(state: SpellsViewState) {
        binding.progressBar.visibility =
            if (state.isLoading) View.VISIBLE
            else View.GONE
        if (state is SpellsViewState.Success) spellAdapter.submitList(state.spells)
        else if (state is SpellsViewState.Error) showError(state.error)
    }

    private fun showError(error: Throwable?) = binding.errorMessage.run {
        visibility = View.VISIBLE
        text = error?.message ?: errorStub
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        disposables.dispose()
    }
}