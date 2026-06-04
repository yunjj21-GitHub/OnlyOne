package com.yjp.onlyone.presentation.home

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
import com.yjp.onlyone.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

    override fun onResume() {
        super.onResume()
        viewModel.loadPetInfo()
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
                onMemoClick = viewModel::onMemoClick,
                onDogInfoEditClick = viewModel::onDogInfoEditClick,
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        HomeNavigation.ToMemo ->
                            findNavController().navigate(R.id.action_home_to_memo)
                        HomeNavigation.ToDogInfoEdit ->
                            findNavController().navigate(R.id.action_home_to_dog_info_edit)
                    }
                }
            }
        }
    }
}
