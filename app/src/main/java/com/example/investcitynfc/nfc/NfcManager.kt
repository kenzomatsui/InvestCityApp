package com.example.investcitynfc.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build
import android.util.Log
import com.example.investcitynfc.model.PlayerCard
import java.io.IOException
import java.nio.charset.StandardCharsets

class NfcManager(private val activity: Activity) {
    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)
    private var pendingIntent: PendingIntent? = null
    private var ndefFilters: Array<IntentFilter>? = null
    private var techListsArray: Array<Array<String>>? = null

    init {
        if (nfcAdapter != null) {
            val intent = Intent(activity, activity.javaClass).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            pendingIntent = PendingIntent.getActivity(
                activity, 0, intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

            val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
                try {
                    addDataType("application/com.example.investcitynfc")
                } catch (e: IntentFilter.MalformedMimeTypeException) {
                    throw RuntimeException("Failed to set MIME type", e)
                }
            }
            ndefFilters = arrayOf(ndef)
            techListsArray = arrayOf(
                arrayOf(Ndef::class.java.name),
                arrayOf(NdefFormatable::class.java.name)
            )
        }
    }

    fun isNfcEnabled(): Boolean = nfcAdapter?.isEnabled == true

    fun enableForegroundDispatch() {
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            ndefFilters,
            techListsArray
        )
    }

    fun disableForegroundDispatch() {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    fun readNfcTag(tag: Tag): PlayerCard? {
        val ndef = Ndef.get(tag) ?: return null
        val ndefMessage = ndef.cachedNdefMessage ?: return null
        val records = ndefMessage.records

        for (record in records) {
            if (record.toMimeType() == "application/com.example.investcitynfc") {
                val payload = String(record.payload, StandardCharsets.UTF_8)
                return PlayerCard.fromJson(payload)
            }
        }
        return null
    }

    fun writeNfcTag(tag: Tag, playerCard: PlayerCard): Boolean {
        val ndef = Ndef.get(tag)
        return when {
            ndef != null -> {
                if (!ndef.isWritable) {
                    Log.e("NFC", "Tag não é gravável")
                    return false
                }
                if (ndef.maxSize < 1024) {
                    Log.e("NFC", "Tag não tem espaço suficiente")
                    return false
                }
                
                val json = PlayerCard.toJson(playerCard)
                val record = NdefRecord.createMime(
                    "application/com.example.investcitynfc",
                    json.toByteArray(StandardCharsets.UTF_8)
                )
                val message = NdefMessage(arrayOf(record))
                
                try {
                    ndef.connect()
                    ndef.writeNdefMessage(message)
                    ndef.close()
                    true
                } catch (e: Exception) {
                    Log.e("NFC", "Erro ao escrever no tag", e)
                    false
                }
            }
            NdefFormatable.get(tag) != null -> {
                val formatable = NdefFormatable.get(tag)
                val json = PlayerCard.toJson(playerCard)
                val record = NdefRecord.createMime(
                    "application/com.example.investcitynfc",
                    json.toByteArray(StandardCharsets.UTF_8)
                )
                val message = NdefMessage(arrayOf(record))
                
                try {
                    formatable?.connect()
                    formatable?.format(message)
                    formatable?.close()
                    true
                } catch (e: Exception) {
                    Log.e("NFC", "Erro ao formatar tag", e)
                    false
                }
            }
            else -> {
                Log.e("NFC", "Tag não é NDEF ou formatável")
                false
            }
        }
    }

    fun clearNfcTag(tag: Tag): Boolean {
        val ndef = Ndef.get(tag)
        val ndefFormatable = NdefFormatable.get(tag)
        
        Log.d("NFC", "Iniciando limpeza do cartão. NDEF=${ndef != null}, NDEFFormatable=${ndefFormatable != null}")
        
        return when {
            ndef != null -> {
                if (!ndef.isWritable) {
                    Log.e("NFC", "Tag não é gravável")
                    return false
                }
                
                try {
                    Log.d("NFC", "Conectando ao cartão NDEF")
                    ndef.connect()
                    
                    // Criar uma mensagem NDEF vazia com um record MIME vazio
                    val emptyRecord = NdefRecord.createMime(
                        "application/com.example.investcitynfc",
                        "".toByteArray(StandardCharsets.UTF_8)
                    )
                    val message = NdefMessage(arrayOf(emptyRecord))
                    
                    Log.d("NFC", "Escrevendo mensagem vazia no cartão")
                    ndef.writeNdefMessage(message)
                    ndef.close()
                    Log.d("NFC", "Cartão NDEF limpo com sucesso")
                    true
                } catch (e: Exception) {
                    Log.e("NFC", "Erro ao limpar tag NDEF: ${e.message}", e)
                    try {
                        ndef.close()
                    } catch (closeError: Exception) {
                        Log.e("NFC", "Erro ao fechar conexão NDEF: ${closeError.message}")
                    }
                    false
                }
            }
            ndefFormatable != null -> {
                try {
                    Log.d("NFC", "Conectando ao cartão formatável")
                    ndefFormatable.connect()
                    
                    // Verificar se o tag está protegido
                    if (ndefFormatable.isConnected) {
                        Log.d("NFC", "Tag conectado com sucesso")
                    } else {
                        Log.e("NFC", "Falha ao conectar ao tag")
                        return false
                    }
                    
                    // Tentar formatar com uma mensagem mínima
                    val emptyRecord = NdefRecord.createMime(
                        "application/com.example.investcitynfc",
                        "".toByteArray(StandardCharsets.UTF_8)
                    )
                    val message = NdefMessage(arrayOf(emptyRecord))
                    
                    Log.d("NFC", "Formatando cartão com mensagem vazia")
                    try {
                        // Tentar formatar com uma mensagem mínima
                        ndefFormatable.format(message)
                        Log.d("NFC", "Formatação bem-sucedida")
                    } catch (formatError: IOException) {
                        Log.e("NFC", "Erro na formatação: ${formatError.message}")
                        
                        // Tentar formatar sem mensagem
                        try {
                            Log.d("NFC", "Tentando formatar sem mensagem")
                            ndefFormatable.format(null)
                            Log.d("NFC", "Formatação sem mensagem bem-sucedida")
                        } catch (e: Exception) {
                            Log.e("NFC", "Falha na formatação sem mensagem: ${e.message}")
                            
                            // Tentar formatar com uma mensagem diferente
                            try {
                                Log.d("NFC", "Tentando formatar com mensagem alternativa")
                                val alternativeRecord = NdefRecord.createTextRecord(null, "")
                                val alternativeMessage = NdefMessage(arrayOf(alternativeRecord))
                                ndefFormatable.format(alternativeMessage)
                                Log.d("NFC", "Formatação alternativa bem-sucedida")
                            } catch (e2: Exception) {
                                Log.e("NFC", "Falha na formatação alternativa: ${e2.message}")
                                
                                // Tentar formatar com uma mensagem diferente
                                try {
                                    Log.d("NFC", "Tentando formatar com mensagem alternativa 2")
                                    val alternativeRecord2 = NdefRecord.createUri("https://example.com")
                                    val alternativeMessage2 = NdefMessage(arrayOf(alternativeRecord2))
                                    ndefFormatable.format(alternativeMessage2)
                                    Log.d("NFC", "Formatação alternativa 2 bem-sucedida")
                                } catch (e3: Exception) {
                                    Log.e("NFC", "Falha na formatação alternativa 2: ${e3.message}")
                                    
                                    // Tentar formatar com uma mensagem diferente
                                    try {
                                        Log.d("NFC", "Tentando formatar com mensagem alternativa 3")
                                        val alternativeRecord3 = NdefRecord.createExternal(
                                            "com.example.investcitynfc",
                                            "empty",
                                            "".toByteArray(StandardCharsets.UTF_8)
                                        )
                                        val alternativeMessage3 = NdefMessage(arrayOf(alternativeRecord3))
                                        ndefFormatable.format(alternativeMessage3)
                                        Log.d("NFC", "Formatação alternativa 3 bem-sucedida")
                                    } catch (e4: Exception) {
                                        Log.e("NFC", "Falha na formatação alternativa 3: ${e4.message}")
                                        
                                        // Tentar formatar com uma mensagem diferente
                                        try {
                                            Log.d("NFC", "Tentando formatar com mensagem alternativa 4")
                                            val alternativeRecord4 = NdefRecord.createApplicationRecord("com.example.investcitynfc")
                                            val alternativeMessage4 = NdefMessage(arrayOf(alternativeRecord4))
                                            ndefFormatable.format(alternativeMessage4)
                                            Log.d("NFC", "Formatação alternativa 4 bem-sucedida")
                                        } catch (e5: Exception) {
                                            Log.e("NFC", "Falha na formatação alternativa 4: ${e5.message}")
                                            throw e5
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    ndefFormatable.close()
                    Log.d("NFC", "Cartão formatável limpo com sucesso")
                    true
                } catch (e: Exception) {
                    Log.e("NFC", "Erro ao formatar tag: ${e.message}", e)
                    try {
                        ndefFormatable.close()
                    } catch (closeError: Exception) {
                        Log.e("NFC", "Erro ao fechar conexão formatável: ${closeError.message}")
                    }
                    false
                }
            }
            else -> {
                Log.e("NFC", "Tag não é NDEF ou formatável")
                false
            }
        }
    }
} 