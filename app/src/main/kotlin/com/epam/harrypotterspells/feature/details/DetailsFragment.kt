package com.epam.harrypotterspells.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.epam.harrypotterspells.databinding.FragmentDetailsBinding
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.details.DetailsIntent.EditSpellFieldIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.FocusOnFieldIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.SaveSpellFieldIntent
import com.epam.harrypotterspells.mvibase.MVIView
import com.epam.harrypotterspells.util.extension.focusAndShowKeyboard
import com.epam.harrypotterspells.util.extension.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.PublishSubject

@AndroidEntryPoint
class DetailsFragment : Fragment(), MVIView<DetailsIntent, DetailsViewState> {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is not initialized" }

    private var _spell: Spell? = null
    private val spell get() = checkNotNull(_spell) { "Spell is not initialized" }

    private val viewModel: DetailsViewModel by viewModels()
    private val disposables = CompositeDisposable()
    private val intentsSubject = PublishSubject.create<DetailsIntent>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        provideIntents()
        getStates()
    }

    private fun setListeners() = binding.run {
        setEditButtonsListeners()
        setFocusListeners()
        setSaveButtonsListeners()
    }

    private fun setEditButtonsListeners() = binding.run {
        editIncantation.setOnClickListener { sendEditSpellFieldIntent(SpellField.INCANTATION) }
        editType.setOnClickListener { sendEditSpellFieldIntent(SpellField.TYPE) }
        editEffect.setOnClickListener { sendEditSpellFieldIntent(SpellField.EFFECT) }
        editLight.setOnClickListener { sendEditSpellFieldIntent(SpellField.LIGHT) }
        editCreator.setOnClickListener { sendEditSpellFieldIntent(SpellField.CREATOR) }
    }

    private fun sendEditSpellFieldIntent(field: SpellField) {
        intentsSubject.onNext(EditSpellFieldIntent(field))
    }

    private fun setFocusListeners() = binding.run {
        incantationInput.onFocusChangeListener = getOnFocusChangeListener(SpellField.INCANTATION)
        typeInput.onFocusChangeListener = getOnFocusChangeListener(SpellField.TYPE)
        effectInput.onFocusChangeListener = getOnFocusChangeListener(SpellField.EFFECT)
        lightInput.onFocusChangeListener = getOnFocusChangeListener(SpellField.LIGHT)
        creatorInput.onFocusChangeListener = getOnFocusChangeListener(SpellField.CREATOR)
    }

    private fun getOnFocusChangeListener(field: SpellField) = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) intentsSubject.onNext(FocusOnFieldIntent(field))
    }

    private fun setSaveButtonsListeners() = binding.run {
        saveIncantation.setOnClickListener(getSaveIncantationListener())
        saveType.setOnClickListener(getSaveTypeListener())
        saveEffect.setOnClickListener(getSaveEffectListener())
        saveLight.setOnClickListener(getSaveLightListener())
        saveCreator.setOnClickListener(getSaveCreatorListener())
    }

    private fun getSaveIncantationListener() = View.OnClickListener {
        val newSpell = spell.copy(incantation = binding.incantationInput.text.toString())
        intentsSubject.onNext(SaveSpellFieldIntent(newSpell, SpellField.INCANTATION))
    }

    private fun getSaveTypeListener() = View.OnClickListener {
        val newSpell = spell.copy(type = binding.typeInput.text.toString())
        intentsSubject.onNext(SaveSpellFieldIntent(newSpell, SpellField.TYPE))
    }

    private fun getSaveEffectListener() = View.OnClickListener {
        val newSpell = spell.copy(effect = binding.effectInput.text.toString())
        intentsSubject.onNext(SaveSpellFieldIntent(newSpell, SpellField.EFFECT))
    }

    private fun getSaveLightListener() = View.OnClickListener {
        val newSpell = spell.copy(light = binding.lightInput.text.toString())
        intentsSubject.onNext(SaveSpellFieldIntent(newSpell, SpellField.LIGHT))
    }

    private fun getSaveCreatorListener() = View.OnClickListener {
        val newSpell = spell.copy(creator = binding.creatorInput.text.toString())
        intentsSubject.onNext(SaveSpellFieldIntent(newSpell, SpellField.CREATOR))
    }

    private fun provideIntents() = viewModel.processIntents(getIntents())

    private fun getStates() {
        disposables += viewModel.getStates().subscribe(this::render)
    }

    override fun getIntents(): Observable<DetailsIntent> = intentsSubject.serialize()

    override fun render(state: DetailsViewState) {
        state.spell?.let { renderSpell(it) }
        renderInputState(state)
        renderFocus(state.focus)
    }

    private fun renderSpell(newSpell: Spell) = binding.run {
        _spell = newSpell
        name.text = newSpell.name
        type.text = newSpell.type
        effect.text = newSpell.effect
        incantation.text = newSpell.incantation
        light.text = newSpell.light
        creator.text = newSpell.creator
        canBeVerbal.text = newSpell.canBeVerbal
    }

    private fun renderInputState(state: DetailsViewState) {
        if (state.editTextsNotSet) state.spell?.let { setTextInInputs(it) }

        renderIncantationGroups(state.fieldsNowEditing.contains(SpellField.INCANTATION))
        renderTypeGroups(state.fieldsNowEditing.contains(SpellField.TYPE))
        renderEffectGroups(state.fieldsNowEditing.contains(SpellField.EFFECT))
        renderLightGroups(state.fieldsNowEditing.contains(SpellField.LIGHT))
        renderCreatorGroups(state.fieldsNowEditing.contains(SpellField.CREATOR))
    }

    private fun setTextInInputs(spell: Spell) = binding.run {
        incantationInput.setText(spell.incantation)
        typeInput.setText(spell.type)
        effectInput.setText(spell.effect)
        lightInput.setText(spell.light)
        creatorInput.setText(spell.creator)
    }

    private fun renderIncantationGroups(isEditing: Boolean) = binding.run {
        incantationGroup.isGone = isEditing
        incantationInputGroup.isVisible = isEditing
    }

    private fun renderTypeGroups(isEditing: Boolean) = binding.run {
        typeGroup.isGone = isEditing
        typeInputGroup.isVisible = isEditing
    }

    private fun renderEffectGroups(isEditing: Boolean) = binding.run {
        effectGroup.isGone = isEditing
        effectInputGroup.isVisible = isEditing
    }

    private fun renderLightGroups(isEditing: Boolean) = binding.run {
        lightGroup.isGone = isEditing
        lightInputGroup.isVisible = isEditing
    }

    private fun renderCreatorGroups(isEditing: Boolean) = binding.run {
        creatorGroup.isGone = isEditing
        creatorInputGroup.isVisible = isEditing
    }

    private fun renderFocus(focus: SpellField?) = binding.run {
        when (focus) {
            SpellField.INCANTATION -> incantationInput.focusAndShowKeyboard()
            SpellField.TYPE -> typeInput.focusAndShowKeyboard()
            SpellField.EFFECT -> effectInput.focusAndShowKeyboard()
            SpellField.LIGHT -> lightInput.focusAndShowKeyboard()
            SpellField.CREATOR -> creatorInput.focusAndShowKeyboard()
            null -> root.hideKeyboard()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _spell = null
        disposables.dispose()
    }
}