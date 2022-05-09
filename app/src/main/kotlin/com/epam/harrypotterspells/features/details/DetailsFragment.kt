package com.epam.harrypotterspells.features.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.epam.harrypotterspells.databinding.FragmentDetailsBinding
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.utils.focusAndShowKeyboard
import com.epam.harrypotterspells.utils.hideKeyboard
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditCreatorIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditEffectIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditIncantationIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditLightIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.EditSpellIntent.EditTypeIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateCreatorIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateEffectIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateIncantationIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateLightIntent
import com.epam.harrypotterspells.features.details.DetailsIntent.UpdateSpellIntent.UpdateTypeIntent
import com.epam.harrypotterspells.mvibase.MVIView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.PublishSubject

@AndroidEntryPoint
class DetailsFragment : Fragment(), MVIView<DetailsIntent, DetailsViewState> {

    private val editIntentsSubject = PublishSubject.create<EditSpellIntent>()
    private val updateIntentsSubject = PublishSubject.create<UpdateSpellIntent>()
    private val args: DetailsFragmentArgs by navArgs()
    private val spellId by lazy { args.spell.id }
    private val disposables = CompositeDisposable()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is not initialized" }
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindViewModel()
        setListeners()
    }

    private fun bindViewModel() {
        viewModel.processIntents(getIntents())
        disposables += viewModel.getStates().subscribe(this::render)
    }

    override fun getIntents(): Observable<DetailsIntent> {
        return Observable.merge(
            getEditSpellIntents(),
            getUpdateSpellIntents(),
        )
    }

    private fun getEditSpellIntents() = editIntentsSubject.serialize()

    private fun getUpdateSpellIntents() = updateIntentsSubject.serialize()

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

    private fun renderFocus(focus: SpellInputFieldFocus) = binding.run {
        when (focus) {
            SpellInputFieldFocus.NONE -> root.hideKeyboard()
            SpellInputFieldFocus.INCANTATION -> incantationInput.focusAndShowKeyboard()
            SpellInputFieldFocus.TYPE -> typeInput.focusAndShowKeyboard()
            SpellInputFieldFocus.EFFECT -> effectInput.focusAndShowKeyboard()
            SpellInputFieldFocus.LIGHT -> lightInput.focusAndShowKeyboard()
            SpellInputFieldFocus.CREATOR -> creatorInput.focusAndShowKeyboard()
        }
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
        editIntentsSubject.onNext(EditIncantationIntent)
    }

    private fun getEditTypeListener() = View.OnClickListener {
        editIntentsSubject.onNext(EditTypeIntent)
    }

    private fun getEditEffectListener() = View.OnClickListener {
        editIntentsSubject.onNext(EditEffectIntent)
    }

    private fun getEditLightListener() = View.OnClickListener {
        editIntentsSubject.onNext(EditLightIntent)
    }

    private fun getEditCreatorListener() = View.OnClickListener {
        editIntentsSubject.onNext(EditCreatorIntent)
    }

    private fun getSaveIncantationListener() = View.OnClickListener {
        val incantation = binding.incantationInput.text.toString()
        val intent = UpdateIncantationIntent(spellId, incantation)
        updateIntentsSubject.onNext(intent)
    }

    private fun getSaveTypeListener() = View.OnClickListener {
        val type = binding.typeInput.text.toString()
        val intent = UpdateTypeIntent(spellId, type)
        updateIntentsSubject.onNext(intent)
    }

    private fun getSaveEffectListener() = View.OnClickListener {
        val effect = binding.effectInput.text.toString()
        val intent = UpdateEffectIntent(spellId, effect)
        updateIntentsSubject.onNext(intent)
    }

    private fun getSaveLightListener() = View.OnClickListener {
        val light = binding.lightInput.text.toString()
        val intent = UpdateLightIntent(spellId, light)
        updateIntentsSubject.onNext(intent)
    }

    private fun getSaveCreatorListener() = View.OnClickListener {
        val creator = binding.creatorInput.text.toString()
        val intent = UpdateCreatorIntent(spellId, creator)
        updateIntentsSubject.onNext(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        disposables.dispose()
    }
}