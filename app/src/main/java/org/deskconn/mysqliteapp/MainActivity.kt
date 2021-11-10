package org.deskconn.mysqliteapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit var addName: Button
    private lateinit var enterName: EditText
    private lateinit var enterAge: EditText
    private lateinit var printName: Button
    private lateinit var itemsListView: ListView

    private val dbHelper = DBHelper(this, null)

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addName = findViewById(R.id.addName)
        enterName = findViewById(R.id.enterName)
        enterAge = findViewById(R.id.enterAge)
        printName = findViewById(R.id.printName)
        itemsListView = findViewById(R.id.itemsListView)

        setUpListData()
        Log.e("work", setUpListData().toString())
        // below we have created
        // a new DBHelper class,
        // and passed context to it

        // below code is to add on click
        // listener to our add name button
        addName.setOnClickListener {view ->
            addRecord(view)
        }

        printName.setOnClickListener {
            setUpListData()
            Toast.makeText(this, setUpListData().toString(), Toast.LENGTH_SHORT).show()
        }
        // below code shows the Inserted data using clickListener
        /*printName.setOnClickListener {

            val person: MutableList<SQLiteModelClass> = dbHelper.getName()
            //val personId = Array(person.size){"0"}
            val personsArray: Array<SQLiteModelClass> = Array(person.size) { return@Array person as SQLiteModelClass}
            //Here in for loop it as declaring the two variables together (index, e)

            *//*for ((index, e) in person.withIndex()) { //with index method is used for increasing the index value by 1 as index++

                sq?.Age = e.Age
                sq?.Id = e.Id
                sq?.Name = e.Name
                personsArray[index] = sq
                //personArrayAge[index] = e.Age.toString()
            }*//*

            //val empArrayId = Array(person.size){"0"}
            //val empArrayName = Array(person.size){"null"}
            //val empArrayAge = Array(person.size){"0"}
            person.withIndex().forEach { (index, e) ->
                val sq = SQLiteModelClass()
                //empArrayId[index] = e.Id.toString()
                sq.Id = e.Id
                sq.Name = e.Name
                sq.Age = e.Age
                person[index] = sq
                //empArrayName[index] = e.Name
                //empArrayAge[index] = e.Age.toString()
            }

            val myListAdapter = ListAdapter(this, person)
            itemsListView.adapter = myListAdapter
        }*/

    }

    private fun addRecord(view: View){
        // creating variables for values
        // in name and age edit texts

        val name = enterName.text.toString()
        val age = enterAge.text.toString()

        if (name.isEmpty() && age.isEmpty()) {
            enterName.error = "Name is required"
            enterAge.error = "Age is required"
        } else if (name.isEmpty()) {
            enterName.error = "Name is required"
        } else if (age.isEmpty()) {
            enterAge.error = "Age is required"
        } else {
            val status = dbHelper.addName(SQLiteModelClass(0,name, Integer.parseInt(age)))

            if (status > -1) {
                // Toast to message on the screen
                Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
                // at last, clearing edit texts
                enterName.text.clear()
                enterAge.text.clear()

                setUpListData()
            } else {
                // Toast to message on the screen
                Toast.makeText(this, "Not added to database", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpListData(){
       if (getItemList().size > 0){
           itemsListView.visibility = View.VISIBLE

           val itemAdapter = ListAdapter(this, getItemList())
           itemsListView.adapter = itemAdapter
       } else {
           itemsListView.visibility = View.GONE
       }
   }

    private fun getItemList(): ArrayList<SQLiteModelClass> {
        //val dbHelper = DBHelper(this, null)
        return dbHelper.getName()
    }

    fun updateRecordDialog(modelClass: SQLiteModelClass) {

        /*val updateDialog = Dialog(this, R.style.Theme_AppCompat_Dialog)
        updateDialog.setCancelable(false)*/

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val etUpdateName: EditText = dialogView.findViewById(R.id.updateName)
        val etUpdateAge: EditText = dialogView.findViewById(R.id.updateAge)

        etUpdateName.setText(modelClass.Name)
        etUpdateAge.setText(modelClass.Age.toString())
        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setIcon(R.drawable.edit_icon)


        dialogBuilder.setPositiveButton(
            "Update",
            DialogInterface.OnClickListener { dialogInterface, _ ->
                val updateName = etUpdateName.text.toString()
                val updateAge = etUpdateAge.text.toString()

                //val dbHelper = DBHelper(this, null)

                if (updateName.isEmpty() && updateAge.isEmpty()) {
                    etUpdateName.error = "Name is required"
                    etUpdateAge.error = "Age is required"
                } else if (updateName.isEmpty()) {
                    etUpdateName.error = "Name is required"
                } else if (updateAge.isEmpty()) {
                    etUpdateAge.error = "Age is required"
                } else {
                    val status = dbHelper.updatePerson(
                        SQLiteModelClass(modelClass.Id,updateName, Integer.parseInt(updateAge)))

                    if (status > -1) {
                        // Toast to message on the screen
                        Toast.makeText(this, "Id: ${modelClass.Id} record is updated", Toast.LENGTH_LONG).show()
                        // at last, clearing edit texts
                        etUpdateName.text.clear()
                        etUpdateAge.text.clear()

                        setUpListData()
                    } else {
                        // Toast to message on the screen
                        Toast.makeText(this, "Id: ${modelClass.Id} record is not updated", Toast.LENGTH_LONG).show()
                    }
                }
               /* if (updateName.isNotEmpty() && updateAge.isNotEmpty()) {

                    if (status > -1) {
                        Toast.makeText(this, "Record updated!", Toast.LENGTH_LONG).show()
                        setUpListData()
                        dialogInterface.dismiss()
                    }
                } else {
                    Toast.makeText(this, "Name and Age can not be blank", Toast.LENGTH_LONG).show()
                }*/
            })
        dialogBuilder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialogBuilder.show()
    }

    fun deleteDialogRecord(modelClass: SQLiteModelClass) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure? Do you want to delete ${modelClass.Name}?")

        builder.setIcon(R.drawable.ic_baseline_warning_24)

        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            //val dbHelper = DBHelper(this, null)

            val status = dbHelper.deletePerson(SQLiteModelClass(modelClass.Id,"", modelClass.Age))

            if (status > -1) {
                // Toast to message on the screen
                Toast.makeText(this, "Record Deleted from DataBase successfully", Toast.LENGTH_LONG)
                    .show()
                setUpListData()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
