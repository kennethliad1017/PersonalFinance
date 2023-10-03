package com.differentshadow.personalfinance.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.differentshadow.personalfinance.UserPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance().toBuilder().setPreferCurrency(Currency.getInstance(
        Locale.getDefault()).currencyCode)
        .setPreferLanguage("English")
        .setDarkThemeConfig(UserPreferences.DarkThemeConfigProto.SYSTEM_DEFAULT).build()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}