package com.epam.harrypotterspells.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.epam.harrypotterspells.network.Spell
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.FragmentMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragment : Fragment(R.layout.fragment_main) {

    private val spellAdapter = SpellAdapter()
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: ListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        binding.list.adapter = spellAdapter

        viewModel.listState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                render(it)
            }
    }

    private fun render(state: ListState) {
        Log.v(TAG, "processViewState ${state.javaClass}")
        when (state) {
            is ListState.Empty -> {
                hideProgressBar()
            }
            is ListState.Loading -> {
                showProgressBar()
            }
            is ListState.Success -> {
                hideProgressBar()
                updateList(state.spells)
            }
            is ListState.Failure -> {
                hideProgressBar()
                showError(state.error.message ?: "Unknown error occured")
            }
        }
    }

    private fun updateList(spells: List<Spell>) {
        spellAdapter.submitList(spells)
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.errorMessage.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
}

const val TAG = "ListFragment"