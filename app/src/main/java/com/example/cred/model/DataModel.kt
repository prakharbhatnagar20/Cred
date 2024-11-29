package com.example.cred.model

data class ApiResponse(
    val items: List<Item>
)

data class Item(
    val open_state: OpenState,
    val closed_state: ClosedState,
    val cta_text: String
)

data class OpenState(
    val body: Body
)

data class ClosedState(
    val body: ClosedBody
)

data class Body(
    val title: String,
    val subtitle: String,
    val card: Card? = null,
    val items: List<RepaymentItem>? = null,
    val footer: String
)

data class Card(
    val header: String,
    val description: String,
    val max_range: Int,
    val min_range: Int
)

data class RepaymentItem(
    val emi: String,
    val duration: String,
    val title: String,
    val subtitle: String,
    val tag: String? = null
)

data class ClosedBody(
    val key1: String,
    val key2: String? = null
)

data class BankItem(
    val title: String,       // e.g., "HDFC BANK"
    val subtitle: String?    // e.g., Account number or identifier
)
