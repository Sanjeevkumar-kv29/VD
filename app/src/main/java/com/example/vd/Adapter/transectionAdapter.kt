package com.example.Adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.vd.R

class transectionAdapter(private val context: Activity, private val amount: ArrayList<String>, private val trnsctndate: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.cstmtransectionlist, amount) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.cstmtransectionlist, null, true)

        val amountText = rowView.findViewById(R.id.amount) as TextView
        val trnsctndateText = rowView.findViewById(R.id.trnsctnDate) as TextView


        amountText.text = amount[position]
        trnsctndateText.text = trnsctndate[position]
        return rowView
    }
}