package com.yjp.onlyone.presentation.develop

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.domain.repository.WeatherDebugRepository
import com.yjp.onlyone.util.KmaGridConverter
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

    private val _debugText = MutableStateFlow("조회 중...")
    val debugText: StateFlow<String> = _debugText.asStateFlow()

    init {
        loadWeatherDebugInfo()
    }

    private fun loadWeatherDebugInfo() {
        viewModelScope.launch {
            _debugText.value = "조회 중..."
            val grid = resolveGrid()
            runCatching {
                weatherDebugRepository.fetchDebugText(
                    nx = grid.nx,
                    ny = grid.ny,
                    locationNote = grid.note,
                )
            }.onSuccess { text ->
                _debugText.value = text
            }.onFailure { throwable ->
                _debugText.value = "오류: ${throwable.message ?: "날씨 API 조회 실패"}"
            }
        }
    }

    private suspend fun resolveGrid(): ResolvedGrid {
        val grid = LocationUtil.getKmaGrid(context)
        return if (grid != null) {
            ResolvedGrid(
                nx = grid.nx,
                ny = grid.ny,
                note = "위치: 위도 ${grid.latitude}, 경도 ${grid.longitude}",
            )
        } else {
            ResolvedGrid(
                nx = KmaGridConverter.FALLBACK_NX,
                ny = KmaGridConverter.FALLBACK_NY,
                note = "위치 권한 없음 또는 조회 실패 → 서울 기본 격자(nx=${KmaGridConverter.FALLBACK_NX}, ny=${KmaGridConverter.FALLBACK_NY}) 사용",
            )
        }
    }

    private data class ResolvedGrid(
        val nx: Int,
        val ny: Int,
        val note: String,
    )
}
