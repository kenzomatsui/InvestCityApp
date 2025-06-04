package com.example.investcitynfc.ui.bank

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investcitynfc.InvestCityApp
import com.example.investcitynfc.model.BankAccount
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class BankViewModel : ViewModel() {
    private val _accounts = MutableLiveData<List<BankAccount>>()
    val accounts: LiveData<List<BankAccount>> = _accounts

    init {
        loadAccounts()
    }

    fun loadAccounts() {
        viewModelScope.launch {
            val prefs = InvestCityApp.instance.getSharedPreferences("bank_accounts", Context.MODE_PRIVATE)
            val accountsJson = prefs.getString("accounts", null)
            
            if (accountsJson != null) {
                val accountsList = mutableListOf<BankAccount>()
                val jsonArray = JSONArray(accountsJson)
                
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    accountsList.add(
                        BankAccount(
                            id = jsonObject.getInt("id"),
                            ownerName = jsonObject.getString("ownerName"),
                            balance = jsonObject.getDouble("balance")
                        )
                    )
                }
                _accounts.postValue(accountsList)
            } else {
                // Inicializar com 4 contas vazias
                val initialAccounts = List(4) { index ->
                    BankAccount(id = index + 1)
                }
                _accounts.postValue(initialAccounts)
                saveAccounts(initialAccounts)
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            val prefs = InvestCityApp.instance.getSharedPreferences("bank_accounts", Context.MODE_PRIVATE)
            prefs.edit().clear().commit()
            
            // Inicializar com 4 contas vazias
            val initialAccounts = List(4) { index ->
                BankAccount(id = index + 1)
            }
            _accounts.postValue(initialAccounts)
            saveAccounts(initialAccounts)
        }
    }

    fun updateAccountName(accountId: Int, newName: String) {
        val currentAccounts = _accounts.value?.toMutableList() ?: return
        val accountIndex = currentAccounts.indexOfFirst { it.id == accountId }
        if (accountIndex != -1) {
            currentAccounts[accountIndex] = currentAccounts[accountIndex].copy(ownerName = newName)
            _accounts.postValue(currentAccounts)
            saveAccounts(currentAccounts)
        }
    }

    fun deposit(accountId: Int, amount: Double) {
        if (amount <= 0) return
        val currentAccounts = _accounts.value?.toMutableList() ?: return
        val accountIndex = currentAccounts.indexOfFirst { it.id == accountId }
        if (accountIndex != -1) {
            val account = currentAccounts[accountIndex]
            currentAccounts[accountIndex] = account.copy(balance = account.balance + amount)
            _accounts.postValue(currentAccounts)
            saveAccounts(currentAccounts)
        }
    }

    fun withdraw(accountId: Int, amount: Double): Boolean {
        if (amount <= 0) return false
        val currentAccounts = _accounts.value?.toMutableList() ?: return false
        val accountIndex = currentAccounts.indexOfFirst { it.id == accountId }
        if (accountIndex != -1) {
            val account = currentAccounts[accountIndex]
            if (account.balance < amount) return false
            currentAccounts[accountIndex] = account.copy(balance = account.balance - amount)
            _accounts.postValue(currentAccounts)
            saveAccounts(currentAccounts)
            return true
        }
        return false
    }

    fun transfer(fromAccountId: Int, toAccountId: Int, amount: Double): Boolean {
        if (amount <= 0) return false
        val currentAccounts = _accounts.value?.toMutableList() ?: return false
        
        val fromAccountIndex = currentAccounts.indexOfFirst { it.id == fromAccountId }
        val toAccountIndex = currentAccounts.indexOfFirst { it.id == toAccountId }
        
        if (fromAccountIndex != -1 && toAccountIndex != -1) {
            val fromAccount = currentAccounts[fromAccountIndex]
            val toAccount = currentAccounts[toAccountIndex]
            
            if (fromAccount.balance < amount) return false
            
            currentAccounts[fromAccountIndex] = fromAccount.copy(balance = fromAccount.balance - amount)
            currentAccounts[toAccountIndex] = toAccount.copy(balance = toAccount.balance + amount)
            
            _accounts.postValue(currentAccounts)
            saveAccounts(currentAccounts)
            return true
        }
        return false
    }

    private fun saveAccounts(accounts: List<BankAccount>) {
        viewModelScope.launch {
            val prefs = InvestCityApp.instance.getSharedPreferences("bank_accounts", Context.MODE_PRIVATE)
            val jsonArray = JSONArray()
            
            accounts.forEach { account ->
                val jsonObject = JSONObject().apply {
                    put("id", account.id)
                    put("ownerName", account.ownerName)
                    put("balance", account.balance)
                }
                jsonArray.put(jsonObject)
            }
            
            prefs.edit().putString("accounts", jsonArray.toString()).commit()
        }
    }
} 