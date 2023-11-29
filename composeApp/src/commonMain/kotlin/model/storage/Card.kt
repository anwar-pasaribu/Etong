package model.storage

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Card : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var cardNumber: String = ""
    var billAmount: Double = 0.0
    var billMinAmount: Double = 0.0
    var billingDate: Long = 0L
    var billDueDate: Long = 0L
}