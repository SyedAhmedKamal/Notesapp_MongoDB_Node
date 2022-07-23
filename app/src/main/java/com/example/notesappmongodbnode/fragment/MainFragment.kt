package com.example.notesappmongodbnode.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesappmongodbnode.adapter.NotesAdapter
import com.example.notesappmongodbnode.databinding.FragmentMainBinding
import com.example.notesappmongodbnode.model.NoteResponse
import com.example.notesappmongodbnode.utils.NetworkResult
import com.example.notesappmongodbnode.utils.NetworkStatus
import com.example.notesappmongodbnode.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val TAG = "MainFragmentNOTES"

    @Inject
    lateinit var networkStatus: NetworkStatus

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesAdapter = NotesAdapter(::onNoteClicked)
        binding.apply {
            noteList.adapter = notesAdapter
            noteList.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        networkStatus.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.tvOfflineBanner.visibility = View.GONE
                    bindObserver()
                    noteViewModel.getNotes()
                }
                false -> {
                    binding.tvOfflineBanner.visibility = View.VISIBLE
                }
            }
        }

        binding.addNote.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToNoteFragment2(
                    null
                )
            )
        }

    }

    private fun bindObserver() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    notesAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun onNoteClicked(noteResponse: NoteResponse) {
        val directions = MainFragmentDirections.actionMainFragmentToNoteFragment2(noteResponse)
        findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}