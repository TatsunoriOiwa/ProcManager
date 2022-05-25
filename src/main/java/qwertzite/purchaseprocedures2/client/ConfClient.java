package qwertzite.purchaseprocedures2.client;

import qwertzite.purchaseprocedures2.util.config.AbstractConfig;
import qwertzite.purchaseprocedures2.util.config.ConfigEntry;

public class ConfClient extends AbstractConfig {

	public static final String FILE_LOC = "client.cfg";
	public static ConfClient INSTANCE;
	
	public static int windowWidth = 820;
	public static int windowHeight = 720;
	public static int sashWeightLeft = 3;
	public static int sashWeightRight = 4;
	
	public static ConfClient getInstance() {
		if (INSTANCE == null) INSTANCE = new ConfClient();
		return INSTANCE;
	}
	
	@Override
	protected String getFileName() {
		return FILE_LOC;
	}

	@Override
	protected void loadEntries() {
		this.entries = new ConfigEntry[] {
				new ConfigEntry("window_width", 820, s -> setWindowWidth(Integer.parseInt(s)), () -> getWindowWidth()),
				new ConfigEntry("window_height", 720, s -> setWindowHeight(Integer.parseInt(s)), () -> getWindowHeight()),
				new ConfigEntry("sash_left", 3, s -> setSashWeightLeft(Integer.parseInt(s)), () -> getSashWeightLeft()),
				new ConfigEntry("sash_right", 4, s -> setSashWeightRight(Integer.parseInt(s)), () -> getSashWeightRight()),
				
		};
		// TODO Auto-generated method stub
	}

	public static int getWindowWidth() { return windowWidth; }
	public static void setWindowWidth(int width) { windowWidth = width; }
	public static int getWindowHeight() { return windowHeight; }
	public static void setWindowHeight(int height) { windowHeight = height; }
	public static int getSashWeightLeft() { return sashWeightLeft; }
	public static void setSashWeightLeft(int sashWeightLeft) { ConfClient.sashWeightLeft = sashWeightLeft; }
	public static int getSashWeightRight() { return sashWeightRight; }
	public static void setSashWeightRight(int sashWeightRight) { ConfClient.sashWeightRight = sashWeightRight; }
}
