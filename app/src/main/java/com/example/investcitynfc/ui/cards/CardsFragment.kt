package com.example.investcitynfc.ui.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.investcitynfc.databinding.FragmentCardsBinding
import com.example.investcitynfc.model.CardType
import com.example.investcitynfc.model.CardEffect
import com.example.investcitynfc.model.InterestType
import com.google.android.material.card.MaterialCardView

class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CardsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        setupCardDrawing()
        setupInterestQuestions()
    }

    private fun setupCardDrawing() {
        binding.drawCardButton.setOnClickListener {
            val card = viewModel.drawRandomCard()
            displayCard(card)
        }
    }

    private fun setupInterestQuestions() {
        binding.drawSimpleButton.setOnClickListener {
            viewModel.drawSimpleQuestion()
        }

        binding.drawCompoundButton.setOnClickListener {
            viewModel.drawCompoundQuestion()
        }

        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            displayQuestion(question)
        }
    }

    private fun displayCard(card: com.example.investcitynfc.model.Card) {
        binding.cardEmojiTextView.text = card.emoji
        binding.cardTitleTextView.text = card.title
        binding.cardDescriptionTextView.text = card.description

        // Display amount or effect description
        binding.cardAmountText.text = when (card.effect) {
            is CardEffect.AddMoney -> "+R$ ${card.effect.amount}"
            is CardEffect.PayMoney -> "-R$ ${card.effect.amount}"
            is CardEffect.ReceiveFromPlayer -> "Receber R$ ${card.effect.amount} de outro jogador"
            is CardEffect.AddRentMonth -> "Ganhar 1 mês de aluguel"
            is CardEffect.DoubleRent -> "Dobrar aluguel"
            is CardEffect.IncreaseRentRate -> "Aumentar aluguel em ${(card.effect.rate * 100).toInt()}%"
            is CardEffect.SkipNextMisfortune -> "Pular próximo revés"
            is CardEffect.HalveRent -> "Reduzir aluguel pela metade"
            is CardEffect.LoseRentMonth -> "Perder 1 mês de aluguel"
            is CardEffect.ZeroRent -> "Zerar aluguel"
            is CardEffect.BlockRent -> "Bloquear aluguel por ${card.effect.rounds} rodadas"
        }

        // Update card background color based on type
        binding.cardView.setCardBackgroundColor(
            when (card.type) {
                CardType.FORTUNE -> 0xFF4CAF50.toInt() // Green
                CardType.MISFORTUNE -> 0xFFF44336.toInt() // Red
            }
        )
    }

    private fun displayQuestion(question: com.example.investcitynfc.model.InterestQuestion) {
        binding.questionTextView.text = question.question
        binding.answerTextView.text = "R$ %.2f".format(question.answer)
        binding.answerTextView.visibility = View.VISIBLE

        // Update card background color based on type
        binding.questionCardView.setCardBackgroundColor(
            when (question.type) {
                InterestType.SIMPLE -> 0xFF2196F3.toInt() // Blue
                InterestType.COMPOUND -> 0xFF9C27B0.toInt() // Purple
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 