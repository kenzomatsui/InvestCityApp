package com.example.investcitynfc

import android.app.AlertDialog
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.investcitynfc.databinding.ActivityMainBinding
import com.example.investcitynfc.model.PlayerCard
import com.example.investcitynfc.nfc.NfcManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nfcManager: NfcManager
    private var currentPlayerCard: PlayerCard? = null
    private var lastOperation: String = ""
    private var errorMessage: String = ""
    private var isClearingCard: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nfcManager = NfcManager(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        if (!nfcManager.isNfcEnabled()) {
            showNfcDisabledDialog()
        } else {
            nfcManager.enableForegroundDispatch()
        }
    }

    override fun onPause() {
        super.onPause()
        nfcManager.disableForegroundDispatch()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
        ) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            
            tag?.let {
                val ndef = Ndef.get(it)
                val ndefFormatable = NdefFormatable.get(it)

                if (ndef != null) {
                    Log.i("NFC", "Cartão é NDEF. Pode ler/escrever.")
                    handleNfcTag(it)
                } else if (ndefFormatable != null) {
                    Log.i("NFC", "Cartão é formatável para NDEF. Precisa formatar antes de usar.")
                    if (isClearingCard) {
                        handleNfcTag(it)
                    } else {
                        errorMessage = "Cartão precisa ser formatado primeiro. Use a opção 'Limpar Cartão'."
                        lastOperation = "Cartão não formatado"
                    }
                } else {
                    Log.e("NFC", "Cartão não suporta NDEF.")
                    errorMessage = "Cartão não é compatível com NDEF"
                    lastOperation = "Cartão incompatível"
                }
            }
        }
    }

    private fun handleNfcTag(tag: Tag) {
        try {
            Log.d("NFC", "Tag detectado: ${tag.id.joinToString { "%02x".format(it) }}")
            
            if (isClearingCard) {
                Log.d("NFC", "Iniciando processo de limpeza do cartão")
                val ndef = Ndef.get(tag)
                val ndefFormatable = NdefFormatable.get(tag)
                
                Log.d("NFC", "Tipo do cartão: NDEF=${ndef != null}, NDEFFormatable=${ndefFormatable != null}")
                
                if (nfcManager.clearNfcTag(tag)) {
                    Log.d("NFC", "Cartão limpo com sucesso")
                    currentPlayerCard = null
                    lastOperation = "Cartão limpo com sucesso"
                    errorMessage = ""
                } else {
                    Log.e("NFC", "Falha ao limpar cartão")
                    errorMessage = "Erro ao limpar cartão. Verifique se o cartão é compatível."
                }
                isClearingCard = false
                return
            }
            
            val playerCard = nfcManager.readNfcTag(tag)
            if (playerCard != null) {
                Log.d("NFC", "Cartão lido com sucesso: $playerCard")
                currentPlayerCard = playerCard
                lastOperation = "Cartão lido: ${playerCard.playerId}"
                errorMessage = ""
            } else {
                Log.d("NFC", "Tentando criar novo cartão")
                // Create new card if it's empty
                currentPlayerCard = PlayerCard(
                    playerId = "player_${System.currentTimeMillis()}",
                    balance = 0
                )
                if (nfcManager.writeNfcTag(tag, currentPlayerCard!!)) {
                    Log.d("NFC", "Novo cartão criado com sucesso")
                    lastOperation = "Novo cartão criado"
                    errorMessage = ""
                } else {
                    Log.e("NFC", "Falha ao criar novo cartão")
                    errorMessage = "Erro ao criar cartão. Verifique se o cartão é compatível com NDEF."
                }
            }
        } catch (e: Exception) {
            Log.e("NFC", "Erro ao processar tag NFC", e)
            errorMessage = "Erro: ${e.message}"
        }
    }

    private fun showNfcDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("NFC Desativado")
            .setMessage("Por favor, ative o NFC para usar o aplicativo")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showClearCardDialog() {
        AlertDialog.Builder(this)
            .setTitle("Limpar Cartão")
            .setMessage("Tem certeza que deseja limpar o cartão? Todas as informações serão apagadas.")
            .setPositiveButton("Sim") { dialog, _ ->
                isClearingCard = true
                dialog.dismiss()
            }
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}