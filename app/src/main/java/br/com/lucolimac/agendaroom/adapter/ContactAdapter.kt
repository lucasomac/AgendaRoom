package br.com.lucolimac.agendaroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.lucolimac.agendaroom.data.Contact
import br.com.lucolimac.agendaroom.databinding.ContactCellBinding

class ContactAdapter(private val onContactListener: OnContactListener) :
    ListAdapter<Contact, ContactAdapter.ContactViewHolder>(diffCallback), Filterable {
    private lateinit var binding: ContactCellBinding
    private var originalList = arrayListOf<Contact>()
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ContactViewHolder {
        binding = ContactCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(private val view: ContactCellBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(contact: Contact) {
            view.name.text = contact.name
            view.phone.text = contact.name
            view.root.setOnClickListener {
                onContactListener.onContactClick(contact)
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (originalList.isEmpty()) originalList = ArrayList(currentList)
                return if (p0.isNullOrEmpty()) FilterResults().apply { values = originalList }
                else {
                    FilterResults().apply {
                        values = originalList.filter { it.name.contains(p0, true) }
                    }
                }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                submitList(p1?.values as ArrayList<Contact>)
            }
        }
    }
}