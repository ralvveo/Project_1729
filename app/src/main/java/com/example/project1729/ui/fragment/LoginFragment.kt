package com.example.project1729.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentLoginBinding
import com.example.project1729.domain.model.UserLogin
import com.example.project1729.domain.state.TryLoginState
import com.example.project1729.ui.view_model.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButtonLogin.isEnabled = false

        binding.loginEnterLogin.doOnTextChanged { text, _, _, _ -> viewModel.changeLoginText(text.toString())  }
        binding.loginEnterPassword.doOnTextChanged { text, _, _, _ -> viewModel.changePasswordText(text.toString())  }

        viewModel.getLoginLiveData().observe(viewLifecycleOwner){state ->
            render(state)
        }

        viewModel.getLoginStateLiveData().observe(viewLifecycleOwner){loginState ->
            renderLogin(loginState)
        }

        binding.loginButtonLogin.setOnClickListener { viewModel.tryToLogin() }
    }

    private fun render(state: UserLogin){
        if (state.login.length >= 3) {
            binding.loginEnterLogin.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.loginEnterLoginActiveText.visibility = View.VISIBLE
        }
        else{
            binding.loginEnterLogin.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.loginEnterLoginActiveText.visibility = View.GONE
        }

        if (state.password.length >= 6) {
            binding.loginEnterPassword.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.loginEnterPasswordActiveText.visibility = View.VISIBLE
        }
        else{
            binding.loginEnterPassword.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.loginEnterPasswordActiveText.visibility = View.GONE
        }

        if ((state.login.length >= 3) and (state.password.length >= 6)){
            binding.loginButtonLogin.background =  requireActivity().getDrawable(R.drawable.btn_active)
            binding.loginButtonLogin.isEnabled = true
            binding.loginButtonLoginText.setTextColor(requireActivity().getColor(R.color.white))
            }
        else{
            binding.loginButtonLogin.isEnabled = false
            binding.loginButtonLogin.background =  requireActivity().getDrawable(R.drawable.btn_inactive)
            binding.loginButtonLoginText.setTextColor(requireActivity().getColor(R.color.gray))
        }
    }

    private fun renderLogin(loginState: TryLoginState){
        when (loginState){
            is TryLoginState.Error -> {
                binding.progressIndicator.visibility = View.GONE
                Toast.makeText(requireActivity(), loginState.errorMessage, Toast.LENGTH_LONG).show()
                Log.d("ERRORMESSAGE", "${loginState.errorMessage}")
            }
            TryLoginState.Fail -> {
                binding.progressIndicator.visibility = View.GONE
                Toast.makeText(requireActivity(), "Login Failed! Incorrect login or password", Toast.LENGTH_LONG).show()
            }
            TryLoginState.Loading -> {
                binding.progressIndicator.visibility = View.VISIBLE
            }
            TryLoginState.Success -> {
                binding.progressIndicator.visibility = View.GONE
                Toast.makeText(requireActivity(), "Login Successfull!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
            }

            TryLoginState.Default -> {}
        }
    }



}