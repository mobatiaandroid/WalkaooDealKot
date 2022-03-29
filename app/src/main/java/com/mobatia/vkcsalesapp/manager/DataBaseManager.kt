/**
 *
 */
package com.mobatia.vkcsalesapp.manager

import android.content.Context
import android.database.Cursor
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants

/**
 * @author Archana S
 */
class DataBaseManager(private val context: Context) : VKCDbConstants {
    private var mySQLiteAdapter: SQLiteAdapter? = null
    fun removeFromDb(
        tableName: String?, fieldName: String,
        fieldValue: String
    ) {
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToWrite()
        mySQLiteAdapter!!.makeEmpty(tableName, "$fieldName==$fieldValue")
        mySQLiteAdapter!!.close()
    }

    fun deleteTitle(name: String?) {
        //return db.delete(DATABASE_TABLE, KEY_NAME + "=" + name, null) > 0;
    }

    fun fetchFromDB(
        columns: Array<String>, tableName: String?,
        constraintFieldName: String?
    ): Cursor {
        var cursor: Cursor? = null
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToRead()
        cursor = mySQLiteAdapter!!.queueAll(tableName, columns, "", null)
        cursor.moveToPosition(0)
        mySQLiteAdapter!!.close()
        return cursor
    }


    fun fetchFromDB(
        columns: Array<String>, tableName: String?
    ): Cursor {
        var cursor: Cursor? = null
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToRead()
        cursor = mySQLiteAdapter!!.queueAll(tableName, columns, "", null)
        cursor.moveToPosition(0)
        mySQLiteAdapter!!.close()
        return cursor
    }
    fun insertIntoDb(tableName: String?, data: Array<Array<String>>) {
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToWrite()
        mySQLiteAdapter!!.insert(data, tableName)
        mySQLiteAdapter!!.close()
    }

    fun insertIntoDbNew(tableName: String?, data: Array<Array<String?>>) {
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToWrite()
        mySQLiteAdapter!!.insertNew(data, tableName)
        mySQLiteAdapter!!.close()
    }

    fun updateTableRow(
        tableName: String?, data: Array<Array<String?>?>?,
        constrain: Array<Array<String?>?>?
    ) {
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToWrite()
        mySQLiteAdapter!!.update(data, tableName, constrain)
        mySQLiteAdapter!!.close()
    }

    fun removeDb(tableName: String?) {
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToWrite()
        mySQLiteAdapter!!.makeEmpty(tableName)
        mySQLiteAdapter!!.close()
    }

    fun updateCartQty(tableName: String?, data: Array<Array<String?>?>?) {
        mySQLiteAdapter = SQLiteAdapter(context, VKCDbConstants.DBNAME)
        mySQLiteAdapter!!.openToWrite()
        //mySQLiteAdapter.makeEmpty(tableName);
        mySQLiteAdapter!!.close()
    }
}