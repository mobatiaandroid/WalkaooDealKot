/**
 *
 */
package com.mobatia.vkcsalesapp.SQLiteServices

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.util.Log
import java.util.*

/**
 * @author Archana.S
 */
class SQLiteAdapter(private val context: Context, private val DB_NAME: String) {
    private var sqLiteHelper: SQLiteHelper? = null
    private var sqLiteDatabase: SQLiteDatabase? = null
    @Throws(SQLException::class)
    fun openToRead(): SQLiteAdapter {
        sqLiteHelper = SQLiteHelper(context, DB_NAME, null, DB_VERSION)
        sqLiteDatabase = sqLiteHelper!!.readableDatabase
        return this
    }

    @Throws(SQLException::class)
    fun openToWrite(): SQLiteAdapter {
        sqLiteHelper = SQLiteHelper(context, DB_NAME, null, DB_VERSION)
        sqLiteDatabase = sqLiteHelper!!.writableDatabase
        return this
    }

    fun executeDump() {}
    @Throws(SQLException::class)
    fun excuteRawQuery(query: String?) {
        sqLiteDatabase!!.execSQL(query)
    }

    fun close() {
        sqLiteHelper!!.close()
    }

    fun getCount(
        DB_TABLE: String,
        constraints: Array<Array<String>>
    ): Long {
        var constraint = ""
        val s: SQLiteStatement
        var j = 0
        while (j < constraints.size && constraints.size > 0) {
            constraint += if (j == constraints.size - 1 || constraints.size == 1) {
                constraints[j][0] + " = " + constraints[j][1]
            } else {
                (constraints[j][0] + " = " + constraints[j][1]
                        + " AND ")
            }
            j++
        }
        s = if (constraint !== "") {
            sqLiteDatabase!!.compileStatement(
                "SELECT COUNT(*) FROM "
                        + DB_TABLE + " WHERE " + constraint
            )
        } else {
            sqLiteDatabase!!.compileStatement(
                "SELECT COUNT(*) FROM "
                        + DB_TABLE
            )
        }
        return s.simpleQueryForLong()
    }

    fun update(
        content: Array<Array<String?>?>?, DB_TABLE: String?,
        constraints: Array<Array<String?>?>?
    ) {

        // String[][] cons={{"qid","2"}};
        // String[][]
        // data={{"type","1"},{"question","Term Fee"},{"option1","Op1"},{"option2","Op2"},{"option3","Op3"},{"option4","Op4"},{"option5","Op5"}};
        //
        // mySQLiteAdapter = new SQLiteAdapter(this);
        // mySQLiteAdapter.openToWrite();
        // mySQLiteAdapter.update(data, "questions", cons);
        // mySQLiteAdapter.openToRead();
        // mySQLiteAdapter.close();
        val contentValues = ContentValues()
        var constraint = ""
        val columns = getAllColumns(DB_TABLE)
        var i = 0
        for (COLUMN in columns) {
            i = 0
            while (i < content?.size!!) {
                if (content[i]!![0]?.equals(COLUMN, ignoreCase = true) == true
                    && (!content[i]!![1]?.equals("", ignoreCase = true)!! || content[i]!![1] == null)
                ) {
                    contentValues.put(COLUMN, content?.get(i)?.get(1))
                   // Log.v("column" + (i + 1), COLUMN + " " + content[i][1])
                }
                i++
            }
        }
        var j = 0
        while (j < constraints?.size!! && constraints?.size!! > 0) {
            constraint += if (j == constraints?.size - 1 || constraints?.size == 1) {
                constraints[j]!![0] + " = " + constraints[j]!![1]!!
            } else {
                (constraints[j]!![0]!! + " = " + constraints[j]!![1]!!
                        + " && ")
            }
            j++
        }
        try {
            sqLiteDatabase!!.beginTransaction()
            sqLiteDatabase!!.update(DB_TABLE, contentValues, constraint, null)
            sqLiteDatabase!!.setTransactionSuccessful()
        } catch (e: Exception) {
            // TODO: handle exception
            Log.v("Error:", "Error: $e")
        } finally {
            sqLiteDatabase!!.endTransaction()
        }
    }

