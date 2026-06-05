package com.yjp.onlyone.presentation.home

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.R
import com.yjp.onlyone.domain.happiness.HappinessIndexCalculator
import com.yjp.onlyone.domain.model.HomeHappinessInput
import com.yjp.onlyone.domain.repository.HappinessRepository
import com.yjp.onlyone.domain.repository.PetRepository
import com.yjp.onlyone.util.daysFromToToday
import com.yjp.onlyone.util.toEpochDayValue
import com.yjp.onlyone.util.todayLocalDate
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
    private val happinessRepository: HappinessRepository,
    @ApplicationContext context: Context,
) : ViewModel() {

    private val _petName = MutableStateFlow(context.getString(R.string.pet_name_default))
    val petName: StateFlow<String> = _petName.asStateFlow()

    @DrawableRes
    private val _petIconRes = MutableStateFlow(R.drawable.ic_dog1)
    val petIconRes: StateFlow<Int> = _petIconRes.asStateFlow()

    private val _happinessInput = MutableStateFlow(HomeHappinessInput())
    val happinessInput: StateFlow<HomeHappinessInput> = _happinessInput.asStateFlow()

    private val _happinessIndex = MutableStateFlow(0)
    val happinessIndex: StateFlow<Int> = _happinessIndex.asStateFlow()

    private val _activityStats = MutableStateFlow(
        HomeHappinessUiMapper.buildActivityStats(HomeHappinessInput()),
    )
    val activityStats: StateFlow<List<HomeActivityStatUi>> = _activityStats.asStateFlow()

    private val _activePicker = MutableStateFlow<HomeHappinessPicker>(HomeHappinessPicker.None)
    val activePicker: StateFlow<HomeHappinessPicker> = _activePicker.asStateFlow()

    private val _daysTogether = MutableStateFlow(0)
    val daysTogether: StateFlow<Int> = _daysTogether.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<HomeNavigation>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<HomeNavigation> = _navigationEvent.asSharedFlow()

    private var lastBackPressTimeMillis = 0L
    private var loadedDateEpochDay: Long = todayLocalDate().toEpochDayValue()

    init {
        loadPetInfo()
        loadHappinessInput()
    }

    fun loadHappinessInput() {
        viewModelScope.launch {
            loadedDateEpochDay = todayLocalDate().toEpochDayValue()
            updateHappinessUi(happinessRepository.getHappinessInput())
        }
    }

    fun loadPetInfo() {
        viewModelScope.launch {
            val petInfo = petRepository.getPetInfo()
            _petIconRes.value = petInfo.iconRes
            _petName.value = petInfo.name
            _daysTogether.value = daysFromToToday(petInfo.adoptionDate)
        }
    }

    fun onActivityStatClick(type: HomeActivityType) {
        ensureTodayHappinessInput()
        _activePicker.value = HomeHappinessPicker.Active(
            type = type,
            initialIndex = HomeHappinessPickerConfig.initialIndex(type, _happinessInput.value),
        )
    }

    fun dismissPicker() {
        _activePicker.value = HomeHappinessPicker.None
    }

    fun confirmPicker(type: HomeActivityType, selectedIndex: Int) {
        ensureTodayHappinessInput()
        applyHappinessInput(
            HomeHappinessPickerConfig.applySelection(
                type = type,
                input = _happinessInput.value,
                selectedIndex = selectedIndex,
            ),
        )
        dismissPicker()
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

    fun onBackPressed(): HomeBackPress {
        val now = System.currentTimeMillis()
        return if (now - lastBackPressTimeMillis <= EXIT_BACK_PRESS_INTERVAL_MILLIS) {
            lastBackPressTimeMillis = 0L
            HomeBackPress.FinishApp
        } else {
            lastBackPressTimeMillis = now
            HomeBackPress.ShowExitToast
        }
    }

    private fun applyHappinessInput(input: HomeHappinessInput) {
        updateHappinessUi(input)
        viewModelScope.launch {
            happinessRepository.saveHappinessInput(input)
        }
    }

    private fun ensureTodayHappinessInput() {
        val todayEpochDay = todayLocalDate().toEpochDayValue()
        if (loadedDateEpochDay == todayEpochDay) return
        loadedDateEpochDay = todayEpochDay
        updateHappinessUi(HomeHappinessInput())
    }

    private fun updateHappinessUi(input: HomeHappinessInput) {
        _happinessInput.value = input
        _happinessIndex.value = HappinessIndexCalculator.calculate(input)
        _activityStats.value = HomeHappinessUiMapper.buildActivityStats(input)
    }

    companion object {
        const val HAPPINESS_INDEX_MIN = 0
        const val HAPPINESS_INDEX_MAX = 100
        const val EXIT_BACK_PRESS_INTERVAL_MILLIS = 2_000L

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
