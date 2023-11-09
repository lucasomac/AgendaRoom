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
import br.com.lucolimac.agendaroom.databinding.FragmentContactRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactRegisterFragment : Fragment() {
    private var _binding: FragmentContactRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.register_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.actionSaveContact -> {
                        val name = binding.commonLayout.editTextName.text.toString()
                        val phone = binding.commonLayout.editTextPhone.text.toString()
                        val email = binding.commonLayout.editTextEmail.text.toString()
                        val contact = Contact(0, name, phone, email)
                        val db = ContactDatabase.getDatabase(requireActivity().applicationContext)
                        CoroutineScope(Dispatchers.IO).launch {
                            db.contactDAO().createContact(contact)
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