    fun insertNew(content: Array<Array<String?>>, DB_TABLE: String?): Boolean {

        // String[][]data={{"qid","5"},{"surid","3"},{"type","1"},{"_id",""},{"question","What is your name?"},{"option1","Test1"},{"option2","Test2"},{"option3","Test3"},{"option4","Test4"},{"option5","Test5"}};
        // mySQLiteAdapter = new SQLiteAdapter(this);
        // mySQLiteAdapter.openToWrite();
        // mySQLiteAdapter.insert(data, "questions");
        // mySQLiteAdapter.openToRead(); mySQLiteAdapter.close();
        val contentValues = ContentValues()
        val columns = getAllColumns(DB_TABLE)
        var i = 0
        for (COLUMN in columns) {
            i = 0
            while (i < content.size) {
                if (content[i][0].equals(COLUMN, ignoreCase = true)
                    && (!content[i][1].equals("", ignoreCase = true) || content[i][1] == null)
                ) {
                    contentValues.put(COLUMN, content[i][1])
                    Log.v("column" + (i + 1), COLUMN + " " + content[i][1])
                }
                i++
            }
        }
        try {
            sqLiteDatabase!!.beginTransaction()
            return if (sqLiteDatabase!!.insert(DB_TABLE, null, contentValues) >= 0) {
                sqLiteDatabase!!.setTransactionSuccessful()
                sqLiteDatabase!!.endTransaction()
                true
            } else {
                sqLiteDatabase!!.setTransactionSuccessful()
                sqLiteDatabase!!.endTransaction()
                false
            }
        } catch (e: Exception) {
            Log.v("Error:", "Error: $e")
        }
        return false
    }
    fun insert(content: Array<Array<String>>, DB_TABLE: String?): Boolean {

        // String[][]data={{"qid","5"},{"surid","3"},{"type","1"},{"_id",""},{"question","What is your name?"},{"option1","Test1"},{"option2","Test2"},{"option3","Test3"},{"option4","Test4"},{"option5","Test5"}};
        // mySQLiteAdapter = new SQLiteAdapter(this);
        // mySQLiteAdapter.openToWrite();
        // mySQLiteAdapter.insert(data, "questions");
        // mySQLiteAdapter.openToRead(); mySQLiteAdapter.close();
        val contentValues = ContentValues()
        val columns = getAllColumns(DB_TABLE)
        var i = 0
        for (COLUMN in columns) {
            i = 0
            while (i < content.size) {
                if (content[i][0].equals(COLUMN, ignoreCase = true)
                    && (!content[i][1].equals("", ignoreCase = true) || content[i][1] == null)
                ) {
                    contentValues.put(COLUMN, content[i][1])
                    Log.v("column" + (i + 1), COLUMN + " " + content[i][1])
                }
                i++
            }
        }
        try {
            sqLiteDatabase!!.beginTransaction()
            return if (sqLiteDatabase!!.insert(DB_TABLE, null, contentValues) >= 0) {
                sqLiteDatabase!!.setTransactionSuccessful()
                sqLiteDatabase!!.endTransaction()
                true
            } else {
                sqLiteDatabase!!.setTransactionSuccessful()
                sqLiteDatabase!!.endTransaction()
                false
            }
        } catch (e: Exception) {
            Log.v("Error:", "Error: $e")
        }
        return false
    }

    fun makeEmpty(DB_TABLE: String?): Int {
        return sqLiteDatabase!!.delete(DB_TABLE, null, null)
    }

    fun makeEmpty(DB_TABLE: String?, cons: String?): Int {
        return sqLiteDatabase!!.delete(DB_TABLE, cons, null)
    }

    fun getAllColumns(DB_TABLE: String?): List<String> {

        // List<String> columns= mySQLiteAdapter.getAllColumns("questions");
        // for(String column: columns){ Log.v("",column); }
        val columns: Array<String>
        val list: MutableList<String> = ArrayList()
        val cursor = sqLiteDatabase!!.query(
            DB_TABLE, null, null, null, null,
            null, null
        )
        columns = cursor!!.columnNames
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        for (i in columns.indices) {
            list.add(columns[i])
        }
        return list
    }

