package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Native PCM wave audio synthesizer for instant sound effects
 * without needing external MP3/WAV assets.
 */
object SoundEffectsManager {
    var soundEnabled: Boolean = true

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Plays a soft page flip sound effect (paper rustle frequency sweep)
     */
    fun playPageFlipSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                val sampleRate = 22050
                val durationMs = 120
                val numSamples = durationMs * sampleRate / 1000
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    // Sweeping white noise + low frequency paper thump
                    val envelope = sin(Math.PI * i / numSamples)
                    val freq = 180.0 + (i.toDouble() / numSamples) * 240.0
                    val noise = (Math.random() * 2.0 - 1.0) * 0.4
                    val tone = sin(2.0 * Math.PI * freq * t) * 0.6
                    val sample = ((tone + noise) * envelope * 0.3 * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playPcmBuffer(buffer, sampleRate)
            } catch (e: Exception) {
                // Audio play fallback
            }
        }
    }

    /**
     * Plays a satisfying paint splash / tap fill pop sound
     */
    fun playPopSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                val sampleRate = 22050
                val durationMs = 80
                val numSamples = durationMs * sampleRate / 1000
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val envelope = Math.exp(-t * 35.0)
                    val freq = 520.0 - (t * 2200.0) // Pitch drops quickly for "pop"
                    val sample = (sin(2.0 * Math.PI * freq * t) * envelope * 0.5 * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playPcmBuffer(buffer, sampleRate)
            } catch (e: Exception) {
                // Audio play fallback
            }
        }
    }

    /**
     * Plays a gentle brush stroke sound
     */
    fun playBrushSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                val sampleRate = 22050
                val durationMs = 50
                val numSamples = durationMs * sampleRate / 1000
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val envelope = sin(Math.PI * i / numSamples)
                    val noise = (Math.random() * 2.0 - 1.0) * 0.25
                    val sample = (noise * envelope * 0.2 * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playPcmBuffer(buffer, sampleRate)
            } catch (e: Exception) {
                // Audio play fallback
            }
        }
    }

    /**
     * Plays a cheerful fanfare when page is completed
     */
    fun playFanfareSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                val sampleRate = 22050
                val notes = intArrayOf(523, 659, 784, 1046) // C5, E5, G5, C6
                val durationMs = 150
                
                for (freq in notes) {
                    val numSamples = durationMs * sampleRate / 1000
                    val buffer = ShortArray(numSamples)
                    for (i in 0 until numSamples) {
                        val t = i.toDouble() / sampleRate
                        val envelope = sin(Math.PI * i / numSamples)
                        val sample = (sin(2.0 * Math.PI * freq * t) * envelope * 0.4 * Short.MAX_VALUE).toInt()
                        buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    }
                    playPcmBuffer(buffer, sampleRate)
                    kotlinx.coroutines.delay(80)
                }
            } catch (e: Exception) {
                // Audio play fallback
            }
        }
    }

    private fun playPcmBuffer(buffer: ShortArray, sampleRate: Int) {
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(maxOf(minBufferSize, buffer.size * 2))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
    }
}
