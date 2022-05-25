package qwertzite.purchaseprocedures2.procedure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import qwertzite.purchaseprocedures2.Log;
import qwertzite.purchaseprocedures2.storage.FileIO;
import qwertzite.purchaseprocedures2.util.TimeDateUtil;

public class EntryManager {
	
	private List<PurchaseEntry> entries = new ArrayList<>();
	
	public void loadEntries() {
		File[] files = FileIO.getEntryFiles();
		
		for (File f : files) {
			JsonObject json = FileIO.getJsonObjFromFile(f);
			this.entries.add(PurchaseEntry.fromJson(json));
		}
		this.entries.sort(PurchaseEntry.COMPARATOR);
		
		if (this.entries.isEmpty()) { return; }
		
		PurchaseId lastID = this.entries.get(this.entries.size()-1).getSerial();
		int year = TimeDateUtil.getFinantialYear();
		int pos = this.entries.size() - 2;
		while (pos >= 0) {
			PurchaseId id = this.entries.get(pos).getSerial();
			if (id.getFinantialYear() != lastID.getFinantialYear()) {
				break;
			}
			if (id.getSerialIndex() < lastID.getSerialIndex() -1) { // 1 2 4 5
				pos++;
				lastID = new PurchaseId(year, lastID.getSerialIndex()-1);
				this.entries.add(pos, new PurchaseEntry(null, lastID, ""));
				Log.warn("Detected purchase id serial index discontinuity! {}", lastID);
			} else {
				lastID = id;
			}
			pos--;
		}
	}
	
	public void saveEntries() {
		for (PurchaseEntry entry : this.entries) {
			this.saveEntry(entry);
		}
	}
	
	public void saveEntry(PurchaseEntry entry) {
		if (entry.isMissingEntry()) { 
			Log.info("Skipped " + entry.getSerial() + " as it is a missing entry.");
			return;
		}
		JsonObject json = entry.toJson();
		FileIO.saveEntryJson(json, entry.getSerial());
		Log.info("Saved " + entry.getSerial());
	}
	
	public List<PurchaseEntry> getEntries() {
		return this.entries;
	}
	
	public PurchaseEntry addNewEntry(Procedure procedure, String subject) {
		PurchaseEntry entry = new PurchaseEntry(procedure, PurchaseId.next(this.getEntries()), subject);
		this.entries.add(entry);
		return entry;
	}
	
}
