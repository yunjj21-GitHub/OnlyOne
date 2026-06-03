package com.yjp.onlyone.presentation.memo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
) : ViewModel() {

    private val _memoContent = MutableStateFlow("")
    val memoContent: StateFlow<String> = _memoContent.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MemoNavigation>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<MemoNavigation> = _navigationEvent.asSharedFlow()

    init {
        loadMemo()
    }

    private fun loadMemo() {
        viewModelScope.launch {
            _memoContent.value = memoRepository.getMemoContent()
        }
    }

    fun updateMemoContent(content: String) {
        _memoContent.value = content
    }

    fun onSaveClick() {
        viewModelScope.launch {
            memoRepository.saveMemoContent(_memoContent.value)
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _navigationEvent.emit(MemoNavigation.ToHome)
        }
    }
}
