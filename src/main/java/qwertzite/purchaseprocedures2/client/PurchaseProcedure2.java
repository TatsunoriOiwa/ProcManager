package qwertzite.purchaseprocedures2.client;

import org.apache.logging.log4j.Level;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import qwertzite.purchaseprocedures2.Log;
import qwertzite.purchaseprocedures2.client.screen.DialogNewEntry;
import qwertzite.purchaseprocedures2.client.screen.MainScreen;
import qwertzite.purchaseprocedures2.implementation.DefaultProcedure;
import qwertzite.purchaseprocedures2.implementation.GasN2Procedure;
import qwertzite.purchaseprocedures2.implementation.HighValueProcedure;
import qwertzite.purchaseprocedures2.implementation.UTCoopProcedure;
import qwertzite.purchaseprocedures2.procedure.EntryManager;
import qwertzite.purchaseprocedures2.procedure.Procedure;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class PurchaseProcedure2 {

	public static final String GAMENAME = "PurchaseProcedure2";
	public static final String GAME_ID = "purchaseprocedure2";
	public static final String VERSION = "0.0.0";
	public static final String VERSION_SERIAL = "0000";
	
	public static PurchaseProcedure2 INSTANCE = null;

	private Display display;
	private Shell shell;
	private StackLayout shellLayout;
//	private Runnable renderDispatcher;
	
	private MainScreen mainScreen;
	
	private boolean isRunning;
	
	public EntryManager entries;
	
	public PurchaseEntry selected;
	
	public PurchaseProcedure2() {
		if (INSTANCE != null) throw new IllegalStateException("ScheduleManager is already running!");
		INSTANCE = this;
	}
	
	public void init() {
		// configs may come here
//		this.entries = new PurchaseManager();
//		this.entries.load();
		ConfClient.getInstance().initialise();
		
		Procedure.registerProcedure(new DefaultProcedure());
		Procedure.registerProcedure(new HighValueProcedure());
		Procedure.registerProcedure(new GasN2Procedure());
		Procedure.registerProcedure(new UTCoopProcedure.UTCoopNormal());
		Procedure.registerProcedure(new UTCoopProcedure.UTCoopHighValue());
		
		this.entries = new EntryManager();
		this.entries.loadEntries();
		
		Log.setLogLevel(Level.DEBUG);
		this.display = Display.getDefault();
		this.shell = new Shell(this.display);
		this.shellLayout = new StackLayout();
		this.shell.setLayout(this.shellLayout);
		this.shell.setSize(ConfClient.windowWidth, ConfClient.windowHeight);
//		this.shell.setMaximized(LaunchConf.isMaximised);
		this.shell.setText(GAMENAME + " " + VERSION);
		this.shell.addDisposeListener(this::onMainShellDisposed);
		this.shell.setModified(true);
		MainScreen mainScreen = new MainScreen(shell);
		this.mainScreen = mainScreen;

		for (Procedure p : Procedure.getProcedureList()) {
			this.mainScreen.registerProcedure(p);
		}
		
		this.shellLayout.topControl = mainScreen;
		shell.layout();
		shell.open();
		
		Log.info("initiated");
	}
	
	public void run() {
		this.init();
		this.setRunning(true);
		
		while (!shell.isDisposed()) {
			if (!this.isRunning) {
				shell.dispose();
				break;
			}
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		this.shutDown();
	}
	
	private void onMainShellDisposed(DisposeEvent event) {
		ConfClient.windowHeight = shell.getBounds().height;
		ConfClient.windowWidth = shell.getBounds().width;
		int[] form = this.mainScreen.sashForm.getWeights();
		ConfClient.sashWeightLeft = form[0];
		ConfClient.sashWeightRight = form[1];
	}
	
	public void shutDown() {
		Log.info("shutting down...");
		this.isRunning = false;
		
		// client
//		display.timerExec(-1, this.renderDispatcher); // prevent runnable from being called
		display.timerExec(-1, () -> {}); // prevent runnable from being called
		display.dispose();
		
		// configurations
		ConfClient.getInstance().saveAndClose();
		
		this.entries.saveEntries();
	}
	
	public void setRunning(boolean flag) { this.isRunning = flag; }
	public boolean isRunning() { return this.isRunning; }
	
	public void onSelectionChanged(PurchaseEntry entry) {
		if (this.selected == entry) { return; }
		this.selected = entry;
		this.mainScreen.mirrorSelectionChanged(entry);
//		this.scrollableRefit();
	}
	
	public void addNewEntry() {
		PurchaseEntry entry = new DialogNewEntry(this.shell, SWT.NONE).open();
		if (entry == null) { return; }
		this.mainScreen.mirrorEntryInsertion(entry);
	}

	public void nextStep() {
		this.selected.gotoNextStep();
		this.mirrorEntryUpdate();
	}
	
	public void mirrorEntryUpdate() {
		this.mainScreen.mirrorEntryUpdate(this.selected);
		this.entries.saveEntry(this.selected);
	}
	
	public void scrollableRefit() {
		this.mainScreen.scrollableRefit();
	}
}
