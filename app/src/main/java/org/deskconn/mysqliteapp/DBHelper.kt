package org.deskconn.mysqliteapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//** DBHelper class extended with SQLiteOpenHelper **//
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COl + " TEXT," +
                AGE_COL + " TEXT" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addName(modelClass: SQLiteModelClass): Long {

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        //values.put(ID_COL, person.Id)
        values.put(NAME_COl, modelClass.Name)
        values.put(AGE_COL, modelClass.Age)

        // all values are inserted into database
        val success = db.insert(TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()
        return success
    }

    // below method is to get
    // all data from our database
    @SuppressLint("Range", "Recycle")
    fun getName(): ArrayList<SQLiteModelClass> {

        //Create mutableList for adding data in list
        val nameList: ArrayList<SQLiteModelClass> = ArrayList<SQLiteModelClass>()
        //create a query variable to select the data from database
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase
        //we will execute the query using rawquery method to get the cursor object
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var age: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(ID_COL))
                name = cursor.getString(cursor.getColumnIndex(NAME_COl))
                age = cursor.getInt(cursor.getColumnIndex(AGE_COL))
                val personList = SQLiteModelClass(Id = id, Name = name, Age = age)
                nameList.add(personList)
            } while (cursor.moveToNext())
        }

        //Here need to close the database and cursor as well
        /*cursor.close()
        db.close()*/

        // below code returns a list of cursor objects to
        // read data from the database
        return nameList

    }

    fun updatePerson(modelClass: SQLiteModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        //contentValues.put(ID_COL, modelClass.Id)
        contentValues.put(NAME_COl, modelClass.Name) // EmpModelClass Name
        contentValues.put(AGE_COL, modelClass.Age) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_NAME, contentValues, ID_COL + "=" + modelClass.Id, null)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    //method to delete data
    fun deletePerson(modelClass: SQLiteModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID_COL, modelClass.Id) // EmpModelClass UserId
        // Deleting Row
//        val success = db.delete(TABLE_NAME, ID_COL + "=" + modelClass.Id, null)
        Log.e("TAG", " delete " + modelClass.Id)

        val success = db.delete(TABLE_NAME, ID_COL + " = " + modelClass.Id, null);
        Log.e("TAG", " delete " + success)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }


    companion object {
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "myDB"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "personTable"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val NAME_COl = "name"

        // below is the variable for age column
        val AGE_COL = "age"
    }
}
