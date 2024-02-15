package model.storage

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class CardPayment() : RealmObject {

    @PrimaryKey var _id: RealmUUID = RealmUUID.random()
    var cardId: String = ""
    var amount: Double = 0.0
    var paymentDate: Long = 0L
    var note: String = ""
}