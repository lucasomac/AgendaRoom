package br.com.lucolimac.agendaroom.adapter

import br.com.lucolimac.agendaroom.data.Contact

interface OnContactListener {
    fun onContactClick(contact: Contact)
}