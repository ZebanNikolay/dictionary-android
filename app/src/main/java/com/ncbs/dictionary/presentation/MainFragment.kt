package com.ncbs.dictionary.presentation

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ncbs.dictionary.R
import com.ncbs.dictionary.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: WordsListViewModel by viewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.language_action -> {
                    showMenu(binding.toolbar.findViewById(R.id.language_action), R.menu.popup_language)
                    true
                }
                else -> false
            }
        }
        print(viewModel.selectedLocale)
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context!!, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nv_language_action -> {
                    true
                }
                R.id.ru_language_action -> {
                    true
                }
                R.id.en_language_action -> {
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}