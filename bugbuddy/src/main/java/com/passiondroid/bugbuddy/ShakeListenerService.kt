package com.passiondroid.bugbuddy

import com.squareup.seismic.ShakeDetector

/**
 * Created by Arif Khan on 26/02/20.
 */
internal object ShakeListenerService: ShakeDetector.Listener {

    lateinit var onShake: () -> Unit

    override fun hearShake() {
        onShake.invoke()
    }
}