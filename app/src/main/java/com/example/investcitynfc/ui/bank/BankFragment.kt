package com.example.investcitynfc.ui.bank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.investcitynfc.R
import com.example.investcitynfc.databinding.FragmentBankBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BankFragment : Fragment() {
    private var _binding: FragmentBankBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BankViewModel by viewModels()
    private lateinit var adapter: BankAccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()
        observeAccounts()
    }

    private fun setupRecyclerView() {
        adapter = BankAccountAdapter { account ->
            val action = BankFragmentDirections.actionBankToAccountDetails(account.id)
            findNavController().navigate(action)
        }
        binding.accountsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@BankFragment.adapter
        }
    }

    private fun setupButtons() {
        binding.refreshButton.setOnClickListener {
            viewModel.loadAccounts()
            Toast.makeText(requireContext(), "Dados atualizados", Toast.LENGTH_SHORT).show()
        }

        binding.clearButton.setOnClickListener {
            viewModel.clearAllData()
            Toast.makeText(requireContext(), "Dados limpos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeAccounts() {
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            adapter.submitList(accounts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 