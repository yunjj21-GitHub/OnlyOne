package com.yjp.onlyone.presentation.develop

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.domain.repository.WeatherDebugRepository
import com.yjp.onlyone.util.KmaGridConverter
import com.yjp.onlyone.util.KmaWeatherIconMapper
import com.yjp.onlyone.util.LocationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevelopViewModel @Inject constructor(
    private val weatherDebugRepository: WeatherDebugRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DevelopUiState())
    val uiState: StateFlow<DevelopUiState> = _uiState.asStateFlow()

    init {
        loadWeatherDebugInfo()
    }

    private fun loadWeatherDebugInfo() {
        viewModelScope.launch {
            _uiState.value = DevelopUiState(isLoading = true)
            val grid = resolveGrid()
            runCatching {
                weatherDebugRepository.fetchDebugResult(
                    nx = grid.nx,
                    ny = grid.ny,
                    latitude = grid.latitude,
                    longitude = grid.longitude,
                    locationNote = grid.note,
                )
            }.onSuccess { result ->
                val skyWeather = result.currentSkyWeather
                _uiState.value = DevelopUiState(
                    debugText = result.debugText,
                    skyWeatherIconRes = skyWeather?.let {
                        KmaWeatherIconMapper.iconRes(
                            skyCode = it.skyCode,
                            precipitationCode = it.precipitationCode,
                            lightningValue = it.lightningValue,
                            isNight = it.isNight,
                        )
                    },
                    skyWeatherLabel = skyWeather?.label.orEmpty(),
                    skyWeatherDetail = skyWeather?.let(KmaWeatherIconMapper::detailLabel).orEmpty(),
                )
            }.onFailure { throwable ->
                _uiState.value = DevelopUiState(
                    debugText = "오류: ${throwable.message ?: "날씨 API 조회 실패"}",
                )
            }
        }
    }

    private suspend fun resolveGrid(): ResolvedGrid {
        val grid = LocationUtil.getKmaGrid(context)
        return if (grid != null) {
            ResolvedGrid(
                nx = grid.nx,
                ny = grid.ny,
                latitude = grid.latitude,
                longitude = grid.longitude,
                note = "위치: 위도 ${grid.latitude}, 경도 ${grid.longitude}",
            )
        } else {
            ResolvedGrid(
                nx = KmaGridConverter.FALLBACK_NX,
                ny = KmaGridConverter.FALLBACK_NY,
                latitude = KmaGridConverter.FALLBACK_LATITUDE,
                longitude = KmaGridConverter.FALLBACK_LONGITUDE,
                note = "위치 권한 없음 또는 조회 실패 → 서울 기본 좌표(nx=${KmaGridConverter.FALLBACK_NX}, ny=${KmaGridConverter.FALLBACK_NY}) 사용",
            )
        }
    }

    private data class ResolvedGrid(
        val nx: Int,
        val ny: Int,
        val latitude: Double,
        val longitude: Double,
        val note: String,
    )
}

data class DevelopUiState(
    val debugText: String = "조회 중...",
    @DrawableRes val skyWeatherIconRes: Int? = null,
    val skyWeatherLabel: String = "",
    val skyWeatherDetail: String = "",
    val isLoading: Boolean = false,
)
