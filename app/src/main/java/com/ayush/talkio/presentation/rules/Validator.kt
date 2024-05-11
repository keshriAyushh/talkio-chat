package com.ayush.talkio.presentation.rules

object Validator {

    fun validatePhone(phone: String): ValidationResult {
        return ValidationResult(
            phone.isNotEmpty() && phone.isNotBlank() &&
                    phone.length == 10
        )
    }

}