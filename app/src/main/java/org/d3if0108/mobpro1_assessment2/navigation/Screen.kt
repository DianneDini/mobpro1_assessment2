package org.d3if0108.mobpro1_assessment2.navigation

import org.d3if0108.mobpro1_assessment2.ui.screen.KEY_ID_CATATAN

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_CATATAN}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}