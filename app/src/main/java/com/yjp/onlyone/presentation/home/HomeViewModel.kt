package com.yjp.onlyone.presentation.home

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.R
import com.yjp.onlyone.domain.repository.PetRepository
import com.yjp.onlyone.util.daysFromToToday
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petRepository: PetRepository,
    @ApplicationContext context: Context,
) : ViewModel() {

    private val _petName = MutableStateFlow(context.getString(R.string.pet_name_default))
    val petName: StateFlow<String> = _petName.asStateFlow()

    @DrawableRes
    private val _petIconRes = MutableStateFlow(R.drawable.ic_dog1)
    val petIconRes: StateFlow<Int> = _petIconRes.asStateFlow()

    private val _happinessIndex = MutableStateFlow(DEFAULT_HAPPINESS_INDEX)
    val happinessIndex: StateFlow<Int> = _happinessIndex.asStateFlow()

    private val _daysTogether = MutableStateFlow(0)
    val daysTogether: StateFlow<Int> = _daysTogether.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<HomeNavigation>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<HomeNavigation> = _navigationEvent.asSharedFlow()

    init {
        loadPetInfo()
    }

    fun loadPetInfo() {
        viewModelScope.launch {
            val petInfo = petRepository.getPetInfo()
            _petIconRes.value = petInfo.iconRes
            _petName.value = petInfo.name
            _daysTogether.value = daysFromToToday(petInfo.adoptionDate)
        }
    }

    fun onMemoClick() {
        viewModelScope.launch {
            _navigationEvent.emit(HomeNavigation.ToMemo)
        }
    }

    fun onDogInfoEditClick() {
        viewModelScope.launch {
            _navigationEvent.emit(HomeNavigation.ToDogInfoEdit)
        }
    }

    companion object {
        const val HAPPINESS_INDEX_MIN = 0
        const val HAPPINESS_INDEX_MAX = 100
        const val DEFAULT_HAPPINESS_INDEX = 0

        @DrawableRes
        val DEFAULT_PET_ICON_RES: Int = R.drawable.ic_dog1

        fun happinessProgress(index: Int): Float {
            return index.coerceIn(HAPPINESS_INDEX_MIN, HAPPINESS_INDEX_MAX) / 100f
        }

        fun formatDaysTogether(days: Int): String {
            return NumberFormat.getNumberInstance(Locale.KOREA).format(days)
        }

        fun buildTogetherDaysText(days: Int): String {
            val formattedDays = formatDaysTogether(days)
            return "♥ 우리가 함께한지 ${formattedDays}일 째 ♥"
        }
    }
}
