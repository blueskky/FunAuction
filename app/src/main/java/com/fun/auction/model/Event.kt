package com.`fun`.auction.model

class Event(var type: String = "") {


    companion object {
        const val SET_CASH = "set_cash_account"
        const val SElECT_CASH = "select_default_cash_account"
        const val CASH_OUT = "cash_out_success"
        const val BOX_UPDATE = "box_entity_resell"
        const val BIND_CODE = "bind_code"
        const val CHARGE_SUCCESS = "charge_success"
        const val PEEK_SUCCESS = "peek_success"
    }

}