/* 
 * Copyright (C) 2009 Roman Masek
 * 
 * This file is part of OpenSudoku.
 * 
 * OpenSudoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSudoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OpenSudoku.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.app2go.sudokupro.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class helps open, create, and upgrade the database file.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "DatabaseHelper";
	
	public static final int DATABASE_VERSION = 8;
	
    DatabaseHelper(Context context) {
        super(context, SudokuDatabase.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL("CREATE TABLE " + SudokuDatabase.SUDOKU_TABLE_NAME + " ("
                + SudokuColumns._ID + " INTEGER PRIMARY KEY,"
                + SudokuColumns.FOLDER_ID + " INTEGER,"
                + SudokuColumns.CREATED + " INTEGER,"
                + SudokuColumns.STATE + " INTEGER,"
                + SudokuColumns.TIME + " INTEGER,"
                + SudokuColumns.LAST_PLAYED + " INTEGER,"
                + SudokuColumns.DATA + " Text,"
                + SudokuColumns.PUZZLE_NOTE + " Text"
                + ");");
        
        db.execSQL("CREATE TABLE " + SudokuDatabase.FOLDER_TABLE_NAME + " ("
                + FolderColumns._ID + " INTEGER PRIMARY KEY,"
                + SudokuColumns.CREATED + " INTEGER,"
                + FolderColumns.NAME + " TEXT"
                + ");");
        createIndexes(db);*/
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ".");
        
        createIndexes(db);
    }
    
    private void createIndexes(SQLiteDatabase db) {
    	db.execSQL("create index "+SudokuDatabase.SUDOKU_TABLE_NAME+
     		   "_idx1 on "+
     		   SudokuDatabase.SUDOKU_TABLE_NAME+" ("+SudokuColumns.FOLDER_ID+");");    	
    }
}