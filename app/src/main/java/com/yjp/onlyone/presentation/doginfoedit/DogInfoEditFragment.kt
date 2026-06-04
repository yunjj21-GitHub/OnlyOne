package com.yjp.onlyone.presentation.doginfoedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yjp.onlyone.R
import com.yjp.onlyone.base.BaseFragment
import com.yjp.onlyone.base.setThemeContent
import com.yjp.onlyone.databinding.FragmentDogInfoEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogInfoEditFragment : BaseFragment<FragmentDogInfoEditBinding>() {

    private val viewModel: DogInfoEditViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDogInfoEditBinding = FragmentDogInfoEditBinding.inflate(inflater, container, false)

    override fun bind(binding: FragmentDogInfoEditBinding) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dogInfoEditComposeView.setThemeContent {
            val petIconRes by viewModel.petIconRes.collectAsStateWithLifecycle()
            val petName by viewModel.petName.collectAsStateWithLifecycle()
            val adoptionDate by viewModel.adoptionDate.collectAsStateWithLifecycle()
            val isDatePickerVisible by viewModel.isDatePickerVisible.collectAsStateWithLifecycle()
            DogInfoEditScreen(
                petIconRes = petIconRes,
                petName = petName,
                adoptionDate = adoptionDate,
                onBackClick = viewModel::onBackClick,
                onSaveClick = viewModel::onSaveClick,
                onPetNameChange = viewModel::onPetNameChange,
                onPetIconSelect = viewModel::onPetIconSelect,
                isDatePickerVisible = isDatePickerVisible,
                onCalendarClick = viewModel::onCalendarClick,
                onDatePickerDismiss = viewModel::onDatePickerDismiss,
                onAdoptionDateSelected = viewModel::onAdoptionDateSelected,
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { event ->
                    if (event == DogInfoEditNavigation.ToHome) {
                        findNavController().navigate(R.id.action_dog_info_edit_to_home)
                    }
                }
            }
        }
    }
}
