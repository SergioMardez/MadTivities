package com.sergiom.madtivities.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sergiom.madtivities.R
import com.sergiom.madtivities.databinding.WelcomeFragmentBinding
import com.sergiom.madtivities.ui.eventslist.EventsViewFragment
import com.sergiom.madtivities.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var binding: WelcomeFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonstart.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, EventsViewFragment())
            transaction.commit()
        }
    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }

}