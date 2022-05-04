package com.epam.harrypotterspells.features.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.epam.harrypotterspells.R
import com.epam.harrypotterspells.databinding.FragmentDetailsBinding
import com.epam.harrypotterspells.ext.focusAndShowKeyboard
import com.epam.harrypotterspells.ext.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding not initialized" }
    private val viewModel: DetailsViewModel by viewModels()
    private val spell get() = viewModel.spell

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        renderSpell()
        renderInputState()
        setListeners()
    }

    private fun renderSpell() = binding.run {
        name.text = spell.name
        type.text = spell.type
        effect.text = spell.effect
        incantation.text = spell.incantation
        light.text = spell.light
        creator.text = spell.creator
        canBeVerbal.text = spell.canBeVerbal
    }

    private fun renderInputState() {
        if (viewModel.state.incantationIsEditing) showIncantationInput()
        if (viewModel.state.typeIsEditing) showTypeInput()
        if (viewModel.state.effectIsEditing) showEffectInput()
        if (viewModel.state.lightIsEditing) showLightInput()
        if (viewModel.state.creatorIsEditing) showCreatorInput()
    }

    private fun setListeners() = binding.run {
        setEditButtonsListeners()
        setSaveButtonsListeners()
    }

    private fun showIncantationInput() = binding.run {
        incantationGroup.visibility = View.GONE
        incantationInputGroup.visibility = View.VISIBLE
        incantationInput.setText(viewModel.spell.incantation)
        viewModel.state.incantationIsEditing = true
    }

    private fun showTypeInput() = binding.run {
        typeGroup.visibility = View.GONE
        typeInputGroup.visibility = View.VISIBLE
        typeInput.setText(spell.type)
        viewModel.state.typeIsEditing = true
    }

    private fun showEffectInput() = binding.run {
        effectGroup.visibility = View.GONE
        effectInputGroup.visibility = View.VISIBLE
        effectInput.setText(spell.effect)
        viewModel.state.effectIsEditing = true
    }

    private fun showLightInput() = binding.run {
        lightGroup.visibility = View.GONE
        lightInputGroup.visibility = View.VISIBLE
        lightInput.setText(spell.light)
        viewModel.state.lightIsEditing = true
    }

    private fun showCreatorInput() = binding.run {
        creatorGroup.visibility = View.GONE
        creatorInputGroup.visibility = View.VISIBLE
        creatorInput.setText(spell.creator)
        viewModel.state.creatorIsEditing = true
    }

    private fun setEditButtonsListeners() = binding.run {
        editIncantation.setOnClickListener(getEditIncantationListener())
        editType.setOnClickListener(getEditTypeListener())
        editEffect.setOnClickListener(getEditEffectListener())
        editLight.setOnClickListener(getEditLightListener())
        editCreator.setOnClickListener(getEditCreatorListener())
    }

    private fun setSaveButtonsListeners() = binding.run {
        saveIncantation.setOnClickListener(getSaveIncantationListener())
        saveType.setOnClickListener(getSaveTypeListener())
        saveEffect.setOnClickListener(getSaveEffectListener())
        saveLight.setOnClickListener(getSaveLightListener())
        saveCreator.setOnClickListener(getSaveCreatorListener())
    }

    private fun getEditIncantationListener() = View.OnClickListener {
        showIncantationInput()
        focusOnIncantationInput()
    }

    private fun getEditTypeListener() = View.OnClickListener {
        showTypeInput()
        focusOnTypeInput()
    }

    private fun getEditEffectListener() = View.OnClickListener {
        showEffectInput()
        focusOnEffectInput()
    }

    private fun getEditLightListener() = View.OnClickListener {
        showLightInput()
        focusOnLightInput()
    }

    private fun getEditCreatorListener() = View.OnClickListener {
        showCreatorInput()
        focusOnCreatorInput()
    }

    private fun getSaveIncantationListener() = View.OnClickListener {
        hideIncantationInputAndKeyboard()
        saveAndUpdateIncantation()
    }

    private fun getSaveTypeListener() = View.OnClickListener {
        hideTypeInputAndKeyboard()
        saveAndUpdateType()
    }

    private fun getSaveEffectListener() = View.OnClickListener {
        hideEffectInputAndKeyboard()
        saveAndUpdateEffect()
    }

    private fun getSaveLightListener() = View.OnClickListener {
        hideLightInputAndKeyboard()
        saveAndUpdateLight()
    }

    private fun getSaveCreatorListener() = View.OnClickListener {
        hideCreatorInputAndKeyboard()
        saveAndUpdateCreator()
    }

    private fun focusOnIncantationInput() = binding.incantationInput.run {
        focusAndShowKeyboard()
        setSelection(spell.incantation.length)
    }

    private fun focusOnTypeInput() = binding.typeInput.run {
        focusAndShowKeyboard()
        setSelection(spell.type.length)
    }

    private fun focusOnEffectInput() = binding.effectInput.run {
        focusAndShowKeyboard()
        setSelection(spell.effect.length)
    }

    private fun focusOnLightInput() = binding.lightInput.run {
        focusAndShowKeyboard()
        setSelection(spell.light.length)
    }

    private fun focusOnCreatorInput() = binding.creatorInput.run {
        focusAndShowKeyboard()
        setSelection(spell.creator.length)
    }

    private fun hideIncantationInputAndKeyboard() = binding.run {
        incantationGroup.visibility = View.VISIBLE
        incantationInputGroup.visibility = View.GONE
        incantationInput.hideKeyboard()
        viewModel.state.incantationIsEditing = false
    }

    private fun hideTypeInputAndKeyboard() = binding.run {
        typeGroup.visibility = View.VISIBLE
        typeInputGroup.visibility = View.GONE
        typeInput.hideKeyboard()
        viewModel.state.typeIsEditing = false
    }

    private fun hideEffectInputAndKeyboard() = binding.run {
        effectGroup.visibility = View.VISIBLE
        effectInputGroup.visibility = View.GONE
        effectInput.hideKeyboard()
        viewModel.state.effectIsEditing = false
    }

    private fun hideLightInputAndKeyboard() = binding.run {
        lightGroup.visibility = View.VISIBLE
        lightInputGroup.visibility = View.GONE
        lightInput.hideKeyboard()
        viewModel.state.lightIsEditing = false
    }

    private fun hideCreatorInputAndKeyboard() = binding.run {
        creatorGroup.visibility = View.VISIBLE
        creatorInputGroup.visibility = View.GONE
        creatorInput.hideKeyboard()
        viewModel.state.creatorIsEditing = false
    }

    private fun saveAndUpdateIncantation() = binding.run {
        val newIncantation = incantationInput.text.toString()
        viewModel.updateSpell(spell.copy(incantation = newIncantation))
        incantation.text = newIncantation
    }

    private fun saveAndUpdateType() = binding.run {
        val newType = typeInput.text.toString()
        viewModel.updateSpell(spell.copy(type = newType))
        type.text = newType
    }

    private fun saveAndUpdateEffect() = binding.run {
        val newEffect = effectInput.text.toString()
        viewModel.updateSpell(spell.copy(effect = newEffect))
        effect.text = newEffect
    }

    private fun saveAndUpdateLight() = binding.run {
        val newLight = lightInput.text.toString()
        viewModel.updateSpell(spell.copy(light = newLight))
        light.text = newLight
    }

    private fun saveAndUpdateCreator() = binding.run {
        val newCreator = creatorInput.text.toString()
        viewModel.updateSpell(spell.copy(creator = newCreator))
        creator.text = newCreator
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}