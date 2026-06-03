package com.yjp.onlyone.presentation.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yjp.onlyone.base.BaseFragment
import com.yjp.onlyone.base.setThemeContent
import com.yjp.onlyone.databinding.FragmentMemoBinding
import dagger.hilt.android.AndroidEntryPoint

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
        binding.memoComposeView.setThemeContent {
            MemoScreen()
        }
    }
}
