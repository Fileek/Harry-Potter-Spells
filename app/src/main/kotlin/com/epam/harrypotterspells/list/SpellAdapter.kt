package com.epam.harrypotterspells.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.epam.harrypotterspells.network.Spell
import com.example.harrypotterspells.databinding.ItemSpellBinding

class SpellAdapter : ListAdapter<Spell, SpellViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        return createViewHolder(parent.context)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun createViewHolder(context: Context): SpellViewHolder {
        val binding = getBinding(context)
        val holder = SpellViewHolder(binding)
        setListener(holder)
        return holder
    }

    private fun getBinding(context: Context): ItemSpellBinding {
        val layoutInflater = LayoutInflater.from(context)
        return ItemSpellBinding.inflate(layoutInflater)
    }

    private fun setListener(holder: SpellViewHolder) {
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val spell = getItem(position)
            val action = ListFragmentDirections.detailsAction(spell)
            it.findNavController().navigate(action)
        }
    }

    companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Spell>() {
            override fun areItemsTheSame(oldItem: Spell, newItem: Spell) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Spell, newItem: Spell) =
                oldItem == newItem
        }
    }
}