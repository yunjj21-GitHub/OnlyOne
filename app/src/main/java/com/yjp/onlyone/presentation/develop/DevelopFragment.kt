package com.yjp.onlyone.presentation.develop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yjp.onlyone.base.BaseFragment
import com.yjp.onlyone.base.setThemeContent
import com.yjp.onlyone.databinding.FragmentDevelopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DevelopFragment : BaseFragment<FragmentDevelopBinding>() {

    private val viewModel: DevelopViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDevelopBinding = FragmentDevelopBinding.inflate(inflater, container, false)

    override fun bind(binding: FragmentDevelopBinding) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.developComposeView.setThemeContent {
            DevelopScreen(viewModel = viewModel)
        }
    }
}
