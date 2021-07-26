package com.dj.chatapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.dj.chatapp.BaseFragment
import com.dj.chatapp.R
import com.dj.chatapp.databinding.FragmentLoginBinding
import com.dj.chatapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            setupConnectingUiState()
            viewModel.connectUser(userName = binding.etUsername.text.toString())
        }
        binding.etUsername.addTextChangedListener {
            binding.etUsername.error = null
        }
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.onEach { event ->
                when (event) {
                    is LoginEvent.ErrorInputTooShort -> {
                        setupIdleUiState()
                        binding.etUsername.error = getString(R.string.error_username_too_short,
                            Constants.MIN_USERNAME_LENGTH)
                    }
                    is LoginEvent.ErrorLogin -> {
                        setupIdleUiState()
                        Toast.makeText(requireContext(), event.error, Toast.LENGTH_LONG).show()
                    }
                    is LoginEvent.Success -> {
                        setupIdleUiState()
                        Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setupConnectingUiState() {
        binding.progressBar.isVisible = true
        binding.btnConfirm.isEnabled = false
    }

    private fun setupIdleUiState() {
        binding.progressBar.isVisible = false
        binding.btnConfirm.isEnabled = true
    }
}