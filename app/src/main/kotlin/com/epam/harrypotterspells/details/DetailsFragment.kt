package com.epam.harrypotterspells.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.harrypotterspells.R
import com.example.harrypotterspells.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args: DetailsFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spell = args.spell
        binding.apply {
            spellName.text = spell.name
            spellType.text = spell.type
            spellEffect.text = spell.effect
            spellIncantation.text = spell.incantation ?: "Unknown"
        }
    }
}