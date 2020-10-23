package edu.bluejack20_1.gogames.itad.api.entity

import java.util.*

data class Deal(
    val plain: String,
    val title: String,
    val price_new: Float,
    val price_old: Float,
    val price_cut: Int,
    val added: Any,
    val expiry: Any,
    val shop: Shop,
    val urls: Url
)