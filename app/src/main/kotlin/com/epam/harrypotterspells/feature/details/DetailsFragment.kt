package com.epam.harrypotterspells.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.epam.harrypotterspells.databinding.FragmentDetailsBinding
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.details.DetailsIntent.EditIntent
import com.epam.harrypotterspells.feature.details.DetailsIntent.UpdateIntent
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

    private val args: DetailsFragmentArgs by navArgs()
    private val spellId by lazy { args.spell.id }

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
        setSaveButtonsListeners()
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
        intentsSubject.onNext(EditIntent.IncantationIntent)
    }

    private fun getEditTypeListener() = View.OnClickListener {
        intentsSubject.onNext(EditIntent.TypeIntent)
    }

    private fun getEditEffectListener() = View.OnClickListener {
        intentsSubject.onNext(EditIntent.EffectIntent)
    }

    private fun getEditLightListener() = View.OnClickListener {
        intentsSubject.onNext(EditIntent.LightIntent)
    }

    private fun getEditCreatorListener() = View.OnClickListener {
        intentsSubject.onNext(EditIntent.CreatorIntent)
    }

    private fun getSaveIncantationListener() = View.OnClickListener {
        val incantation = binding.incantationInput.text.toString()
        val intent = UpdateIntent.IncantationIntent(spellId, incantation)
        intentsSubject.onNext(intent)
    }

    private fun getSaveTypeListener() = View.OnClickListener {
        val type = binding.typeInput.text.toString()
        val intent = UpdateIntent.TypeIntent(spellId, type)
        intentsSubject.onNext(intent)
    }

    private fun getSaveEffectListener() = View.OnClickListener {
        val effect = binding.effectInput.text.toString()
        val intent = UpdateIntent.EffectIntent(spellId, effect)
        intentsSubject.onNext(intent)
    }

    private fun getSaveLightListener() = View.OnClickListener {
        val light = binding.lightInput.text.toString()
        val intent = UpdateIntent.LightIntent(spellId, light)
        intentsSubject.onNext(intent)
    }

    private fun getSaveCreatorListener() = View.OnClickListener {
        val creator = binding.creatorInput.text.toString()
        val intent = UpdateIntent.CreatorIntent(spellId, creator)
        intentsSubject.onNext(intent)
    }

    private fun provideIntents() = viewModel.processIntents(getIntents())

    private fun getStates() {
        disposables += viewModel.getStates()
            .subscribe(this::render)
    }

    override fun getIntents(): Observable<DetailsIntent> = intentsSubject.serialize()

    override fun render(state: DetailsViewState) {
        state.spell?.let { renderSpell(it) }
        renderInputState(state)
        renderFocus(state.focus)
    }

    private fun renderSpell(spell: Spell) = binding.run {
        name.text = spell.name
        type.text = spell.type
        effect.text = spell.effect
        incantation.text = spell.incantation
        light.text = spell.light
        creator.text = spell.creator
        canBeVerbal.text = spell.canBeVerbal
    }

    private fun renderInputState(state: DetailsViewState) {
        if (state.inputsTextsNotSet) state.spell?.let { setTextInInputs(it) }

        renderIncantationGroups(state.incantationIsEditing)
        renderTypeGroups(state.typeIsEditing)
        renderEffectGroups(state.effectIsEditing)
        renderLightGroups(state.lightIsEditing)
        renderCreatorGroups(state.creatorIsEditing)
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

    private fun renderFocus(focus: SpellFieldFocus) = binding.run {
        when (focus) {
            SpellFieldFocus.NONE -> root.hideKeyboard()
            SpellFieldFocus.INCANTATION -> incantationInput.focusAndShowKeyboard()
            SpellFieldFocus.TYPE -> typeInput.focusAndShowKeyboard()
            SpellFieldFocus.EFFECT -> effectInput.focusAndShowKeyboard()
            SpellFieldFocus.LIGHT -> lightInput.focusAndShowKeyboard()
            SpellFieldFocus.CREATOR -> creatorInput.focusAndShowKeyboard()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        disposables.dispose()
    }
}