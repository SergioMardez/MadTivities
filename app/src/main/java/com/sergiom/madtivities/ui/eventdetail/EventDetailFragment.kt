package com.sergiom.madtivities.ui.eventdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sergiom.madtivities.R
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
import com.sergiom.madtivities.databinding.FragmentEventDetailBinding
import com.sergiom.madtivities.utils.Resource
import com.sergiom.madtivities.utils.Utils
import com.sergiom.madtivities.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EventDetailFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentEventDetailBinding by autoCleared()
    private val viewModel: EventDetailViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private var latitude = 40.4378698
    private var longitude = -3.8196216
    private var title = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("uid")?.let { viewModel.start(it) }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.event.observe(viewLifecycleOwner, {
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
        val date = Utils().parseDate(event.dtstart)
        binding.dateDetail.text = context?.getString(R.string.date_time_text, date[0], date[1])
        binding.placeDetail.text = context?.getString(R.string.place_text, event.eventLocation)

        if (event.price.isEmpty() && event.free == 0) {
            binding.priceDetail.text = context?.getString(R.string.no_price_text)
        } else {
            binding.priceDetail.text = context?.getString(
                R.string.price_text,
                if (event.free == 0) event.price else context?.getString(R.string.free_text)
            )
        }

        if (event.description.isEmpty()) {
            binding.nestedScroll.visibility = View.INVISIBLE
            binding.descriptionDetail.visibility = View.GONE
        } else {
            binding.descriptionDetail.text =
                context?.getString(R.string.description_text, event.description)
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.buttonSeeOnInternet.setOnClickListener {
            if (event.link.isNotEmpty()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(browserIntent)
            } else {
                Toast.makeText(context, "No hay link disponible", Toast.LENGTH_SHORT).show()
            }
        }

        try {
            title = event.eventLocation
            longitude = event.longitude!!
            latitude = event.latitude!!
            val marker = LatLng(latitude, longitude)
            addMarkerToMap(marker)
        } catch (ignored: NullPointerException) {
            // Add a marker in Sydney and move the camera
            title = "Madrid"
            val marker = LatLng(latitude, longitude)
            addMarkerToMap(marker)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun addMarkerToMap(marker: LatLng) {
        mMap.addMarker(
            MarkerOptions()
                .position(marker)
                .title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15f))
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventDetailFragment()
    }
}