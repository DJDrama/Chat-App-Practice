package com.dj.chatapp.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.navigateSafely(
    @IdRes actionId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null,
) {
    val action = currentDestination?.getAction(actionId) ?: graph.getAction(actionId)
    action?.let {
        if (currentDestination?.id != action.destinationId) {
            navigate(actionId, args, navOptions, navExtras)
        }
    }
}