package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentRegisterBinding
import com.example.project1729.domain.model.UserRegister
import com.example.project1729.domain.state.TryRegisterState
import com.example.project1729.ui.view_model.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModel<RegisterViewModel>()
    private var mode: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mode = requireActivity().getWindow().getAttributes().softInputMode
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.registerButtonRegister.isEnabled = false

        binding.rabkinRegisterButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getRegisterLiveData().observe(viewLifecycleOwner){state ->
            render(state)
        }

        viewModel.getRegisterStateLiveData().observe(viewLifecycleOwner){registerState ->
            renderRegister(registerState)

        }

        binding.registerEnterFIO.doOnTextChanged { text, _, _, _ -> viewModel.changeFioText(text.toString())  }
        binding.registerEnterYear.doOnTextChanged { text, _, _, _ -> viewModel.changeYearText(text.toString())  }
        binding.registerEnterDiagnoz.doOnTextChanged { text, _, _, _ -> viewModel.changeDiagnozText(text.toString())  }
        binding.registerEnterLogin.doOnTextChanged { text, _, _, _ -> viewModel.changeLoginText(text.toString())  }
        binding.registerEnterPassword.doOnTextChanged { text, _, _, _ -> viewModel.changePasswordText(text.toString())  }

        binding.registerButtonRegister.setOnClickListener {
            viewModel.tryToRegister()
        }


        super.onViewCreated(view, savedInstanceState)
    }


    private fun render(state: UserRegister){

        if (state.fio.length >= 6){
            binding.registerEnterFIO.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.registerEnterFIOActiveText.visibility = View.VISIBLE
        }
        else{
            binding.registerEnterFIO.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.registerEnterFIOActiveText.visibility = View.GONE
        }


        if (state.year.length == 4){
            binding.registerEnterYear.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.registerEnterYearActiveText.visibility = View.VISIBLE
        }
        else{
            binding.registerEnterYear.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.registerEnterYearActiveText.visibility = View.GONE
        }


        if (state.diagnoz.length >= 6){
            binding.registerEnterDiagnoz.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.registerEnterDiagnozActiveText.visibility = View.VISIBLE
        }
        else{
            binding.registerEnterDiagnoz.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.registerEnterDiagnozActiveText.visibility = View.GONE
        }


        if ((state.login.length >= 3) && (state.login.length <= 10)) {
            binding.registerEnterLogin.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.registerEnterLoginActiveText.visibility = View.VISIBLE
        }
        else{
            binding.registerEnterLogin.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.registerEnterLoginActiveText.visibility = View.GONE
        }

        if (state.password.length >= 6){
            binding.registerEnterPassword.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.registerEnterPasswordActiveText.visibility = View.VISIBLE
        }
        else{
            binding.registerEnterPassword.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.registerEnterPasswordActiveText.visibility = View.GONE
        }


        if ((state.fio.length >= 6) and (state.year.length == 4) and (state.login.length >= 3) and (state.password.length >= 6)){
            binding.registerButtonRegister.background =  requireActivity().getDrawable(R.drawable.btn_active)
            binding.registerButtonRegister.isEnabled = true
            binding.loginButtonLoginText.setTextColor(requireActivity().getColor(R.color.white))
        }
        else{
            binding.registerButtonRegister.background =  requireActivity().getDrawable(R.drawable.btn_inactive)
            binding.registerButtonRegister.isEnabled = false
            binding.loginButtonLoginText.setTextColor(requireActivity().getColor(R.color.gray))
        }
    }

    private fun renderRegister(registerState: TryRegisterState){
        when(registerState){
            is TryRegisterState.Error -> {
                binding.progressIndicator.visibility = View.GONE
                Toast.makeText(requireActivity(), registerState.errorMessage, Toast.LENGTH_LONG).show()
            }
            TryRegisterState.Success -> {
                binding.progressIndicator.visibility = View.GONE
                Toast.makeText(requireActivity(), "Registration Successfull!", Toast.LENGTH_LONG).show()
                lifecycleScope.launch {
                    delay(2000L)
                    findNavController().navigateUp()
                }
            }

            TryRegisterState.Loading -> {
                binding.progressIndicator.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        requireActivity().getWindow().setSoftInputMode(mode)
        super.onDestroy()
    }
}