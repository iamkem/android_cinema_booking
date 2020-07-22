package com.example.vayo.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.lang.reflect.Field;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Cinema.db"; //variabila pentru stocarea numelui bazei de date

    //=============USERS_TABLE==========================

    public static final String TABLE_NAME = "users_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "EMAIL";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "ADMIN";

    //=============MOVIES_TABLE==========================

    public static final String MOVIES_TABLE = "movies_table";
    public static final String MCOL_2 = "NAME";
    public static final String MCOL_3 = "CATEGORY";
    public static final String MCOL_4 = "DESCRIPTION";
    public static final String MCOL_5 = "PRICE";
    public static final String MCOL_6 = "LENGTH";
    public static final String MCOL_7 = "IMAGE";



    //=============CINEMAS_TABLE==========================
    public static final String CINEMAS_TABLE = "cinemas_table";
    public static final String CCOL_2 = "NAME";
    public static final String CCOL_3 = "MANAGER";
    public static final String CCOL_4 = "ADDRESS";
    public static final String CCOL_5 = "CAPACITY";

    //=============SHOWING_MOVIES_TABLE====================

    public static final String SHOWINGS_MOVIES_TABLE = "showing_movies_table";
    public static final String SMCOL_2 = "MOVIE_ID";
    public static final String SMCOL_3 = "CINEMA_ID";
    public static final String SMCOL_4 = "FROM_DATE";
    public static final String SMCOL_5 = "TO_DATE";
    public static final String SMCOL_6 = "START_TIME";


    //=============Bookings====================
    public static final String BOOKINGS = "bookings";
    public static final String BCOL_2 = "USER_ID";
    public static final String BCOL_3 = "SHOWING_ID";
    public static final String BCOL_4 = "BOOKING_FOR_DATE";
    public static final String BCOL_5 = "BOOKING_MADE_DATE";
    public static final String BCOL_6 = "BOOKING_MADE_TIME";
    public static final String BCOL_7 = "SEAT_COUNT";
    public static final String BCOL_8 = "CONFIRMED";


    //=============Favourites====================
    public static final String FAVOURITE = "favourite";
    public static final String FCOL_2 = "USER_ID";
    public static final String FCOL_3 = "MOVIE_ID";


    //=============Movies_Reviews_Table====================
    public static final String MOVIE_REVIEWS = "movies_reviews_table";
    public static final String MRCOL_2 = "MOVIE_ID";
    public static final String MROL_3 = "REVIEW";


    public DatabaseHelper(Context context)
    {
        //creare baza de date cu numele specificat anterior
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creare structura pentru fiecare tabel al bazei de date precum si impunerea constrangerilor
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,EMAIL TEXT NOT NULL UNIQUE,PASSWORD TEXT NOT NULL, ADMIN TEXT NOT NULL CHECK(ADMIN = 'yes' OR ADMIN = 'no'))");
        db.execSQL("create table " + MOVIES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL UNIQUE,CATEGORY TEXT NOT NULL,DESCRIPTION TEXT,PRICE INTEGER NOT NULL, LENGTH INTEGER NOT NULL, IMAGE BLOB)");
        db.execSQL("create table " + CINEMAS_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL , MANAGER TEXT NOT NULL, ADDRESS TEXT NOT NULL UNIQUE, CAPACITY INTEGER NOT NULL)");
        db.execSQL("create table " + SHOWINGS_MOVIES_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, MOVIE_ID INTEGER NOT NULL,CINEMA_ID INTEGER NOT NULL,FROM_DATE DATE NOT NULL, TO_DATE DATE NOT NULL, START_TIME TIME NOT NULL,UNIQUE(MOVIE_ID,CINEMA_ID) ,FOREIGN KEY(MOVIE_ID) REFERENCES movies_table(ID),FOREIGN KEY(CINEMA_ID) REFERENCES cinemas_table(ID))");
        db.execSQL("create table " + BOOKINGS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,USER_ID INTEGER NOT NULL, SHOWING_ID INTEGER NOT NULL, BOOKING_FOR_DATE DATE NOT NULL, BOOKING_MADE_DATE DATE NOT NULL, BOOKING_MADE_TIME TIME NOT NULL,SEAT_COUNT INTEGER NOT NULL,CONFIRMED TEXT NOT NULL CHECK(CONFIRMED = 'yes' OR CONFIRMED = 'no'), FOREIGN KEY(USER_ID) REFERENCES users_table(ID),FOREIGN KEY(SHOWING_ID) REFERENCES showing_movies_table(ID))");
        db.execSQL("create table " + FAVOURITE + "(MOVIE_ID INTEGER NOT NULL, USER_ID INTEGER NOT NULL, PRIMARY KEY(MOVIE_ID,USER_ID) ,FOREIGN KEY(USER_ID) REFERENCES users_table(ID),FOREIGN KEY(MOVIE_ID) REFERENCES movies_table(ID))");
        db.execSQL("create table " + MOVIE_REVIEWS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, MOVIE_ID INTEGER NOT NULL, REVIEW TEXT NOT NULL, MARK INTEGER NOT NULL,FOREIGN KEY(MOVIE_ID) REFERENCES movies_table(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //stergere tabel daca exista la o noua creare de structura
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CINEMAS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SHOWINGS_MOVIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_REVIEWS);

        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    //=============Users================

    //introducere users in BD
    public void insertData(String email, String password,String admin) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO users_table VALUES (NULL,?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, email);
        statement.bindString(2, password);
        statement.bindString(3, admin);


        statement.executeInsert();
    }



    //preluare informatii tabel users
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    //actualizare cont
    public void updateData(String id, String email, String password, String admin) {
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "UPDATE users_table SET EMAIL = ? ,PASSWORD = ? , ADMIN = ? WHERE ID = ? ";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, email);
        statement.bindString(2, password);
        statement.bindString(3, admin);
        statement.bindString(4,id);

        statement.executeUpdateDelete();

    }

    //stergere cont
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }


    //===================================


    //=============Movies================

    //introducere filme in BD
    public void insertMovie(String name, String category, String description, String price, String length, byte [] image) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO movies_table VALUES (NULL,?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, name);
        statement.bindString(2, category);
        statement.bindString(3, description);
        statement.bindString(4, price);
        statement.bindString(5, length);
        statement.bindBlob(6, image);


        statement.executeInsert();
    }

    //preluare date cu toate filmele
    public Cursor getAllMovies() {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + MOVIES_TABLE, null);
        return res;
    }

    //editare film
    public void updateMovie(String id, String name, String category, String description, String price, String length) {
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "UPDATE movies_table SET NAME = ? ,CATEGORY = ? , DESCRIPTION = ? ,PRICE = ? , LENGTH = ? WHERE ID = ? ";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, name);
        statement.bindString(2, category);
        statement.bindString(3, description);
        statement.bindString(4, price);
        statement.bindString(5, length);
        statement.bindString(6,id);


        statement.executeUpdateDelete();


    }

    //stergere film
    public Integer deleteMovie(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MOVIES_TABLE, "ID = ?", new String[]{id});
    }

    //ordonare filme
    public Cursor getOrderedMovies(String order) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "";
        if (order.equals("News"))
            sql = "select * from movies_table ORDER BY ID ASC";
        if (order.equals("AscendingName"))
            sql = "select * from movies_table ORDER BY NAME ASC";
        if (order.equals("DescendingName"))
            sql = "select * from movies_table ORDER BY NAME DESC";
        if (order.equals("AscendingPrice"))
            sql = "select * from movies_table ORDER BY PRICE ASC";
        if (order.equals("DescendingPrice"))
            sql = "select * from movies_table ORDER BY PRICE DESC";

        Cursor res = db.rawQuery(sql, null);
        return res;
    }


    //===================================

    //=============Cinemas================
    //introducere cinemauri in BD
    public void insertCinema(String name, String manager, String address, String capacity) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO cinemas_table VALUES (NULL,?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, name);
        statement.bindString(2, manager);
        statement.bindString(3, address);
        statement.bindString(4, capacity);


        statement.executeInsert();
    }

    //preluare informatii despre toate cinemaurile
    public Cursor getAllCinemas() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + CINEMAS_TABLE, null);
        return res;
    }

    //stergere cinema
    public Integer deleteCinema(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CINEMAS_TABLE, "ID = ?", new String[]{id});
    }

    //update cinema
    public void updateCinema(String id, String name, String manager, String address, String capacity) {
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "UPDATE cinemas_table SET NAME = ? ,MANAGER = ? , ADDRESS = ? ,CAPACITY = ? WHERE ID = ? ";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, name);
        statement.bindString(2, manager);
        statement.bindString(3, address);
        statement.bindString(4, capacity);
        statement.bindString(5, id);


        statement.executeUpdateDelete();
    }


    //====================================

    //=============Showings_Movies========
    //introducere shows in BD
    public void insertShowings(String movie_id, String cinema_id, String from_date, String to_date, String time_start) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO showing_movies_table VALUES (NULL,?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, movie_id);
        statement.bindString(2, cinema_id);
        statement.bindString(3, from_date);
        statement.bindString(4, to_date);
        statement.bindString(5, time_start);


        statement.executeInsert();
    }

    //preluare toate informatii despre show
    public Cursor getAllShowings() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + SHOWINGS_MOVIES_TABLE, null);
        return res;
    }

    //stergere show
    public Integer deleteShowings(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SHOWINGS_MOVIES_TABLE, "ID = ?", new String[]{id});
    }

    //update show
    public void updateShowings(String id, String movie_id, String cinema_id, String from_date, String to_date, String time_start) {
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "UPDATE showing_movies_table SET MOVIE_ID = ? ,CINEMA_ID = ? , FROM_DATE = ? ,TO_DATE = ?, START_TIME = ? WHERE ID = ? ";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, movie_id);
        statement.bindString(2, cinema_id);
        statement.bindString(3, from_date);
        statement.bindString(4, to_date);
        statement.bindString(5, time_start);
        statement.bindString(6, id);


        statement.executeUpdateDelete();
    }

    //====================================

    //=============Bookings========
    //introducere bookings in BD
    public void insertBookings(String user_id, String showing_id, String booking_for_date, String booking_made_date, String booking_made_time, String booking_seat_count, String booking_confirmed_status) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO bookings VALUES (NULL,?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, user_id);
        statement.bindString(2, showing_id);
        statement.bindString(3, booking_for_date);
        statement.bindString(4, booking_made_date);
        statement.bindString(5, booking_made_time);
        statement.bindString(6, booking_seat_count);
        statement.bindString(7, booking_confirmed_status);



        statement.executeInsert();
    }

    //preluare toate informatiile despre rezervari
    public Cursor getAllBookings() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + BOOKINGS, null);
        return res;
    }


    //update rezervari
    public void updateBookings(String id, String booking_confirmed_status) {
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "UPDATE bookings SET CONFIRMED = ?  WHERE ID = ? ";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, booking_confirmed_status);
        statement.bindString(2, id);


        statement.executeUpdateDelete();
    }






    //====================================

    //=============Movie_Review_Table========

    public void insertMovieReview(String movie_id,String review, String mark) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO favourite VALUES (NULL,?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, movie_id);
        statement.bindString(2, review);
        statement.bindString(2, mark);


        statement.executeInsert();
    }

    //preluare toate filmele favorite
    public Cursor getAllMovieeReviews() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + MOVIE_REVIEWS, null);
        return res;
    }


    //=============Favourites========
    //introducere favorite in BD
    public void insertFavourite(String user_id,String movie_id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO favourite VALUES (?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        //inlocuire "?" cu String-ul specificat
        statement.bindString(1, user_id);
        statement.bindString(2, movie_id);


        statement.executeInsert();
    }

    //preluare toate filmele favorite
    public Cursor getAllFavourite() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + FAVOURITE, null);
        return res;
    }

    //stergere favorite
    public Integer deleteFavourite(String movie_id, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FAVOURITE, "MOVIE_ID = ? AND USER_ID = ? ", new String[]{movie_id, user_id} );
    }

    //=============JOIN========

    //filtrare filme care apartin unui show
    public Cursor getMovieswithShowings()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT m.ID , m.NAME\n" +
                "FROM movies_table m\n" +
                "where m.ID in\n" +
                "(select DISTINCT m2.ID from movies_table m2, showing_movies_table s \n" +
                "where m2.id = s.MOVIE_ID)";
        Cursor res = db.rawQuery(sql, null);
        return res;

    }


    //sortare doar userii care au rezervari
    public Cursor getAllUserWithBookings(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT u.ID, u.EMAIL\n" +
                "FROM users_table u\n" +
                "where u.ID \n" +
                "in (select DISTINCT u2.ID from users_table u2, bookings b \n" +
                "where u2.ID = b.USER_ID)";
        Cursor res = db.rawQuery(sql, null);
        return res;

    }

    public Cursor getAllMoviesifromShowingsMostPopular()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select DISTINCT s.MOVIE_ID\n" +
                "from showing_movies_table s\n" +
                "where s.id in (select b.SHOWING_ID from showing_movies_table s2, bookings b\n" +
                "where s2.ID = b.SHOWING_ID\n" +
                "group by s2.ID\n" +
                "having sum(SEAT_COUNT) >= (select avg(SEAT_COUNT) from bookings)\n" +
                ")";
        Cursor res = db.rawQuery(sql, null);


        return res;
    }

    public Cursor getAllShowwithoutBookings()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select s.ID, s.MOVIE_ID, S.CINEMA_ID\n" +
                "from showing_movies_table s\n" +
                "where s.id not in (select distinct s2.id\n" +
                " from showing_movies_table s2, bookings b\n" +
                " where s2.id = b.SHOWING_ID) ";
        Cursor res = db.rawQuery(sql, null);


        return res;
    }



    //toate showurile in cinema ul selectat
    public Cursor getAllShowingsInCinema(String cinema_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT S.MOVIE_ID FROM showing_movies_table S INNER JOIN cinemas_table C ON S.CINEMA_ID = C.ID\n" +
                "WHERE C.ID = " + cinema_id, null);


        return res;
    }

    //toate filmele din categoria specificata
    public Cursor getAllMoviesbyCategory(String category)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT ID, NAME FROM movies_table WHERE CATEGORY = '" + category + "'";
        Cursor res = db.rawQuery(sql, null);

        return res;

    }

    //toate filmele sub pretul specificat
    public Cursor getAllMoviesunderPrice(String price)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT ID FROM movies_table WHERE PRICE < " + price;
        Cursor res = db.rawQuery(sql, null);

        return res;

    }

    //toate filmele sortate dupa cinema si pret
    public Cursor getAllMoviesbyCinemaandPrice(String cinema_id, String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT DISTINCT MOVIE_ID FROM showing_movies_table S \n" +
                "INNER JOIN cinemas_table C \n" +
                "ON S.CINEMA_ID = C.ID\n" +
                "INNER JOIN movies_table M\n" +
                "ON S.MOVIE_ID = M.ID\n" +
                "WHERE C.ID = " + cinema_id + " AND M.PRICE <= " + price;
        Cursor res = db.rawQuery(sql, null);

        return res;

    }


    //toate filmele sortate dupa categorie si pret
    public Cursor getAllMoviesbyCategoryPrice(String category, String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM movies_table WHERE CATEGORY = '" + category + "'" + "AND PRICE < " + price;
        Cursor res = db.rawQuery(sql, null);

        return res;

    }

    public Cursor getMovieAverage(String movie_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT AVG(MARK) FROM movies_reviews_table MR where MR.MOVIE_ID = " + movie_id;
        Cursor res = db.rawQuery(sql, null);

        return res;

    }

    //toate filmele sortate dupa categorie si cinema
    public Cursor getAllMoviesbyCategoryandCinema(String category, String cinema_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT DISTINCT MOVIE_ID\n" +
                "FROM showing_movies_table S \n" +
                "INNER JOIN cinemas_table C \n" +
                "ON S.CINEMA_ID = C.ID \n" +
                "INNER JOIN movies_table M\n" +
                "ON S.MOVIE_ID = M.ID \n" +
                "WHERE M.CATEGORY = '" + category + "' AND C.ID = " + cinema_id;


        Cursor res = db.rawQuery(sql, null);

        return res;

    }


    //toate filmele dupa cinema, categorie si pret
    public Cursor getAllFilteredMovies(String cinema_id, String category, String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT DISTINCT MOVIE_ID\n" +
                "FROM showing_movies_table S \n" +
                "INNER JOIN cinemas_table C \n" +
                "ON S.CINEMA_ID = C.ID \n" +
                "INNER JOIN movies_table M\n" +
                "ON S.MOVIE_ID = M.ID \n" +
                "WHERE M.CATEGORY = '" + category + "' AND C.ID = " + cinema_id + " AND M.PRICE < " + price;


        Cursor res = db.rawQuery(sql, null);

        return res;

    }






}






