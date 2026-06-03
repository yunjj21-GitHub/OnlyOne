package com.yjp.onlyone.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjp.onlyone.base.BaseFragment
import com.yjp.onlyone.base.setThemeContent
import com.yjp.onlyone.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun bind(binding: FragmentHomeBinding) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeComposeView.setThemeContent {
            val petName by viewModel.petName.collectAsStateWithLifecycle()
            val petIconRes by viewModel.petIconRes.collectAsStateWithLifecycle()
            val happinessIndex by viewModel.happinessIndex.collectAsStateWithLifecycle()
            val daysTogether by viewModel.daysTogether.collectAsStateWithLifecycle()
            HomeScreen(
                petName = petName,
                petIconRes = petIconRes,
                happinessIndex = happinessIndex,
                daysTogether = daysTogether,
            )
        }
    }
}
