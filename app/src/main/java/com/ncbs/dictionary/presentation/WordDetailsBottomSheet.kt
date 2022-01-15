package com.ncbs.dictionary.presentation

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ncbs.dictionary.data.HOST_URL
import com.ncbs.dictionary.databinding.BottomSheetWordDetailsBinding
import com.ncbs.dictionary.domain.DictionaryInteractor
import com.ncbs.dictionary.domain.Language
import com.ncbs.dictionary.domain.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class WordDetailsBottomSheet(
    private val word: Word
) : BottomSheetDialogFragment() {

    private val dictionaryInteractor: DictionaryInteractor = DictionaryInteractor()

    private var _binding: BottomSheetWordDetailsBinding? = null
    private val binding get() = _binding!!

    private var player: MediaPlayer? = null

    private suspend fun createPlayer(): MediaPlayer? {
        val nvLocale = word.locales[Language.NIVKH.code] ?: return null
        return if (dictionaryInteractor.isUrlExist(nvLocale.audioPath)) {
            MediaPlayer.create(context, Uri.parse(nvLocale.audioPath))
        } else {
            null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWordDetailsBinding.inflate(inflater, container, false).apply {
            lifecycleScope.launch {
                val nvLocale = word.locales[Language.NIVKH.code] ?: return@launch
                nvWord.text = nvLocale.value
                ruWord.text = word.getTranslate(Language.RUSSIAN.code)
                enWord.text = word.getTranslate(Language.ENGLISH.code)
                player = createPlayer()
                playButton.isVisible = player != null
                playButton.setOnClickListener {
                    if (player?.isPlaying == true) return@setOnClickListener
                    try {
                        lifecycleScope.launch(Dispatchers.IO) {
                            player?.start()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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