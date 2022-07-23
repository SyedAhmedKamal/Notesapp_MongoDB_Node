package com.example.notesappmongodbnode.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notesappmongodbnode.databinding.FragmentRegisterBinding
import com.example.notesappmongodbnode.model.UserRequest
import com.example.notesappmongodbnode.utils.Constants.TAG
import com.example.notesappmongodbnode.utils.NetworkResult
import com.example.notesappmongodbnode.utils.TokenManager
import com.example.notesappmongodbnode.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        tokenManager.getToken()?.let {
            Log.d(TAG, "registration: $it")
        }
        if (tokenManager.getToken() != null) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainFragment2())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.registerUser(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnLogin.setOnClickListener {
            val directions = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(directions)
        }

        bindObserver()
    }

    private fun getUserRequest(): UserRequest {
        binding.apply {
            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()
            val userName = txtUsername.text.toString().trim()
            return UserRequest(email, password, userName)
        }
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password,
            false
        )
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    val directions =
                        RegisterFragmentDirections.actionRegisterFragmentToMainFragment2()
                    findNavController().navigate(directions)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}