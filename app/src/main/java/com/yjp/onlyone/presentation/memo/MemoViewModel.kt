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

    private val _discardAlertRequest = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val discardAlertRequest: SharedFlow<Unit> = _discardAlertRequest.asSharedFlow()

    private val _saveToastEvent = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val saveToastEvent: SharedFlow<Boolean> = _saveToastEvent.asSharedFlow()

    private var savedMemoContent = ""

    init {
        loadMemo()
    }

    private fun loadMemo() {
        viewModelScope.launch {
            val content = memoRepository.getMemoContent()
            _memoContent.value = content
            savedMemoContent = content
        }
    }

    fun updateMemoContent(content: String) {
        _memoContent.value = content
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (!hasUnsavedChanges()) {
                _saveToastEvent.emit(false)
                return@launch
            }
            memoRepository.saveMemoContent(_memoContent.value)
            savedMemoContent = _memoContent.value
            _saveToastEvent.emit(true)
        }
    }

    fun onBackClick() {
        if (hasUnsavedChanges()) {
            _discardAlertRequest.tryEmit(Unit)
        } else {
            navigateHome()
        }
    }

    fun onDiscardConfirmed() {
        navigateHome()
    }

    private fun hasUnsavedChanges(): Boolean = _memoContent.value != savedMemoContent

    private fun navigateHome() {
        viewModelScope.launch {
            _navigationEvent.emit(MemoNavigation.ToHome)
        }
    }
}
