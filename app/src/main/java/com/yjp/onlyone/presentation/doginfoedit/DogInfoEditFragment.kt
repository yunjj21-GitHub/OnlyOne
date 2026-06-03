package com.yjp.onlyone.presentation.doginfoedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yjp.onlyone.base.BaseFragment
import com.yjp.onlyone.base.setThemeContent
import com.yjp.onlyone.databinding.FragmentDogInfoEditBinding
import dagger.hilt.android.AndroidEntryPoint

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
            DogInfoEditScreen()
        }
    }
}
