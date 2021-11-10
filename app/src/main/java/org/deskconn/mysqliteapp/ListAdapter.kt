package org.deskconn.mysqliteapp

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView

class ListAdapter(
    private val context: Activity,
    private val items: ArrayList<SQLiteModelClass>
) : ArrayAdapter<String>(context, R.layout.list_items) {

    @SuppressLint("SetTextI18n", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val item = items[position]

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_items, null, true)

        val nameTextView = rowView.findViewById<TextView>(R.id.Name)
        val ageTextView = rowView.findViewById<TextView>(R.id.Age)
        val updateButton = rowView.findViewById<ImageButton>(R.id.editRecord)
        val deleteButton = rowView.findViewById<ImageButton>(R.id.deleteRecord)

        nameTextView.text = "Name: ${item.Name}"
        ageTextView.text = "Age: ${item.Age}"

        updateButton.setOnClickListener { view ->
            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
        }

        deleteButton.setOnClickListener { view ->
            if (context is MainActivity) {
                context.deleteDialogRecord(item)
            }
        }
        return rowView
    }

    override fun getCount(): Int {
        return items.size
    }
}