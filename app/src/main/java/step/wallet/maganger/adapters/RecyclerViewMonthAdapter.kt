package step.wallet.maganger.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import step.wallet.maganger.R
import java.util.*

class RecyclerViewMonthAdapter(
    // on below line we are passing variables
    private val courseList: Array<String>,
    private val context: Context,
    private var selectedItem: Int
) : RecyclerView.Adapter<RecyclerViewMonthAdapter.MonthViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener) {
        mListener = listener
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewMonthAdapter.MonthViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_row_month,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return MonthViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: RecyclerViewMonthAdapter.MonthViewHolder, @SuppressLint("RecyclerView") position: Int) {


        // on below line we are setting data to our views
        holder.monthName.text = courseList.get(position)
        if (selectedItem != position) {
            holder.monthName.setBackgroundResource(R.drawable.subcat_recycleview_background)
            holder.monthName.setTextColor(ContextCompat.getColor(context,R.color.olx_color_1))
        } else {
            holder.monthName.setBackgroundResource(R.drawable.subcat_recycleview_selected_background)
            holder.monthName.setTextColor(Color.WHITE)
        }

        holder.monthName.setOnClickListener {
            selectedItem = position
            notifyDataSetChanged()
            notifyItemChanged(position)
            mListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        // on below line we are
        // returning our size of our list
        return courseList.size
    }

    class MonthViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our views.
        val monthName: TextView = itemView.findViewById(R.id.dTrMonthAdapterTxt)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition)
            }
        }

    }

}