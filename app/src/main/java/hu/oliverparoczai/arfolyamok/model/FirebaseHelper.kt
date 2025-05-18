package hu.oliverparoczai.arfolyamok.model

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseHelper {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("currencyRates")

    fun addCurrencyRate(currencyRate: CurrencyRate) {
        Log.d("FirebaseHelper", "addCurrencyRate called!")
        val id = databaseReference.push().key
        id?.let {
            currencyRate.id = it
            databaseReference.child(it).setValue(currencyRate)
        }
    }

    fun getLastNCurrencyRates(currencyCode: String, limit: Int, listener: ValueEventListener) {
        databaseReference.orderByChild("currencyCode").equalTo(currencyCode)
            .limitToLast(limit)
            .addListenerForSingleValueEvent(listener)
    }

    fun getOrderedCurrencies(currencyCode: String, sortBy: String, listener: ValueEventListener) {
        databaseReference.orderByChild(sortBy).equalTo(currencyCode)
            .addListenerForSingleValueEvent(listener)
    }

    fun getCurrencyValuesAbove(currencyCode: String, minValue: Double, listener: ValueEventListener) {
        databaseReference.orderByChild("currencyCode").equalTo(currencyCode)
            .addListenerForSingleValueEvent(listener)
    }

    fun updateCurrencyRate(currencyRate: CurrencyRate) {
        Log.d("FirebaseHelper", "updateCurrencyRate called!")
        currencyRate.id?.let {
            databaseReference.child(it).setValue(currencyRate)
        }
    }

    fun deleteCurrencyRate(currencyRateId: String) {
        Log.d("FirebaseHelper", "deleteCurrencyRate called!")
        databaseReference.child(currencyRateId).removeValue()
    }

    fun getCurrencyRatesWithPagination(currencyCode: String, limit: Int, startAt: Int, listener: ValueEventListener) {
        databaseReference.orderByChild("currencyCode").equalTo(currencyCode)
            .startAt(startAt.toDouble())
            .limitToFirst(limit)
            .addListenerForSingleValueEvent(listener)
    }
}
