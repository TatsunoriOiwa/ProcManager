package qwertzite.purchaseprocedures2.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import qwertzite.purchaseprocedures2.Log;
import qwertzite.purchaseprocedures2.procedure.PurchaseId;

public class FileIO {
	public static final String SAVE_FOLDER = "entries";
	public static final String JSON_SUFFIX = ".json";
	
	public static void saveEntryJson(JsonObject json, PurchaseId id) {
		File f = new File(".", SAVE_FOLDER);
		try {
			if (!f.exists()) { f.mkdirs(); }
			File ff = new File(f, id.toString() + JSON_SUFFIX);
			if (!ff.exists()) { ff.createNewFile(); }
			
			FileOutputStream fos = new FileOutputStream(ff);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			JsonWriter jw = new JsonWriter(bw);
			jw.setIndent("\t");
			
			Streams.write(json, jw);
			jw.close();
		} catch (IOException e) {
			Log.warn("Caught an exception while saving an entry " + id, e);
		}
	}
	
	public static File[] getEntryFiles() {
		File ff = new File(".", SAVE_FOLDER);
		if (!ff.exists()) { ff.mkdirs(); }
		File[] files = ff.listFiles();
		return files;
	}
	
	public static JsonObject getJsonObjFromFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			JsonReader jr = new JsonReader(isr);
			JsonElement value = Streams.parse(jr);
			jr.close();
			return value.getAsJsonObject();
		} catch (IOException e) {
			Log.warn("Caught an exception while loading entries.", e);
		}
		return null;
	}
}
