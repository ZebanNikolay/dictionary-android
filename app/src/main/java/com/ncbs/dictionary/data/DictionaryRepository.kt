package com.ncbs.dictionary.data

import android.content.Context
import android.util.Log
import com.ncbs.dictionary.App
import com.ncbs.dictionary.domain.Language
import com.ncbs.dictionary.domain.LocaleData
import com.ncbs.dictionary.domain.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.suspendCoroutine

const val HOST_URL = "http://bibl-nogl-dictionary.ru/data"
private const val WORDS_FILE_NAME = "words.json"
private const val WORDS_FILE_URL = "$HOST_URL/$WORDS_FILE_NAME"
private const val TAG = "DictionaryRepository"

class DictionaryRepository(
    private val context: Context = App.context
) {

    private val client = OkHttpClient()

    suspend fun getWords(): List<Word> = withContext(Dispatchers.IO) {
        val path = "${context.filesDir}/$WORDS_FILE_NAME"
        Log.d(TAG, "Start read words from [$path]")
        val file = File(path)
        return@withContext if (file.exists()) {
            val list = Json.decodeFromString<List<WordDto>>(file.readText())
            Log.d(TAG, "Read [$path] successfully")
            list.mapNotNull(::mapToWord)
        } else {
            Log.d(TAG, "[$path] isn't exist")
            updateWords()
        }
    }

    private fun mapToWord(wordDto: WordDto): Word? {
        if (wordDto.id.isNullOrBlank() || wordDto.nv.isNullOrBlank() || wordDto.ru.isNullOrBlank() || wordDto.en.isNullOrBlank()) return null
        return Word(
            id = wordDto.id.toString(),
            locales = mapOf(
                Language.NIVKH.code to LocaleData(
                    Language.NIVKH,
                    wordDto.nv,
                    "$HOST_URL/audio/${wordDto.id}.mp3"
                ),
                Language.RUSSIAN.code to LocaleData(Language.RUSSIAN, wordDto.ru),
                Language.ENGLISH.code to LocaleData(Language.ENGLISH, wordDto.en),
            )
        )
    }

    suspend fun updateWords(): List<Word> = suspendCoroutine { continuation ->
        Log.d(TAG, "Start update words from server url = $WORDS_FILE_URL")
        val request = Request.Builder()
            .url(WORDS_FILE_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "Fetch words from server failure")

                e.printStackTrace()
                continuation.resumeWith(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                Log.d(TAG, "Fetch words from server successfully")
                val string = response.body!!.string()
                writeWordsToFile(string)
                val list = Json.decodeFromString<List<WordDto>>(string)
                    .mapNotNull(::mapToWord)
                continuation.resumeWith(Result.success(list))
            }
        })
    }

    private fun writeWordsToFile(json: String): File {
        val path = "${context.filesDir}/$WORDS_FILE_NAME"
        Log.d(TAG, "Start write words to [$path]")
        return File(path).apply {
            if (exists()) {
                Log.d(TAG, "[$path] already exist, it has been deleted")
                delete()
            }
            createNewFile()
            writeText(json)
            Log.d(TAG, "Write [$path] successfully")
        }
    }

    suspend fun isUrlExist(url: String?): Boolean {
        url ?: return false
        return withContext(Dispatchers.IO) {
            try {
                HttpURLConnection.setFollowRedirects(false)
                val urlConnection: HttpURLConnection =
                    URL(url).openConnection() as HttpURLConnection
                urlConnection.requestMethod = "HEAD"
                urlConnection.responseCode == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }.also {
            Log.d(TAG, "Url = $url is exist = $it")
        }
    }
}