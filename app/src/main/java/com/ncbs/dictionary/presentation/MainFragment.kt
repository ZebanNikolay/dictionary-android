package com.ncbs.dictionary.presentation

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ncbs.dictionary.R
import com.ncbs.dictionary.databinding.MainFragmentBinding
import com.ncbs.dictionary.domain.Language
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.filterNotNull

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: WordsListViewModel by viewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentBottomSheet: BottomSheetDialogFragment? = null
    private var updateDialog: AlertDialog? = null

    private val wordsAdapter: WordsAdapter = WordsAdapter { word ->
        val activity = activity ?: return@WordsAdapter
        WordDetailsBottomSheet(word).apply {
            currentBottomSheet?.dismissAllowingStateLoss()
            currentBottomSheet = this
            show(activity.supportFragmentManager, WordDetailsBottomSheet::class.simpleName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.wordsList.adapter = wordsAdapter
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.wordsList.addItemDecoration(dividerItemDecoration)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.language_action -> {
                    showMenu(
                        binding.toolbar.findViewById(R.id.language_action),
                        R.menu.popup_language
                    )
                    true
                }
                R.id.update_action -> {
                    viewModel.updateWords()
                    true
                }
                R.id.about_action -> {
                    activity?.supportFragmentManager?.beginTransaction()?.let {
                        it.replace(R.id.container, AboutFragment.newInstance())
                        it.commitAllowingStateLoss()
                    }
                    true
                }
                else -> false
            }
        }
        binding.searchBar.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged((text ?: "").toString())
        }
        lifecycleScope.launch {
            viewModel.selectedLocale.collect {
                wordsAdapter.setCurrentLanguage(it)
            }
        }
        lifecycleScope.launch {
            viewModel.words.collect {
                wordsAdapter.setData(it)
            }
        }
        lifecycleScope.launch {
            viewModel.errorMessage.filterNotNull()
                .collect {
                    Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_LONG).show()
                }
        }
        lifecycleScope.launch {
            viewModel.isUpdateDialogShowing.collect {
                if (it) {
                    showUpdateDialog(view)
                } else {
                    updateDialog?.dismiss()
                    updateDialog = null
                }
            }
        }
    }

    private fun showUpdateDialog(view: View) {
        updateDialog = MaterialAlertDialogBuilder(
            view.context,
            R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setMessage(R.string.dialog_update_words_title)
            .setCancelable(false)
            .setView(R.layout.dialog_update_words)
            .create()
        updateDialog?.show()
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context!!, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.menu.findItem(viewModel.selectedLocale.value.menuId).isChecked = true
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            viewModel.selectedLocale.value = when (menuItem.itemId) {
                R.id.nv_language_action -> {
                    Language.NIVKH
                }
                R.id.ru_language_action -> {
                    Language.RUSSIAN
                }
                R.id.en_language_action -> {
                    Language.ENGLISH
                }
                else -> return@setOnMenuItemClickListener false
            }
            true
        }
        popup.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}