package br.com.lucolimac.agendaroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import br.com.lucolimac.agendaroom.ContactDatabase
import br.com.lucolimac.agendaroom.R
import br.com.lucolimac.agendaroom.data.Contact
import br.com.lucolimac.agendaroom.databinding.FragmentContactDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactDetailFragment : Fragment() {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var contact: Contact
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contact = requireArguments().getParcelable("contact", Contact::class.java) as Contact
        with(binding.commonLayout) {
            editTextName.setText(contact.name)
            editTextPhone.setText(contact.phone)
            editTextEmail.setText(contact.email)
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.actionChangeContact -> {
                        val db = ContactDatabase.getDatabase(requireActivity().applicationContext)
                        val contactUpdate = Contact(
                            contact.id, contact.name, contact.phone, contact.email
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            db.contactDAO().updateContact(contactUpdate)
                        }
                        findNavController().popBackStack()
                        true
                    }

                    R.id.actionDeleteContact -> {
                        val db = ContactDatabase.getDatabase(requireActivity().applicationContext)
                        CoroutineScope(Dispatchers.IO).launch {
                            db.contactDAO().deleteContact(contact)
                        }
                        findNavController().popBackStack()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}