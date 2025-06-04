package com.example.investcitynfc.ui.bank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.investcitynfc.databinding.FragmentTransferBinding
import java.text.NumberFormat
import java.util.Locale

class TransferFragment : Fragment() {
    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BankViewModel by viewModels()
    private val args: TransferFragmentArgs by navArgs()
    private lateinit var adapter: TransferAccountAdapter
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        observeAccounts()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            val fromAccount = accounts.find { it.id == args.fromAccountId }
            fromAccount?.let {
                binding.fromAccountText.text = "Conta ${it.id}"
                binding.balanceText.text = "Saldo: ${currencyFormat.format(it.balance)}"
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = TransferAccountAdapter { toAccount ->
            val amountStr = binding.amountInput.text.toString()
            if (amountStr.isBlank()) {
                Toast.makeText(requireContext(), "Por favor, insira um valor", Toast.LENGTH_SHORT).show()
                return@TransferAccountAdapter
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(requireContext(), "Valor inválido", Toast.LENGTH_SHORT).show()
                return@TransferAccountAdapter
            }

            if (viewModel.transfer(args.fromAccountId, toAccount.id, amount)) {
                Toast.makeText(requireContext(), "Transferência realizada com sucesso", Toast.LENGTH_SHORT).show()
                viewModel.loadAccounts()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Saldo insuficiente", Toast.LENGTH_SHORT).show()
            }
        }

        binding.accountsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TransferFragment.adapter
        }
    }

    private fun observeAccounts() {
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            // Filtrar a conta de origem da lista
            val otherAccounts = accounts.filter { it.id != args.fromAccountId }
            adapter.submitList(otherAccounts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 