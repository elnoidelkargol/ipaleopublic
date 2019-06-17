package com.nappstic.alimentospaleo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import android.database.SQLException;
//import android.util.Log;

public class MyDBAdapter extends SQLiteOpenHelper {

	private SQLiteDatabase mDB;
	private final Context mCtx;

	private static String DB_PATH = "/data/data/com.nappstic.alimentospaleo/databases/";
	private static String DB_NAME = "paleo_alimentos.db";
	private static String DB_TABLE = "alimentos";
	private static final int DB_VERSION = 1;
	private static final String KEY_ROWID = "_id";
	public static final String KEY_NOMBRE_EN = "nombre_en";
	private static final String KEY_NOMBRE_ES = "nombre_es";
	private static final String KEY_NOMBRE_CA = "nombre_ca";
	private static final String KEY_DESC_EN = "descripcio_en";
	private static final String KEY_DESC_ES = "descripcio_es";
	private static final String KEY_DESC_CA = "descripcio_ca";
	private static final String KEY_IMG = "img_firebase";
	private static final String KEY_CAT_CA = "categoria_ca";
	public static final String KEY_CAT_EN = "categoria_en";
	private static final String KEY_CAT_ES = "categoria_es";

	public MyDBAdapter(Context ctx) {
		super(ctx,DB_NAME,null,DB_VERSION);
		this.mCtx = ctx;
	}

	public void close() {
		if(mDB!=null)
			mDB.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = mCtx.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];

		int length;
		while((length = myInput.read(buffer))>0)
			myOutput.write(buffer, 0, length);

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();

		/*switch (Locale.getDefault().getLanguage().toUpperCase()){
			case "ES":
				Lang = "_ES";
				break;
			case "EN":
				Lang = "_EN";
				break;
			case "CA":
				Lang = "_CA";
				break;
			default:
				Lang = "_EN";
				break;
		}
		KEY_CAT = KEY_CAT.concat(Lang);
		KEY_NOMBRE_EN = KEY_NOMBRE_EN.concat(Lang);
		KEY_DESC = KEY_DESC.concat(Lang);*/

