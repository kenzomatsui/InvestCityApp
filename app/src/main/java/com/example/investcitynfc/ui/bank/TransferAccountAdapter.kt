package com.example.investcitynfc.ui.bank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.investcitynfc.databinding.ItemTransferAccountBinding
import com.example.investcitynfc.model.BankAccount
import java.text.NumberFormat
import java.util.Locale

class TransferAccountAdapter(
    private val onItemClick: (BankAccount) -> Unit
) : ListAdapter<BankAccount, TransferAccountAdapter.ViewHolder>(TransferAccountDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransferAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemTransferAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(account: BankAccount) {
            binding.accountNumber.text = "Conta ${account.id}"
            binding.ownerName.text = account.ownerName.ifEmpty { "Sem nome" }
            binding.balance.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                .format(account.balance)
        }
    }

    private class TransferAccountDiffCallback : DiffUtil.ItemCallback<BankAccount>() {
        override fun areItemsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
            return oldItem == newItem
        }
    }
} 