package com.example.notesappmongodbnode.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notesappmongodbnode.R
import com.example.notesappmongodbnode.databinding.FragmentNoteBinding
import com.example.notesappmongodbnode.model.NoteRequest
import com.example.notesappmongodbnode.model.NoteResponse
import com.example.notesappmongodbnode.utils.NetworkResult
import com.example.notesappmongodbnode.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<NoteFragmentArgs>()
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val notes = args.note

            if (notes == null) {
                binding.apply {
                    addEditText.text = "Update Note"
                    btnSubmit.setOnClickListener {
                        val title = txtTitle.text.toString().toString()
                        val desc = txtDescription.text.toString().trim()
                        if (title.isNotEmpty() && desc.isNotEmpty()) {
                            noteViewModel.createNote(NoteRequest(desc, title))
                        } else {
                            Toast.makeText(requireContext(), "Field is empty", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            } else {
                binding.addEditText.text = "Update Note"
                binding.apply {
                    txtTitle.setText(notes.title)
                    txtDescription.setText(notes.description)

                    btnSubmit.setOnClickListener {
                        val title = txtTitle.text.toString().toString()
                        val desc = txtDescription.text.toString().trim()

                        if (title.isNotEmpty() && desc.isNotEmpty()) {
                            noteViewModel.updateNote(notes._id, NoteRequest(desc, title))
                        }
                    }
                }

                binding.btnDelete.setOnClickListener {
                    noteViewModel.deleteNote(notes._id)
                    findNavController().popBackStack()
                }
            }

        bindObserver()
    }

    private fun bindObserver() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}