package com.epam.harrypotterspells.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.epam.harrypotterspells.databinding.FragmentDetailsBinding
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.details.DetailsIntent.EditSpellFieldIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.FocusOnFieldIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.SaveSpellFieldIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent
import com.epam.harrypotterspells.feature.spells.SpellsViewModel
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

    private val spellsViewModel: SpellsViewModel by activityViewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()
    private val disposables = CompositeDisposable()
    private val intentsSubject = PublishSubject.create<DetailsIntent>()
    private val spellsIntentsSubject = PublishSubject.create<SpellsIntent>()

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
        editableIncantation.onFocusChangeListener = getOnFocusChangeListener(SpellField.INCANTATION)
        editableType.onFocusChangeListener = getOnFocusChangeListener(SpellField.TYPE)
        editableEffect.onFocusChangeListener = getOnFocusChangeListener(SpellField.EFFECT)
        editableLight.onFocusChangeListener = getOnFocusChangeListener(SpellField.LIGHT)
        editableCreator.onFocusChangeListener = getOnFocusChangeListener(SpellField.CREATOR)
    }

    private fun getOnFocusChangeListener(field: SpellField) =
        View.OnFocusChangeListener { _, hasFocus ->
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
        val newSpell = spell.copy(incantation = binding.editableIncantation.text.toString())
        sendSaveSpellIntents(newSpell, SpellField.INCANTATION)
    }

    private fun getSaveTypeListener() = View.OnClickListener {
        val newSpell = spell.copy(type = binding.editableType.text.toString())
        sendSaveSpellIntents(newSpell, SpellField.TYPE)
    }

    private fun getSaveEffectListener() = View.OnClickListener {
        val newSpell = spell.copy(effect = binding.editableEffect.text.toString())
        sendSaveSpellIntents(newSpell, SpellField.EFFECT)
    }

    private fun getSaveLightListener() = View.OnClickListener {
        val newSpell = spell.copy(light = binding.editableLight.text.toString())
        sendSaveSpellIntents(newSpell, SpellField.LIGHT)
    }

    private fun getSaveCreatorListener() = View.OnClickListener {
        val newSpell = spell.copy(creator = binding.editableCreator.text.toString())
        sendSaveSpellIntents(newSpell, SpellField.CREATOR)
    }

    private fun sendSaveSpellIntents(spell: Spell, field: SpellField) {
        intentsSubject.onNext(SaveSpellFieldIntent(spell, field))
        spellsIntentsSubject.onNext(SpellsIntent.SaveSpellIntent(spell))
    }

    private fun provideIntents() {
        detailsViewModel.processIntents(getIntents())
        spellsViewModel.processIntents(getSpellsIntents())
    }

    private fun getStates() {
        disposables += detailsViewModel.getStates().subscribe(this::render)
    }

    override fun getIntents(): Observable<DetailsIntent> = intentsSubject.serialize()

    private fun getSpellsIntents(): Observable<SpellsIntent> = spellsIntentsSubject.serialize()

    override fun render(state: DetailsViewState) {
        renderSpell(state.spell)
        renderEditableFields(state.editableFields)
        if (state.isInitial) renderSpellInEditableFields(state.spell)
        else renderFocus(state.focus)
    }

    private fun renderSpell(newSpell: Spell?) = binding.run {
        newSpell?.let {
            _spell = newSpell
            name.text = newSpell.name
            type.text = newSpell.type
            effect.text = newSpell.effect
            incantation.text = newSpell.incantation
            light.text = newSpell.light
            creator.text = newSpell.creator
            canBeVerbal.text = newSpell.canBeVerbal.toString()
        }
    }

    private fun renderEditableFields(fields: Set<SpellField>) {
        renderIncantationGroups(fields.contains(SpellField.INCANTATION))
        renderTypeGroups(fields.contains(SpellField.TYPE))
        renderEffectGroups(fields.contains(SpellField.EFFECT))
        renderLightGroups(fields.contains(SpellField.LIGHT))
        renderCreatorGroups(fields.contains(SpellField.CREATOR))
    }

    private fun renderSpellInEditableFields(newSpell: Spell?) = binding.run {
        newSpell?.let {
            editableIncantation.setText(newSpell.incantation)
            editableType.setText(newSpell.type)
            editableEffect.setText(newSpell.effect)
            editableLight.setText(newSpell.light)
            editableCreator.setText(newSpell.creator)
        }
    }

    private fun renderIncantationGroups(isEditing: Boolean) = binding.run {
        incantationGroup.isGone = isEditing
        editableIncantationGroup.isVisible = isEditing
    }

    private fun renderTypeGroups(isEditing: Boolean) = binding.run {
        typeGroup.isGone = isEditing
        editableTypeGroup.isVisible = isEditing
    }

    private fun renderEffectGroups(isEditing: Boolean) = binding.run {
        effectGroup.isGone = isEditing
        editableEffectGroup.isVisible = isEditing
    }

    private fun renderLightGroups(isEditing: Boolean) = binding.run {
        lightGroup.isGone = isEditing
        editableLightGroup.isVisible = isEditing
    }

    private fun renderCreatorGroups(isEditing: Boolean) = binding.run {
        creatorGroup.isGone = isEditing
        editableCreatorGroup.isVisible = isEditing
    }

    private fun renderFocus(focus: SpellField?) = binding.run {
        when (focus) {
            SpellField.INCANTATION -> editableIncantation.focusAndShowKeyboard()
            SpellField.TYPE -> editableType.focusAndShowKeyboard()
            SpellField.EFFECT -> editableEffect.focusAndShowKeyboard()
            SpellField.LIGHT -> editableLight.focusAndShowKeyboard()
            SpellField.CREATOR -> editableCreator.focusAndShowKeyboard()
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