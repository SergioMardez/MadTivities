package com.sergiom.madtivities.ui.eventdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sergiom.madtivities.R
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
import com.sergiom.madtivities.databinding.FragmentEventDetailBinding
import com.sergiom.madtivities.utils.Resource
import com.sergiom.madtivities.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private var binding: FragmentEventDetailBinding by autoCleared()
    private val viewModel: EventDetailViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("uid")?.let { viewModel.start(it) }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.event.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    bindCharacter(it.data!!)
                }

                Resource.Status.ERROR -> {}
                Resource.Status.LOADING -> {}
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindCharacter(event: MadEventItemDataBase) {
        binding.titleDetail.text = event.title
        binding.dateDetail.text = context?.getString(R.string.date_time_text, event.dtstart)
        binding.placeDetail.text = context?.getString(R.string.place_text, event.eventLocation)
        binding.priceDetail.text = context?.getString(R.string.price_text, if(event.free == 0) event.price else "Gratuito")
        binding.descriptionDetail.text = context?.getString(R.string.description_text, event.description)

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.buttonSeeOnInternet.setOnClickListener {
            if (!event.link.isNullOrEmpty()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(browserIntent)
            } else {
                Toast.makeText(context, "No hay link disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventDetailFragment()
    }
}