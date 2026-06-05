package com.yjp.onlyone.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
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
import com.yjp.onlyone.ui.component.rememberOnlyOneToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private var showExitToast: (() -> Unit)? = null

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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (viewModel.onBackPressed()) {
                        HomeBackPress.ShowExitToast -> showExitToast?.invoke()
                        HomeBackPress.FinishApp -> requireActivity().finish()
                    }
                }
            },
        )
        binding.homeComposeView.setThemeContent {
            val showToast = rememberOnlyOneToast()
            val exitBackPressMessage = stringResource(R.string.home_exit_back_press_message)
            val petName by viewModel.petName.collectAsStateWithLifecycle()
            val petIconRes by viewModel.petIconRes.collectAsStateWithLifecycle()
            val happinessIndex by viewModel.happinessIndex.collectAsStateWithLifecycle()
            val activityStats by viewModel.activityStats.collectAsStateWithLifecycle()
            val activePicker by viewModel.activePicker.collectAsStateWithLifecycle()
            val daysTogether by viewModel.daysTogether.collectAsStateWithLifecycle()

            SideEffect {
                showExitToast = { showToast(exitBackPressMessage) }
            }

            HomeScreen(
                petName = petName,
                petIconRes = petIconRes,
                happinessIndex = happinessIndex,
                activityStats = activityStats,
                activePicker = activePicker,
                daysTogether = daysTogether,
                onMemoClick = viewModel::onMemoClick,
                onDogInfoEditClick = viewModel::onDogInfoEditClick,
                onActivityStatClick = viewModel::onActivityStatClick,
                onDismissPicker = viewModel::dismissPicker,
                onConfirmPicker = viewModel::confirmPicker,
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

    override fun onDestroyView() {
        showExitToast = null
        super.onDestroyView()
    }
}
