package qwertzite.purchaseprocedures2.util.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import qwertzite.purchaseprocedures2.Log;

public abstract class AbstractConfig {

	public static final String FILE_DIR = "conf";
	public static final String FILE_SEP = System.getProperty("file.separator");
	public static final String ENCODING = "UTF-8";
	
	public static final char SEPARATOR = ':';
	
	protected ConfigEntry[] entries = null;
	
	protected void clearEntries() {
		this.entries = null;
	}
	
	/**
	 *  Called before reading configurations from, or writing to the file.<br>
	 *  Assign {@link AbstractConfig#entries}.
	 */
	protected abstract void loadEntries();
	
	/**
	 * load file data
	 */
	public void initialise() {
		loadEntries();		
		HashMap<String, String> map = new HashMap<>(entries.length);
		
		File fd = new File(".", FILE_DIR);
		File ff = new File(fd, this.getFileName());
		if (ff.exists()) {
			Log.info("loading config from file '{}'", this.getFileName());
			this.readFromFile(ff, map);
		} else {
			Log.info("cound not find config file '{}'. applying default values", this.getFileName());
		}
		
		setValue(map);
		clearEntries();
	}
	
	protected abstract String getFileName();
	
	private void readFromFile(File f, Map<String, String> map) {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), ENCODING));
			
			br.lines().parallel().forEach(s -> {
				String[] pair = split(s);
				if (pair != null) map.put(pair[0], pair[1]);
			});
			
			br.close();
		} catch (IOException e) {
			Log.error("failed to load configuration file. applying default values.", e);
		}
	}
	
	/**
	 * splits given string at the first colon(:), and returns the new two Strings
	 * @param targ
	 * @return null if it does not match the format. ( <key>:<value> )
	 */
	private String[] split(String targ) {
    	
        int i = targ.indexOf(SEPARATOR);

        if (i >= 0) {
            String[] res = new String[] {"", targ};
            res[1] = targ.substring(i + 1, targ.length());
            if (i > 1) {
                res[0] = targ.substring(0, i);
            }
            return res;
        }

        return null;
    }
    
	private void setValue(Map<String, String> map) {
    	for (ConfigEntry entry : this.entries) {
    		entry.setValue(map);
    	}
    }
    
	/**
	 * Must be called before shutdown, on appropriate timing for each configuration groups.
	 */
	public void saveAndClose() {
		loadEntries();
		writeToFile();
		clearEntries();
	}
	
	private void writeToFile() {
		File fd = new File(".", FILE_DIR);
		if (!fd.exists()) fd.mkdirs();
		File ff = new File(fd, this.getFileName());
		try {
			ff.createNewFile();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ff), ENCODING)))) {
			for (ConfigEntry entry : this.entries) {
				pw.println(entry.getString());
			}
			pw.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
