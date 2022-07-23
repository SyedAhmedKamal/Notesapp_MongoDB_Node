package com.example.notesappmongodbnode.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappmongodbnode.databinding.NoteItemBinding
import com.example.notesappmongodbnode.model.NoteResponse

class NotesAdapter(private val onNoteClicked: (NoteResponse) -> Unit) : ListAdapter<NoteResponse, NotesAdapter.NoteViewHolder>(ComparatorDiffUtil()) {

    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(noteResponse: NoteResponse) {
            binding.title.text = noteResponse.title
            binding.desc.text = noteResponse.description
            binding.root.setOnClickListener {
                onNoteClicked(noteResponse)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse) =
            oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse) =
            oldItem == newItem
    }
}