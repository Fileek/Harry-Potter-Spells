package com.epam.harrypotterspells.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.epam.harrypotterspells.network.SpellApi
import com.epam.harrypotterspells.network.SpellApiImpl
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.FragmentMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = SpellAdapter()
        binding.list.adapter = adapter

        val api: SpellApi = SpellApiImpl

        api.getSpells()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                adapter.submitList(it)
            }
    }
}