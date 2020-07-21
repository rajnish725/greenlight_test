package com.fycus.models.response

data class FaqMainModel(
    val created_at: String,
    val description: String,
    val id: Int,
    val status: Int,
    val title: String,
    val type: String,
    val updated_at: String
)