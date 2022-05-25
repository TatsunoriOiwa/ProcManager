package qwertzite.purchaseprocedures2;

import qwertzite.purchaseprocedures2.client.PurchaseProcedure2;

/**
 * 
 * Launch the application.
 * @author ellip
 * @date 2020/10/25
 */
public class Main {

	public static void main(String[] args) {
		try {
			Log.info("Java name:{}, version:{}", System.getProperty("java.vm.name"), System.getProperty("java.version"));
			Log.info("OS name:{}, arch:{}, version:{}", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
			Log.info("JAVA_HOME:{}", System.getProperty("java.home"));
			new PurchaseProcedure2().run();
		} catch (Exception e) {
			Log.error("", e);
		}
	}
}
