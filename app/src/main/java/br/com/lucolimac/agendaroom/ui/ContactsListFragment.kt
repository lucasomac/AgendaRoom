package br.com.lucolimac.agendaroom.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import br.com.lucolimac.agendaroom.ContactDatabase
import br.com.lucolimac.agendaroom.R
import br.com.lucolimac.agendaroom.adapter.ContactAdapter
import br.com.lucolimac.agendaroom.adapter.OnContactListener
import br.com.lucolimac.agendaroom.data.Contact
import br.com.lucolimac.agendaroom.databinding.FragmentContactsListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsListFragment : Fragment(), OnContactListener {
    private var _binding: FragmentContactsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactAdapter: ContactAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_contactsListFragment_to_registerFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_menu, menu)
                val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        contactAdapter.filter.filter(p0)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                TODO("Not yet implemented")
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val db = ContactDatabase.getDatabase(requireActivity().applicationContext)
        var contactsList: ArrayList<Contact>
        CoroutineScope(Dispatchers.IO).launch {
            contactsList = db.contactDAO().getContacts() as ArrayList<Contact>
            contactAdapter = ContactAdapter(this@ContactsListFragment).apply {
                submitList(contactsList)
            }
            withContext(Dispatchers.Main) {
                binding.recyclerview.adapter = contactAdapter
            }
        }
    }

    override fun onContactClick(contact: Contact) {
        val bundle = Bundle()
        bundle.putParcelable("contact", contact)
        findNavController().navigate(
            R.id.action_contactsListFragment_to_contactDetailFragment, bundle
        )
    }
}