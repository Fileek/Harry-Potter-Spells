package com.epam.harrypotterspells.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.epam.harrypotterspells.ext.focusAndShowKeyboard
import com.epam.harrypotterspells.ext.hideKeyboard
import com.epam.harrypotterspells.ext.toSpellColor
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding? = null
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val spell get() = viewModel.getSpellById(args.spellId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindBinding(view)
        initViews()
        setListeners()
        render(viewModel.state)
    }

    private fun bindBinding(view: View) {
        binding = FragmentDetailsBinding.bind(view)
    }

    private fun initViews() = binding?.run {
        name.text = spell.name
        type.text = spell.type
        effect.text = spell.effect
        incantation.text = spell.incantation
        light.text = spell.light.toString()
        creator.text = spell.creator
        canBeVerbal.text = spell.canBeVerbal
    }

    private fun setListeners() = binding?.run {
        editIncantation.setOnClickListener(getEditIncantationListener())
        editType.setOnClickListener(getEditTypeListener())
        editEffect.setOnClickListener(getEditEffectListener())
        editLight.setOnClickListener(getEditLightListener())
        editCreator.setOnClickListener(getEditCreatorListener())
        saveIncantation.setOnClickListener(getSaveIncantationListener())
        saveType.setOnClickListener(getSaveTypeListener())
        saveEffect.setOnClickListener(getSaveEffectListener())
        saveLight.setOnClickListener(getSaveLightListener())
        saveCreator.setOnClickListener(getSaveCreatorListener())
    }

    private fun getEditIncantationListener() = View.OnClickListener {
        setIncantationGroupInEditMode()
        binding?.incantationInput?.setSelection(spell.incantation.length)
        binding?.incantationInput?.focusAndShowKeyboard()
    }

    private fun getEditTypeListener() = View.OnClickListener {
        val typeInput = binding?.typeInput
        setTypeGroupInEditMode()
        typeInput?.setSelection(spell.type.length)
        typeInput?.focusAndShowKeyboard()
    }

    private fun getEditEffectListener() = View.OnClickListener {
        val effectInput = binding?.effectInput
        setEffectGroupInEditMode()
        effectInput?.setSelection(spell.effect.length)
        effectInput?.focusAndShowKeyboard()
    }

    private fun getEditLightListener() = View.OnClickListener {
        val lightInput = binding?.lightInput
        setLightGroupInEditMode()
        lightInput?.setSelection(spell.light.toString().length)
        lightInput?.focusAndShowKeyboard()
    }

    private fun getEditCreatorListener() = View.OnClickListener {
        val creatorInput = binding?.creatorInput
        setCreatorGroupInEditMode()
        creatorInput?.setSelection(spell.creator.length)
        creatorInput?.focusAndShowKeyboard()
    }

    private fun getSaveIncantationListener() = View.OnClickListener {
        binding?.let {
            val newSpell = spell.copy(incantation = it.incantationInput.text.toString())
            it.incantationGroup.visibility = View.VISIBLE
            it.incantationInputGroup.visibility = View.GONE
            it.incantationInput.hideKeyboard()
            viewModel.editSpell(newSpell)
            viewModel.state.incantationIsEditing = false
            it.incantation.text = newSpell.incantation
        }
    }

    private fun getSaveTypeListener() = View.OnClickListener {
        binding?.let {
            val newSpell = spell.copy(type = it.typeInput.text.toString())
            it.typeGroup.visibility = View.VISIBLE
            it.typeInputGroup.visibility = View.GONE
            it.typeInput.hideKeyboard()
            viewModel.editSpell(newSpell)
            viewModel.state.typeIsEditing = false
            it.type.text = newSpell.type
        }
    }

    private fun getSaveEffectListener() = View.OnClickListener {
        binding?.let {
            val newSpell = spell.copy(effect = it.effectInput.text.toString())
            it.effectGroup.visibility = View.VISIBLE
            it.effectInputGroup.visibility = View.GONE
            it.effectInput.hideKeyboard()
            viewModel.editSpell(newSpell)
            viewModel.state.effectIsEditing = false
            it.effect.text = newSpell.effect
        }
    }

    private fun getSaveLightListener() = View.OnClickListener {
        binding?.let {
            val newSpell = spell.copy(light = it.lightInput.text.toString().toSpellColor())
            it.lightGroup.visibility = View.VISIBLE
            it.lightInputGroup.visibility = View.GONE
            it.lightInput.hideKeyboard()
            viewModel.editSpell(newSpell)
            viewModel.state.lightIsEditing = false
            it.light.text = newSpell.light.toString()
        }
    }

    private fun getSaveCreatorListener() = View.OnClickListener {
        binding?.let {
            val newSpell = spell.copy(creator = it.creatorInput.text.toString())
            it.creatorGroup.visibility = View.VISIBLE
            it.creatorInputGroup.visibility = View.GONE
            it.creatorInput.hideKeyboard()
            viewModel.editSpell(newSpell)
            viewModel.state.creatorIsEditing = false
            it.creator.text = newSpell.creator
        }
    }

    private fun render(state: DetailsViewState) {
        if (state.incantationIsEditing) setIncantationGroupInEditMode()
        if (state.typeIsEditing) setTypeGroupInEditMode()
        if (state.effectIsEditing) setEffectGroupInEditMode()
        if (state.lightIsEditing) setLightGroupInEditMode()
        if (state.creatorIsEditing) setCreatorGroupInEditMode()
    }

    private fun setIncantationGroupInEditMode() = binding?.run {
        incantationGroup.visibility = View.GONE
        incantationInputGroup.visibility = View.VISIBLE
        incantationInput.setText(spell.incantation)
        viewModel.state.incantationIsEditing = true
    }

    private fun setTypeGroupInEditMode() = binding?.run {
        typeGroup.visibility = View.GONE
        typeInputGroup.visibility = View.VISIBLE
        typeInput.setText(spell.type)
        viewModel.state.typeIsEditing = true
    }

    private fun setEffectGroupInEditMode() = binding?.run {
        effectGroup.visibility = View.GONE
        effectInputGroup.visibility = View.VISIBLE
        effectInput.setText(spell.effect)
        viewModel.state.effectIsEditing = true
    }

    private fun setLightGroupInEditMode() = binding?.run {
        lightGroup.visibility = View.GONE
        lightInputGroup.visibility = View.VISIBLE
        lightInput.setText(spell.light.toString())
        viewModel.state.lightIsEditing = true
    }

    private fun setCreatorGroupInEditMode() = binding?.run {
        creatorGroup.visibility = View.GONE
        creatorInputGroup.visibility = View.VISIBLE
        creatorInput.setText(spell.creator)
        viewModel.state.creatorIsEditing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}