package com.epam.harrypotterspells.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.epam.harrypotterspells.list.ListViewModel
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args: DetailsFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: ListViewModel by activityViewModels()
    private val spell by lazy { args.spell }
    private val spellIncantationPlaceholder by lazy { getString(R.string.spell_incantation_placeholder) }
    private val spellCreatorPlaceholder by lazy { getString(R.string.spell_creator_placeholder) }
    private val canBeVerbal by lazy { getString(R.string.spell_can_be_verbal) }
    private val canNotBeVerbal by lazy { getString(R.string.spell_can_not_be_verbal) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateViews()
        setListeners()
    }

    private fun updateViews() = binding.apply {
        spellName.text = spell.name
        spellType.text = spell.type
        spellEffect.text = spell.effect
        spellIncantation.text = spell.incantation ?: spellIncantationPlaceholder
        spellLight.text = spell.light
        spellCreator.text = spell.creator ?: spellCreatorPlaceholder
        spellCanBeVerbal.text = if (spell.canBeVerbal == true) canBeVerbal else canNotBeVerbal
    }

    private fun setListeners() = binding.apply {
        editIncantationButton.setOnClickListener {
            editIncantation()
        }
        saveIncantationButton.setOnClickListener {
            saveIncantation()
        }
        editCreatorButton.setOnClickListener {
            editCreator()
        }
        saveCreatorButton.setOnClickListener {
            saveCreator()
        }
    }

    private fun editIncantation() = binding.let {
        hideViews(it.spellIncantation, it.editIncantationButton)
        it.spellIncantationInput.setText(spell.incantation ?: spellIncantationPlaceholder)
        showViews(it.spellIncantationInput, it.saveIncantationButton)
        it.spellIncantationInput.requestFocus()
    }

    private fun saveIncantation() = binding.let {
        spell.incantation = it.spellIncantationInput.text.toString()
        hideViews(it.spellIncantationInput, it.saveIncantationButton)
        showViews(it.spellIncantation, it.editIncantationButton)
        applyChanges()
    }

    private fun editCreator() = binding.let {
        hideViews(it.spellCreator, it.editCreatorButton)
        it.spellCreatorInput.setText(spell.creator ?: spellCreatorPlaceholder)
        showViews(it.spellCreatorInput, it.saveCreatorButton)
        it.spellIncantationInput.requestFocus()
    }

    private fun saveCreator() = binding.let {
        spell.creator = it.spellCreatorInput.text.toString()
        hideViews(it.spellCreatorInput, it.saveCreatorButton)
        showViews(it.spellCreator, it.editCreatorButton)
        applyChanges()
    }

    private fun hideViews(vararg views: View) {
        views.forEach {
            it.visibility = View.GONE
        }
    }

    private fun showViews(vararg views: View) {
        views.forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun applyChanges() {
        updateViews()
        viewModel.edit(spell)
    }
}