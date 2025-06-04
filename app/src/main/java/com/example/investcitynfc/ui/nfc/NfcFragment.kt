package com.example.investcitynfc.ui.nfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.investcitynfc.databinding.FragmentNfcBinding

class NfcFragment : Fragment() {
    private var _binding: FragmentNfcBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNfcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.statusText.text = "Aproxime um cartão NFC"
        binding.statusText.textSize = 20f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 