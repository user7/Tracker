package com.gb.tracker.screens.entities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gb.tracker.App
import com.gb.tracker.AppScreens
import com.gb.tracker.IBackListener
import com.gb.tracker.databinding.EntitiesFragmentBinding
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class EntitiesFragment : MvpAppCompatFragment(), EntitiesView, IBackListener {
    companion object {
        fun newInstance() = EntitiesFragment()
    }

    override fun backPressed(): Boolean = presenter.backPressed()

    private val presenter: EntitiesPresenter by moxyPresenter {
        EntitiesPresenter(App.instance.router, AppScreens())
    }

    private var _binding: EntitiesFragmentBinding? = null
    private val binding: EntitiesFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EntitiesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backFabButton.setOnClickListener { presenter.backPressed() }
    }

}