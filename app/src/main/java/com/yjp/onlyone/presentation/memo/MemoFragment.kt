package com.yjp.onlyone.presentation.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.LaunchedEffect
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
import com.yjp.onlyone.databinding.FragmentMemoBinding
import com.yjp.onlyone.ui.component.rememberOOToast
import com.yjp.onlyone.ui.dialog.OOUnsavedBackAlertHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MemoFragment : BaseFragment<FragmentMemoBinding>() {

    private val viewModel: MemoViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMemoBinding = FragmentMemoBinding.inflate(inflater, container, false)

    override fun bind(binding: FragmentMemoBinding) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackClick()
                }
            },
        )
        binding.memoComposeView.setThemeContent {
            val memoContent by viewModel.memoContent.collectAsStateWithLifecycle()
            val showToast = rememberOOToast()
            val savedMessage = stringResource(R.string.save_feedback_saved)
            val noChangesMessage = stringResource(R.string.save_feedback_no_changes)

            LaunchedEffect(Unit) {
                viewModel.saveToastEvent.collect { saved ->
                    showToast(if (saved) savedMessage else noChangesMessage)
                }
            }
            OOUnsavedBackAlertHost(
                discardAlertRequest = viewModel.discardAlertRequest,
                onDiscardConfirm = viewModel::onDiscardConfirmed,
            )
            MemoScreen(
                memoContent = memoContent,
                onMemoContentChange = viewModel::updateMemoContent,
                onSaveClick = viewModel::onSaveClick,
                onBackClick = viewModel::onBackClick,
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { event ->
                    if (event == MemoNavigation.ToHome) {
                        findNavController().navigate(R.id.action_memo_to_home)
                    }
                }
            }
        }
    }
}
