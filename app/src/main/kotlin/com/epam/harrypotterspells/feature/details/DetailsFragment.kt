package com.epam.harrypotterspells.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        bindViewModel()
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

    private fun bindViewModel() {
        viewModel.processIntents(getIntents())
        disposables += viewModel.getStates().subscribe(this::render)
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
        if (state.inputsNotInitialized) state.spell?.let { initializeInputs(it) }

        if (state.incantationIsEditing) showIncantationInput()
        else hideIncantationInput()

        if (state.typeIsEditing) showTypeInput()
        else hideTypeInput()

        if (state.effectIsEditing) showEffectInput()
        else hideEffectInput()

        if (state.lightIsEditing) showLightInput()
        else hideLightInput()

        if (state.creatorIsEditing) showCreatorInput()
        else hideCreatorInput()
    }

    private fun initializeInputs(spell: Spell) = binding.run {
        incantationInput.setText(spell.incantation)
        typeInput.setText(spell.type)
        effectInput.setText(spell.effect)
        lightInput.setText(spell.light)
        creatorInput.setText(spell.creator)
    }

    private fun showIncantationInput() = binding.run {
        incantationGroup.visibility = View.GONE
        incantationInputGroup.visibility = View.VISIBLE
    }

    private fun showTypeInput() = binding.run {
        typeGroup.visibility = View.GONE
        typeInputGroup.visibility = View.VISIBLE
    }

    private fun showEffectInput() = binding.run {
        effectGroup.visibility = View.GONE
        effectInputGroup.visibility = View.VISIBLE
    }

    private fun showLightInput() = binding.run {
        lightGroup.visibility = View.GONE
        lightInputGroup.visibility = View.VISIBLE
    }

    private fun showCreatorInput() = binding.run {
        creatorGroup.visibility = View.GONE
        creatorInputGroup.visibility = View.VISIBLE
    }

    private fun hideIncantationInput() = binding.run {
        incantationGroup.visibility = View.VISIBLE
        incantationInputGroup.visibility = View.GONE
    }

    private fun hideTypeInput() = binding.run {
        typeGroup.visibility = View.VISIBLE
        typeInputGroup.visibility = View.GONE
    }

    private fun hideEffectInput() = binding.run {
        effectGroup.visibility = View.VISIBLE
        effectInputGroup.visibility = View.GONE
    }

    private fun hideLightInput() = binding.run {
        lightGroup.visibility = View.VISIBLE
        lightInputGroup.visibility = View.GONE
    }

    private fun hideCreatorInput() = binding.run {
        creatorGroup.visibility = View.VISIBLE
        creatorInputGroup.visibility = View.GONE
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