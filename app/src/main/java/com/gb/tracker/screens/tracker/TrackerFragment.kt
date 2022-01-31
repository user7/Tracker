package com.gb.tracker.screens.tracker

import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gb.tracker.App
import com.gb.tracker.IBackListener
import com.gb.tracker.databinding.TrackerFragmentBinding
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


class TrackerFragment : MvpAppCompatFragment(), TrackerView, IBackListener {
    companion object {
        fun newInstance() = TrackerFragment()
    }

    private val presenter: TrackerPresenter by moxyPresenter {
        TrackerPresenter(App.instance.router)
    }

    private var _binding: TrackerFragmentBinding? = null
    private val binding: TrackerFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrackerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startButton.setOnClickListener { presenter.startPressed() }
        binding.stopButton.setOnClickListener { presenter.stopPressed() }
        binding.commitButton.setOnClickListener { presenter.commitPressed() }
        binding.discardButton.setOnClickListener { presenter.discardPressed() }
    }

    override fun setDisplay(text: String) {
        binding.timerDisplay.text = text
    }

    override fun showStart(show: Boolean) = setVisible(binding.startButton, show)
    override fun showStop(show: Boolean) = setVisible(binding.stopButton, show)
    override fun showCommit(show: Boolean) = setVisible(binding.commitButton, show)
    override fun showDiscard(show: Boolean) = setVisible(binding.discardButton, show)

    override fun playAlarm() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(requireContext(), notification)
        r.play()
    }

    override fun backPressed() = presenter.backPressed()

    private fun setVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}