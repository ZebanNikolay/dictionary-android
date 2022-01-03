package com.ncbs.dictionary.presentation

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ncbs.dictionary.data.HOST_URL
import com.ncbs.dictionary.databinding.BottomSheetWordDetailsBinding
import com.ncbs.dictionary.domain.Language
import com.ncbs.dictionary.domain.Word
import java.lang.Exception

class WordDetailsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetWordDetailsBinding? = null
    private val binding get() = _binding!!

    var word: Word? = null

    private val player: MediaPlayer? by lazy {
        val nvLocale = word?.locales?.get(Language.NIVKH.code) ?: return@lazy null
        MediaPlayer.create(context, Uri.parse("$HOST_URL/${nvLocale.audioPath}"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWordDetailsBinding.inflate(inflater, container, false).apply {
            val nvLocale = word?.locales?.get(Language.NIVKH.code) ?: return@apply
            nvWord.text = nvLocale.value
            ruWord.text = word?.getTranslate(Language.RUSSIAN.code)
            enWord.text = word?.getTranslate(Language.ENGLISH.code)
            playButton.isVisible = !nvLocale.audioPath.isNullOrBlank()
            playButton.setOnClickListener {
                if (player?.isPlaying == true) return@setOnClickListener
                try {
                    player?.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}