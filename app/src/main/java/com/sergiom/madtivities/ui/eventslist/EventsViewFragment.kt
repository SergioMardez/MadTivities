package com.sergiom.madtivities.ui.eventslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergiom.madtivities.R
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
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
    private var isFavListShow = false
    private var eventsList = arrayListOf<MadEventItemDataBase>()
    private var favouriteEventsList = arrayListOf<MadEventItemDataBase>()

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
        setUpListeners()
    }

    override fun onResume() {
        super.onResume()
        setListButtons()
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
                        eventsList = ArrayList(it.data)
                        showEvents()
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

        viewModel.favourites.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty()) {
                        favouriteEventsList = ArrayList(it.data)
                        showEvents()
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

    private fun setUpListeners() {
        binding.favButtonFav.setOnClickListener {
            adapter.setItems(favouriteEventsList)
            isFavListShow = true
            setListButtons()
            if (favouriteEventsList.isEmpty()) {
                Toast.makeText(context, "No hay favoritos aún", Toast.LENGTH_SHORT).show()
            }
        }

        binding.favButtonList.setOnClickListener {
            isFavListShow = false
            adapter.setItems(eventsList)
            setListButtons()
        }
    }

    private fun setListButtons() {
        if (isFavListShow) {
            binding.favButtonFav.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.favorite_red_shape)
            binding.favButtonList.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.list_white)
        } else {
            binding.favButtonList.background = ContextCompat.getDrawable(requireContext(), R.drawable.list_red)
            binding.favButtonFav.background = ContextCompat.getDrawable(requireContext(), R.drawable.favorite_white_shape)
        }
    }

    private fun showEvents() {
        if (isFavListShow) {
            adapter.setItems(favouriteEventsList)
        } else {
            adapter.setItems(eventsList)
        }
    }

    override fun onClickedEvent(eventUid: String) {
        val fragment = EventDetailFragment.newInstance()
        fragment.arguments = bundleOf("uid" to eventUid)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.addToBackStack("eventDetail")
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    override fun onFavouriteClick(event: MadEventItemDataBase) {
        if (favouriteEventsList.size == 1 && favouriteEventsList[0].uid == event.uid) {
            favouriteEventsList.clear()
            showEvents()
        }
        viewModel.saveFavourite(event)
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventsViewFragment()
    }
}