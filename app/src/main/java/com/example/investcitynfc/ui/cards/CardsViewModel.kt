package com.example.investcitynfc.ui.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.investcitynfc.model.Card
import com.example.investcitynfc.model.CardRepository
import com.example.investcitynfc.model.InterestQuestion
import com.example.investcitynfc.model.InterestRepository
import com.example.investcitynfc.model.InterestType

class CardsViewModel : ViewModel() {
    private val cardRepository = CardRepository()
    private val interestRepository = InterestRepository()

    private val _currentQuestion = MutableLiveData<InterestQuestion>()
    val currentQuestion: LiveData<InterestQuestion> = _currentQuestion

    fun drawRandomCard(): Card {
        return if (Math.random() < 0.5) {
            cardRepository.getRandomFortuneCard()
        } else {
            cardRepository.getRandomMisfortuneCard()
        }
    }

    fun drawSimpleQuestion() {
        _currentQuestion.value = interestRepository.getRandomSimpleQuestion()
    }

    fun drawCompoundQuestion() {
        _currentQuestion.value = interestRepository.getRandomCompoundQuestion()
    }
} 