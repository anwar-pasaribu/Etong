package model.storage

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Card() : RealmObject {

    @PrimaryKey var _id: RealmUUID = RealmUUID.random()
    var cardNumber: String = ""
    var billAmount: Double = 0.0
    var billMinAmount: Double = 0.0
    var billingDate: Long = 0L
    var billDueDate: Long = 0L
    var ownerId: String = ""

    constructor(ownerId: String = "") : this() {
        this.ownerId = ownerId
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Card) return false
        if (this._id != other._id) return false
        if (this.cardNumber != other.cardNumber) return false
        if (this.billAmount != other.billAmount) return false
        if (this.billMinAmount != other.billMinAmount) return false
        return true
    }
}