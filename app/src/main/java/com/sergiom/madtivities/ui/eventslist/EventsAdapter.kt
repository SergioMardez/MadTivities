package com.sergiom.madtivities.ui.eventslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sergiom.madtivities.R
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
import com.sergiom.madtivities.databinding.EventItemBinding

class EventsAdapter(private val listener: EventItemListener) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    interface EventItemListener {
        fun onClickedEvent(eventUid: String)
    }

    private val items = ArrayList<MadEventItemDataBase>()

    fun setItems(items: ArrayList<MadEventItemDataBase>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding: EventItemBinding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) = holder.bind(items[position])

    class EventViewHolder(private val itemBinding: EventItemBinding, private val listener: EventItemListener) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var event: MadEventItemDataBase

        init {
            itemBinding.root.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: MadEventItemDataBase) {
            this.event = item
            itemBinding.eventTitle.text = item.title
            val dateString = item.dtstart.split(" ")
            itemBinding.eventDate.text = itemBinding.root.context.getString(R.string.date_text, dateString[0])

            if(item.free == 1) {
                itemBinding.free.visibility = View.VISIBLE
            }
        }

        override fun onClick(v: View?) {
            listener.onClickedEvent(event.uid)
        }
    }
}