		if(!dbExist) {
			this.getReadableDatabase();


			try {
				copyDataBase();
                Log.d("COPYDATABASE", "DATABASE COPIED: ");
            } catch (IOException e) {
				throw new Error("Error copying DB");
			}
		}
	}


	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch(SQLiteException e) {
			//database does't exist yet.
		}
		if(checkDB != null){
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}


	public void openDataBase() throws SQLiteException {
		//Open the database
		String myPath = DB_PATH + DB_NAME;
		mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	public String getImgFood(Long id){
		mDB = this.getReadableDatabase();
		Cursor mCursor = mDB.query(DB_TABLE, new String[]{"img"},"_id = ?",new String []{id.toString()},null,null,"NOMBRE_EN");


		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor.getString(mCursor.getColumnIndexOrThrow("img"));

	}
	public Cursor fetchAllCodes() {
		Log.d("ALLCODES", "fetchAllCodes: ENTER");
		Cursor mCursor = mDB.query(DB_TABLE, new String[]{KEY_ROWID, KEY_NOMBRE_EN,KEY_NOMBRE_CA,KEY_NOMBRE_ES,KEY_CAT_CA,KEY_CAT_EN,KEY_CAT_ES,KEY_IMG,KEY_DESC_CA,KEY_DESC_EN,KEY_DESC_ES},null,null,null,null,"nombre_en");


		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor fetchListFoodByCategory (String category_id){

		Cursor mCursor = mDB.query(DB_TABLE,new String[]{KEY_ROWID, KEY_NOMBRE_EN,KEY_NOMBRE_CA,KEY_NOMBRE_ES,KEY_CAT_CA,KEY_CAT_EN,KEY_CAT_ES,KEY_IMG,KEY_DESC_CA,KEY_DESC_EN,KEY_DESC_ES},"categoria_id = ?",new String[]{category_id},null,null,"nombre_en");
		if (mCursor !=null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor fetchOneAlimento (String id){

		Cursor mCursor = mDB.query(DB_TABLE,new String[]{KEY_ROWID, KEY_NOMBRE_EN,KEY_DESC_EN,KEY_IMG,KEY_DESC_EN},"_id = ?",new String[]{id},null,null,null);

		/*String query = "SELECT "
				+ KEY_NOMBRE_EN + " NOMBRE,"
				+ KEY_ROWID + ","
				+ KEY_IMG + ","
				+ KEY_CAT  + " CATEGORIA, "
				+ KEY_DESC + " DESCRIPCIO"
				+ " FROM " + DB_TABLE
				+ " WHERE _id = '" + id +  "'";

		Log.d("query fetch one", query);
		Cursor mCursor = mDB.rawQuery(query, null);*/

		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public long deleteFavouriteRecipe(Integer id){

			mDB = this.getWritableDatabase();
			long res;
			res = mDB.delete("favouriteRecipes", "id = ?", new String[]{id.toString()});
			return res;

	}
	public long insertToFavouriteRecipe (Recipe recipe){
		long res;

		mDB = this.getWritableDatabase();
		ContentValues initialValues = new ContentValues();

		initialValues.put ("ingredientes_txt_ca",recipe.getIngredientes_txt_ca());
		initialValues.put ("ingredientes_txt_es",recipe.getIngredientes_txt_es());
		initialValues.put ("ingredientes_txt_en",recipe.getIngredientes_txt_en());
		initialValues.put ("categoria_ppal",recipe.getCategoria_ppal());
		initialValues.put ("categoria_secun",recipe.getCategoria_secun());
		initialValues.put ("ingredientes_obj",recipe.getIngredientes_obj());
		initialValues.put ("nombre_ca",recipe.getNombre_ca());
		initialValues.put ("nombre_es",recipe.getNombre_es());
		initialValues.put ("nombre_en",recipe.getNombre_en());
		initialValues.put ("preparacion_ca",recipe.getPreparacion_ca());
		initialValues.put ("preparacion_es",recipe.getPreparacion_es());
		initialValues.put ("preparacion_en",recipe.getPreparacion_en());
		initialValues.put ("personas",recipe.getPersonas());
		initialValues.put ("dificultat",recipe.getDificultat());
		initialValues.put ("tiempo",recipe.getTiempo());
		initialValues.put ("foto",recipe.getFoto());
		initialValues.put ("fuente",recipe.getFuente());
		initialValues.put ("id",recipe.getId());
		initialValues.put ("isFavourite",true);

		res = mDB.insert("favouriteRecipes",null,initialValues);
		Log.d("INSERTFAVRECIPE", "insertToFavouriteRecipe: " + res);
        Cursor mCursor = mDB.query("favouriteRecipes",new String[]{"id"},null,null,null,null,null);
        Log.d("INSERTCOUNT", "insertToFavouriteRecipe: " + mCursor.getCount());
		return res;



	}
    public long insertIntoShoppingList (Long id,String name, String quantity, int value){
        mDB = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        long res;
		String img = this.getImgFood(id);
        int id_alimento= (int)(long)id;
        initialValues.put("name",name);
        initialValues.put("quantity",quantity);
        initialValues.put("value",value);
        initialValues.put("id_alimento",id_alimento);
        initialValues.put("img",img);

        res = mDB.insert("cart",null,initialValues);
        Cursor mCursor = mDB.query("cart",new String[]{"_id","name","quantity","value","id_alimento,img"},null,null,null,null,"Name");

        if(mCursor != null){
            mCursor.moveToFirst();
        }
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(mCursor));

        return res;
        //String sql = "INSERT INTO cart ('name','quantity','value') VALUES ('testname','8','quantitity_value')";
        //mDB.execSQL(sql);
    }
    public long checkItemOnShoopingList (String id,Boolean isChecked){
    	mDB = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	long res;
    	Integer check = 0;
    	if (isChecked) check = 1;
		values.put("isChecked",check);
    	res = mDB.update("cart",values,"_id = '" +id+"'",null);
    	return res;
	}
	public long clearSelectedItems(){
    	mDB = this.getWritableDatabase();
    	long res;
    	res = mDB.delete("cart","isChecked = 1",null);
    	return res;
	}
	public Cursor fetchFavouriteRecipeList (){
	    Cursor mCursor = mDB.query("favouriteRecipes",new String[]{"_id","nombre_en","nombre_es","nombre_ca","tiempo","personas","foto","ingredientes_txt_ca","ingredientes_txt_es","ingredientes_txt_en","categoria_ppal","categoria_secun","ingredientes_obj","preparacion_ca","preparacion_es","preparacion_en","dificultat","fuente","id"},null,null,null,null,null);
        if (mCursor !=null){
            mCursor.moveToFirst();
        }
	    return mCursor;
    }
    public Cursor fetchShoppingList() {

		Cursor mCursor = mDB.query("cart",new String[]{"_id","name","quantity","value","id_alimento,isChecked,img"},null,null,null,null,"Name");

		if(mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getCategories(){
		Cursor mCursor;
		String [] campos = {"*"};

		mCursor = mDB.query("categoria",new String[]{"*"},null,null,null,null,"categoria_id");

		if(mCursor != null){
			mCursor.moveToFirst();
		}

		return mCursor;
	}
	public Cursor getFilterRecipes (){
		Cursor mCursor;
		String [] campos = {"*"};

		mCursor = mDB.query("filtrosIngredientePrincipal",campos,null,null,null,null,null);

		if (mCursor !=null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor clearShoppingList () {
		mDB = this.getWritableDatabase();
		long res = mDB.delete("cart", "isChecked = ?", new String[]{"1"});
		Cursor shoppingList = this.fetchShoppingList();
		return shoppingList;

	}
}
