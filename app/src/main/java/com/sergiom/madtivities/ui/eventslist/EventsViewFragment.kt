package com.sergiom.madtivities.ui.eventslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergiom.madtivities.R
import com.sergiom.madtivities.utils.Resource
import com.sergiom.madtivities.databinding.FragmentEventsViewBinding
import com.sergiom.madtivities.ui.eventdetail.EventDetailFragment
import com.sergiom.madtivities.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsViewFragment : Fragment(), EventsAdapter.EventItemListener {

    private var binding: FragmentEventsViewBinding by autoCleared()
    private val viewModel: EventsViewViewModel by viewModels()
    private lateinit var adapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEventsViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = EventsAdapter(this)
        binding.eventsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.eventsRv.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.events.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty()) {
                        if (!it.data.isNullOrEmpty()) adapter.setItems(ArrayList(it.data))
                    }
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onClickedEvent(eventUid: String) {
        val fragment = EventDetailFragment.newInstance()
        fragment.arguments = bundleOf("uid" to eventUid)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.addToBackStack("eventDetail")
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventsViewFragment()
    }
}