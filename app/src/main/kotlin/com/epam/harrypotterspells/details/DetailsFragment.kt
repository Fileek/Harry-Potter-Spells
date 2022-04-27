package com.epam.harrypotterspells.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.epam.harrypotterspells.ext.focusAndShowKeyboard
import com.epam.harrypotterspells.ext.hideKeyboard
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding? = null
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val spell get() = viewModel.getSpellById(args.spellId)

    private val incantationPlaceholder by lazy { getString(R.string.incantation_placeholder) }
    private val incantationString get() = spell.incantation ?: incantationPlaceholder
    private val creatorPlaceholder by lazy { getString(R.string.creator_placeholder) }
    private val creatorString get() = spell.creator ?: creatorPlaceholder
    private val canBeVerbalString by lazy { getString(R.string.can_be_verbal) }
    private val canNotBeVerbalString by lazy { getString(R.string.can_not_be_verbal) }
    private val canBeVerbalPlaceholder by lazy { getString(R.string.can_be_verbal_placeholder) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindBinding(view)
        initViews()
        setListeners()
        render(viewModel.state)
    }

    private fun bindBinding(view: View) {
        binding = FragmentDetailsBinding.bind(view)
    }

    private fun initViews() {
        binding?.apply {
            name.text = spell.name
            type.text = spell.type
            effect.text = spell.effect
            incantation.text = incantationString
            light.text = spell.light
            creator.text = creatorString
            canBeVerbal.text = when (spell.canBeVerbal) {
                true -> canBeVerbalString
                false -> canNotBeVerbalString
                null -> canBeVerbalPlaceholder
            }
        }
    }

    private fun setListeners() = binding?.apply {
        editIncantation.setOnClickListener {
            editIncantation()
        }
        saveIncantation.setOnClickListener {
            saveIncantation()
        }
        editCreator.setOnClickListener {
            editCreator()
        }
        saveCreator.setOnClickListener {
            saveCreator()
        }
    }

    private fun render(state: DetailsViewState) {
        binding?.let {
            if (state.incantationIsEditing) {
                setIncantationGroupInEditMode()
            }
            if (state.creatorIsEditing) {
                setCreatorGroupInEditMode()
            }
        }
    }

    private fun setIncantationGroupInEditMode() = binding?.let {
        it.incantationGroup.visibility = View.GONE
        it.incantationInputGroup.visibility = View.VISIBLE
        it.incantationInput.setText(incantationString)
        viewModel.state.incantationIsEditing = true
    }

    private fun setCreatorGroupInEditMode() = binding?.let {
        it.creatorGroup.visibility = View.GONE
        it.creatorInputGroup.visibility = View.VISIBLE
        it.creatorInput.setText(creatorString)
        viewModel.state.creatorIsEditing = true
    }

    private fun editIncantation() = binding?.incantationInput?.run {
        setIncantationGroupInEditMode()
        setSelection(incantationString.length)
        focusAndShowKeyboard()
    }

    private fun editCreator() = binding?.creatorInput?.run {
        setCreatorGroupInEditMode()
        setSelection(creatorString.length)
        focusAndShowKeyboard()
    }

    private fun saveIncantation() = binding?.let {
        val newSpell = spell.copy(incantation = it.incantationInput.text.toString())
        it.incantationGroup.visibility = View.VISIBLE
        it.incantationInputGroup.visibility = View.GONE
        it.incantationInput.hideKeyboard()
        viewModel.editSpell(newSpell)
        viewModel.state.incantationIsEditing = false
        it.incantation.text = newSpell.incantation
    }

    private fun saveCreator() = binding?.let {
        val newSpell = spell.copy(creator = it.creatorInput.text.toString())
        it.creatorInputGroup.visibility = View.GONE
        it.creatorGroup.visibility = View.VISIBLE
        it.creatorInput.hideKeyboard()
        viewModel.editSpell(newSpell)
        viewModel.state.creatorIsEditing = false
        it.creator.text = newSpell.creator
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}