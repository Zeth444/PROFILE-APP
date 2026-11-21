package com.example.profileapp.data.local

import com.example.profileapp.data.model.User

/** Sesión SOLO en memoria (token + usuario). No se escribe nada en disco. */
object SessionManager {
    var token: String? = null
        private set
    var user: User? = null
        private set

    fun save(token: String, user: User) {
        this.token = token
        this.user = user
    }

    fun clear() {
        token = null
        user = null
    }
}
