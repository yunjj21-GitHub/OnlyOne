package com.yjp.onlyone.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

val locationPermissions: Array<String> = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

fun hasLocationPermission(context: Context): Boolean {
    return locationPermissions.any { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}

fun isLocationPermissionGranted(permissionResults: Map<String, Boolean>): Boolean {
    return permissionResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
        permissionResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true
}

class LocationPermissionRequester(
    private val fragment: Fragment,
) {
    private var pendingContinuation: CancellableContinuation<Boolean>? = null

    private val launcher: ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { results ->
            resumeWith(isLocationPermissionGranted(results))
        }

    suspend fun requestLocationPermission(): Boolean {
        if (hasLocationPermission(fragment.requireContext())) return true
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

    private fun resumeWith(granted: Boolean) {
        pendingContinuation?.let { continuation ->
            if (continuation.isActive) {
                continuation.resume(granted)
            }
        }
        pendingContinuation = null
    }
}
