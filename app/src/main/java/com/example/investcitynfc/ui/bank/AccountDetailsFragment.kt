package com.example.investcitynfc.ui.bank

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.investcitynfc.R
import com.example.investcitynfc.databinding.FragmentAccountDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

class AccountDetailsFragment : Fragment() {
    private var _binding: FragmentAccountDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BankViewModel by viewModels()
    private val args: AccountDetailsFragmentArgs by navArgs()
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeAccount()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.accountNumber.text = "Conta ${args.accountId}"
        
        binding.ownerName.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || 
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                saveOwnerName()
                true
            } else {
                false
            }
        }

        binding.ownerName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                saveOwnerName()
            }
        }

        binding.transferButton.setOnClickListener {
            val action = AccountDetailsFragmentDirections.actionAccountDetailsToTransfer(args.accountId)
            findNavController().navigate(action)
        }

        binding.depositButton.setOnClickListener {
            showTransactionDialog(true)
        }

        binding.withdrawButton.setOnClickListener {
            showTransactionDialog(false)
        }
    }

    private fun saveOwnerName() {
        val newName = binding.ownerName.text.toString()
        if (newName.isNotBlank()) {
            viewModel.updateAccountName(args.accountId, newName)
            Toast.makeText(requireContext(), "Nome atualizado com sucesso", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeAccount() {
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            val account = accounts.find { it.id == args.accountId }
            account?.let {
                binding.ownerName.setText(it.ownerName)
                binding.balance.text = currencyFormat.format(it.balance)
            }
        }
    }

    private fun showTransactionDialog(isDeposit: Boolean) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_transaction, null)
        val amountInput = dialogView.findViewById<TextInputEditText>(R.id.amount)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (isDeposit) "Depositar" else "Sacar")
            .setView(dialogView)
            .setPositiveButton("Confirmar") { dialog, _ ->
                val amountStr = amountInput.text.toString()
                if (amountStr.isBlank()) {
                    Toast.makeText(requireContext(), "Por favor, insira um valor", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val amount = amountStr.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    Toast.makeText(requireContext(), "Valor inválido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (isDeposit) {
                    viewModel.deposit(args.accountId, amount)
                    Toast.makeText(requireContext(), "Depósito realizado com sucesso", Toast.LENGTH_SHORT).show()
                    viewModel.loadAccounts()
                } else {
                    if (viewModel.withdraw(args.accountId, amount)) {
                        Toast.makeText(requireContext(), "Saque realizado com sucesso", Toast.LENGTH_SHORT).show()
                        viewModel.loadAccounts()
                    } else {
                        Toast.makeText(requireContext(), "Saldo insuficiente", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 