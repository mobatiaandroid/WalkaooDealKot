package com.mobatia.vkcsalesapp.SQLiteServices

import android.app.Activity
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.mobatia.vkcsalesapp.controller.AppController
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DatabaseHelper(
    private val myContext: Activity, //public static String DB_PATH = "/data/data/com.storefrontbase/databases/";
    private val DB_NAME: String
) : SQLiteOpenHelper(myContext, DB_NAME, null, 1) {
    private var myDataBase: SQLiteDatabase? = null
    var TABLE_NAME = "cart"
    var db: SQLiteDatabase? = null

    /* public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }*/
    @Throws(IOException::class)
    fun createDataBase() {
        val dbExist = checkDataBase()
        if (dbExist) {
        } else {
            Log.v("Need to create", "Need to create")
            this.readableDatabase
            try {
                copyDataBase()
            } catch (e: IOException) {
                throw Error("Error copying database")
            }
        }
    }

    private fun checkDataBase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            val myPath = DB_PATH + DB_NAME
            checkDB = SQLiteDatabase.openDatabase(
                myPath, null,
                SQLiteDatabase.OPEN_READWRITE
            )
            Log.v("DB Exists", "DB Exists")
        } catch (e: SQLiteException) {
            Log.v("Database Not Exist", "Database Not Exist")
        }
        checkDB?.close()
        return if (checkDB != null) true else false
    }

    @Throws(IOException::class)
    private fun copyDataBase() {
        val myInput = myContext.assets.open(DB_NAME)
        val outFileName = DB_PATH + DB_NAME
        val myOutput: OutputStream = FileOutputStream(outFileName)
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }

    @Throws(SQLException::class)
    fun openDataBase() {
        // Open the database
        val myPath = DB_PATH + DB_NAME
        myDataBase = SQLiteDatabase.openDatabase(
            myPath, null,
            SQLiteDatabase.OPEN_READWRITE
        )
    }

    override fun onCreate(db: SQLiteDatabase) {
        //  Log.v("DB", "inside create");
        val S =
            ("CREATE TABLE " + TABLE_CART + "(" + PRODUCT_ID + " INTEGER," + PRODUCT_NAME + " TEXT," + SIZE_ID + " INTEGER,"
                    + PRODUCT_SIZE + " TEXT," + COLOR_ID + " INTEGER," + PRODUCT_COLOR + " TEXT," + PRODUCT_QTY + " TEXT," + GRID_VALUE + " TEXT," + PID +
                    " TEXT," + SAP_ID + " TEXT," + CAT_ID + " TEXT," + STATUS + " TEXT," + PRICE + " TEXT" + ")")
        db.execSQL(S)
        /*  try {
            //String S = "CREATE TABLE " + TABLE_CONTACTS + "(" + MOVIE_NAME + " TEXT," + MOVIE_YEAR + " TEXT," + MOVIE_TYPE + " TEXT" + ")";
            String S = "CREATE TABLE " + TABLE_NAME + "(" + "productid" + " TEXT," + "productname" + " TEXT," + "sizeid" + " TEXT," + "productsize" + " TEXT," + "colorid" + " TEXT," + "productcolor" + " TEXT," + "productqty" + " TEXT," + "gridvalue" + " TEXT," + "pid" + " TEXT," + "sapid" + " TEXT," + "catid" + " TEXT," + "status" + " TEXT," + "price" + " TEXT" + ")";
            db.execSQL(S);
        } catch (Exception e) {
            Log.i("Sqlite Error", e.toString());
        }*/
    }

    @Synchronized
    override fun close() {
        if (myDataBase != null) myDataBase!!.close()
        super.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART)
        onCreate(db)
    }

    @Throws(SQLException::class)
    fun open() {
        close()
        db = this.writableDatabase
    }

    fun closeDB() {
        if (db!!.isOpen) db!!.close()
    }

    companion object {
        var DB_PATH =
            "/data/data/" + AppController.getInstance().getPackageName().toString() + "/databases/"
        private const val DATABASE_NAME = "VKC"
        private const val TABLE_CART = "shoppingcart"
        private const val PRODUCT_ID = "productid"
        private const val PRODUCT_NAME = "productname"
        private const val SIZE_ID = "sizeid"
        private const val PRODUCT_SIZE = "productsize"
        private const val COLOR_ID = "colorid"
        private const val PRODUCT_COLOR = "productcolor"
        private const val PRODUCT_QTY = "productqty"
        private const val GRID_VALUE = "gridvalue"
        private const val PID = "pid"
        private const val SAP_ID = "sapid"
        private const val CAT_ID = "catid"
        private const val STATUS = "status"
        private const val PRICE = "price"
    }
}