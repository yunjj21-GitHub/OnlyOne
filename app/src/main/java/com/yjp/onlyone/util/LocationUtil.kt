package com.yjp.onlyone.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

private val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

/** 위치 권한 확인 및 지역명 조회 유틸. */
object LocationUtil {

    /** 위치 권한(정밀/대략) 중 하나라도 허용됐는지 확인한다. */
    fun hasPermission(context: Context): Boolean {
        return locationPermissions.any { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /** 현재 위치를 조회해 지역명 문자열을 반환한다. 권한 없음·조회 실패 시 null. */
    suspend fun getRegionName(context: Context): String? = withContext(Dispatchers.IO) {
        if (!hasPermission(context)) return@withContext null

        val location = fetchLocation(context) ?: return@withContext null
        resolveRegionName(
            context = context,
            latitude = location.latitude,
            longitude = location.longitude,
        )
    }

    /** Fused Location으로 현재 좌표를 가져온다. 실패 시 마지막 위치로 대체한다. */
    private suspend fun fetchLocation(context: Context): Location? {
        val client = LocationServices.getFusedLocationProviderClient(context)
        return fetchCurrentLocation(client) ?: fetchLastLocation(client)
    }

    /** GPS/네트워크로 현재 위치를 새로 조회한다. */
    @SuppressLint("MissingPermission")
    private suspend fun fetchCurrentLocation(client: FusedLocationProviderClient): Location? =
        suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()
            continuation.invokeOnCancellation { cancellationTokenSource.cancel() }

            client.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token,
            )
                .addOnSuccessListener { location ->
                    continuation.resume(location)
                    OOLog.d("사용자 현재 위치: longitude ${location.longitude}, latitude ${location.latitude}")
                }
                .addOnFailureListener { continuation.resume(null) }
        }

    /** 캐시된 마지막 위치를 반환한다. */
    @SuppressLint("MissingPermission")
    private suspend fun fetchLastLocation(client: FusedLocationProviderClient): Location? =
        suspendCancellableCoroutine { continuation ->
            client.lastLocation
                .addOnSuccessListener { location -> continuation.resume(location) }
                .addOnFailureListener { continuation.resume(null) }
        }

    /** 위·경도 좌표를 주소 문자열(예: 서울특별시 중구 정동)로 바꾼다. */
    private suspend fun resolveRegionName(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): String? {
        if (!Geocoder.isPresent()) return null

        val geocoder = Geocoder(context, Locale.KOREA)
        val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            fetchAddresses(geocoder, latitude, longitude)
        } else {
            @Suppress("DEPRECATION")
            geocoder.getFromLocation(latitude, longitude, 1).orEmpty()
        }

        val address: String? = addresses.firstOrNull()?.toRegionName()?.takeIf { it.isNotBlank() }
        OOLog.d("사용자 현재 위치 주소명: $address")

        return address
    }

    /** Android 13+ Geocoder API로 주소 목록을 조회한다. */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun fetchAddresses(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double,
    ): List<Address> = suspendCancellableCoroutine { continuation ->
        geocoder.getFromLocation(
            latitude,
            longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    continuation.resume(addresses)
                }

                override fun onError(errorMessage: String?) {
                    continuation.resume(emptyList())
                }
            },
        )
    }
}

/** Fragment에서 위치 권한 요청과 지역명 조회를 한 번에 처리한다. */
class LocationRequester(
    private val fragment: Fragment,
) {
    private var pendingContinuation: CancellableContinuation<Boolean>? = null

    private val launcher: ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { results ->
            val granted = results[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                results[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            resumePermissionRequest(granted)
        }

    /**
     * 권한이 없으면 요청하고, 거부 시 [onDenied]를 호출한다.
     * 권한이 있거나 허용되면 지역명을 조회해 반환한다.
     */
    suspend fun requestRegionName(onDenied: () -> Unit = {}): String? {
        val context = fragment.requireContext()

        if (!LocationUtil.hasPermission(context)) {
            if (!requestPermission()) {
                onDenied()
                return null
            }
        }

        return LocationUtil.getRegionName(context)
    }

    /** 시스템 권한 다이얼로그를 띄우고 허용 여부를 suspend로 반환한다. */
    private suspend fun requestPermission(): Boolean {
        if (LocationUtil.hasPermission(fragment.requireContext())) return true
        if (!fragment.isAdded || fragment.isStateSaved) return false

        return suspendCancellableCoroutine { continuation ->
            pendingContinuation?.let { previous ->
                if (previous.isActive) {
                    previous.resume(false)
                }
            }
            pendingContinuation = continuation
            continuation.invokeOnCancellation { pendingContinuation = null }
            launcher.launch(locationPermissions)
        }
    }

    /** 권한 요청 결과를 대기 중인 코루틴에 전달한다. */
    private fun resumePermissionRequest(granted: Boolean) {
        pendingContinuation?.let { continuation ->
            if (continuation.isActive) {
                continuation.resume(granted)
            }
        }
        pendingContinuation = null
    }
}

/** Address 필드를 조합해 "시·도 구·동 도로명 번지" 형식 문자열을 만든다. */
private fun Address.toRegionName(): String {
    return listOfNotNull(
        adminArea?.takeIf { it.isNotBlank() },
        subLocality?.takeIf { it.isNotBlank() },
        thoroughfare?.takeIf { it.isNotBlank() },
        subThoroughfare?.takeIf { it.isNotBlank() },
    ).joinToString(" ")
}