    fun queueAll(
        DB_TABLE: String?, Columns: Array<String>, order: String?,
        condition: String?
    ): Cursor {
        return sqLiteDatabase!!.query(
            DB_TABLE, Columns, condition,
            null, null, null, order
        )
    }

    fun deleteItem(DB_TABLE: String?, condition: String?): Int {
        return sqLiteDatabase!!.delete(DB_TABLE, condition, null)
    }// The 0 is the column index, we only

    // have 1
    // column, so the index is 0
// String query =
    // "SELECT COLUMN_NAME from DB_TABLE order by COLUMN_NAME DESC limit 1";
    // Cursor c = sqLiteDatabase.rawQuery(query,null);
    // select last_insert_rowid();
    val latestRowId: Int
        get() {
            var latestId = 0
            // String query =
            // "SELECT COLUMN_NAME from DB_TABLE order by COLUMN_NAME DESC limit 1";
            // Cursor c = sqLiteDatabase.rawQuery(query,null);
            val cursor = sqLiteDatabase!!.rawQuery(
                "select last_insert_rowid()",
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                latestId = cursor.getInt(0) // The 0 is the column index, we only
                // have 1
                // column, so the index is 0
                println("LatestId $latestId")
                cursor.close()
            }
            return latestId
        }

    private class SQLiteHelper(
        context: Context?, name: String?,
        factory: CursorFactory?, version: Int
    ) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase) {
            // TODO Auto-generated method stub
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // TODO Auto-generated method stub
        }
    }

    /*
	 * public ArrayList<CartModel> getDataFromDb(String product_id) { // String
	 * // sql="SELECT * FROM NotificationList ORDER BY notification DESC";
	 * Log.d("TAG", "getDataFromDb>>>>>" + product_id);
	 * 
	 * String sql = "SELECT * FROM ShoppingCart where pid='" + product_id + "'";
	 * SQLiteHelper sqLiteHelper = new SQLiteHelper(context, DB_NAME, null,
	 * DB_VERSION);
	 * 
	 * SQLiteDatabase db = sqLiteHelper.getReadableDatabase(); Cursor c =
	 * db.rawQuery(sql, null); if(c.getCount()>0){ Log.d("TAG",
	 * "getDataFromDb>>>>>1" + c.getString(c.getColumnIndex("productname"))); }
	 * 
	 * ArrayList<CartModel> array = new ArrayList<CartModel>(); if
	 * (c.moveToNext()) { CartModel cartModel = new CartModel();
	 * cartModel.setProdName(c.getString(c.getColumnIndex("productname")));
	 * cartModel.setProdSize(c.getString(c.getColumnIndex("productsize")));
	 * cartModel.setProdSizeId(c.getString(c.getColumnIndex("sizeid")));
	 * cartModel .setProdColor(c.getString(c.getColumnIndex("productcolor")));
	 * System.out.println("c.getString---" +
	 * c.getString(c.getColumnIndex("productname"))); array.add(cartModel);
	 * 
	 * } return array;
	 * 
	 * }
	 */
    // function to delete existing product
    fun deleteUser(
        productname: String, quantity: String, color: String,
        categoryId: String?
    ) {
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        println(
            "delete called---" + productname + "--" + quantity
                    + "--" + color
        )
        val db = sqLiteHelper.writableDatabase
        try {
            // db.delete("ShoppingCart", "productname = ? AND productsize=?",
            // new String[] { productname, });
            db.execSQL(
                "Delete From ShoppingCart where productname='"
                        + productname + "' AND productsize='" + quantity
                        + "' AND productcolor='" + color + "' AND productcolor='"
                        + color + "'"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    // get countfrpm db
    fun getCount(
        productname: String, quantity: String, color: String,
        enteredValue: String
    ): Int {
        val countQuery = ("SELECT  * FROM " + "ShoppingCart"
                + " where productname='" + productname + "' AND productsize='"
                + quantity + "' AND productcolor='" + color
                + "' AND productqty=" + enteredValue)
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        val db = sqLiteHelper.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val cnt = cursor.count
        cursor.close()
        println("Count---$cnt")
        return cnt
    }

    fun getCountDuplicateEntry(
        productname: String, quantity: String,
        color: String
    ): Int {
        val countQuery = ("SELECT  * FROM " + "ShoppingCart"
                + " where productname='" + productname + "' AND productsize='"
                + quantity + "' AND productcolor='" + color + "' ")
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        val db = sqLiteHelper.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val cnt = cursor.count
        cursor.close()
        return cnt
    }

    fun getCountDuplicate(
        productname: String, quantity: String,
        color: String
    ): Int {
        val countQuery = ("SELECT  * FROM " + "ShoppingCart"
                + " where productname='" + productname + "' AND productsize='"
                + quantity + "' AND productcolor='" + color + "' ")
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        val db = sqLiteHelper.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val cnt = cursor.count
        cursor.close()
        return cnt
    }

    fun getQuantity(productname: String, color: String, size: String): String {
        val countQuery = ("SELECT  * FROM " + "ShoppingCart"
                + " where productname='" + productname + "' AND productsize='"
                + size + "' AND productcolor='" + color + "' ")
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        val db = sqLiteHelper.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val cnt = cursor.getString(6)
        println("Count---$cnt")
        cursor.close()
        return cnt
    }

    fun updateData(
        prodId: String, prodColor: String, prodSize: String,
        qty: String
    ) {
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        val db = sqLiteHelper.writableDatabase
        try {

            // db.delete("ShoppingCart", "productname = ? AND productsize=?",
            // new String[] { productname, });
            db.execSQL(
                "Update ShoppingCart SET productqty='" + qty
                        + "' where productname='" + prodId + "' AND productsize='"
                        + prodSize + "' AND productcolor='" + prodColor + "'"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun deletePending() {
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        val db = sqLiteHelper.writableDatabase
        try {
            val status = "pending"
            // db.delete("ShoppingCart", "productname = ? AND productsize=?",
            // new String[] { productname, });
            db.execSQL("Delete From ShoppingCart where status='$status'")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun getQty(
        productname: String, color: String, size: String,
        categoryId: String
    ): String? {
        var details: String? = null
        sqLiteHelper = SQLiteHelper(context, DB_NAME, null, DB_VERSION)
        val countQuery = ("SELECT  * FROM " + "ShoppingCart"
                + " where productname='" + productname + "' AND productsize='"
                + size + "' AND productcolor='" + color + "' AND catid='"
                + categoryId + "'")
        val db = sqLiteHelper!!.readableDatabase
        val c = db.rawQuery(countQuery, null)
        if (c != null && c.moveToFirst()) {

            // details=new VehicleDetails();
            details = c.getString(c.getColumnIndex("productqty"))

            // Log.d("TAG", "getMasterId>>>>>Qty"+details);
        }
        if (c != null && !c.isClosed) {
            c.close()
        }
        return details
    }// details=new VehicleDetails();

    // Log.d("TAG", "getMasterId>>>>>Qty"+details);
    //	System.out.println("Price" + sumValue);
    val cartSum: String?
        get() {
            val countQuery = "SELECT  sum(price) FROM ShoppingCart"
            val sqLiteHelper = SQLiteHelper(
                context, DB_NAME, null,
                DB_VERSION
            )
            val db = sqLiteHelper.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            var sumValue: String? = null

            //	System.out.println("Price" + sumValue);
            sumValue = if (cursor != null && cursor.moveToFirst()) {

                // details=new VehicleDetails();
                cursor.getString(0)

                // Log.d("TAG", "getMasterId>>>>>Qty"+details);
            } else {
                "0"
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            return sumValue
        }

    fun updateQuantity(
        prodId: String, prodColor: String, prodSize: String,
        qty: String
    ) {
        val sqLiteHelper = SQLiteHelper(
            context, DB_NAME, null,
            DB_VERSION
        )
        val db = sqLiteHelper.writableDatabase
        try {

            // db.delete("ShoppingCart", "productname = ? AND productsize=?",
            // new String[] { productname, });
            db.execSQL(
                "Update ShoppingCart SET productqty='" + qty
                        + "' where productname='" + prodId + "' AND productsize='"
                        + prodSize + "' AND productcolor='" + prodColor + "'"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    companion object {
        private const val DB_VERSION = 1
        private val TAG: String? = null
        var MAX_INSERT_RECORDS = 0
        var CURRENT_RECORD = 0
    }
}