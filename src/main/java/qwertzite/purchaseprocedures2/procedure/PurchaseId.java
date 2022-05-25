package qwertzite.purchaseprocedures2.procedure;

import java.util.List;

import com.google.gson.JsonObject;

import qwertzite.purchaseprocedures2.magicconst.ConstPurchase;
import qwertzite.purchaseprocedures2.util.TimeDateUtil;

public class PurchaseId {
	private final int finantialYear;
	private final int serialIndex;
	
	public PurchaseId(int finantialYear, int serialIndex) {
		this.finantialYear = finantialYear;
		this.serialIndex = serialIndex;
	}
	
	public static PurchaseId next(List<PurchaseEntry> entries) {
		if (entries.isEmpty()) {
			return new PurchaseId(TimeDateUtil.getFinantialYear(), 1);
		}
		PurchaseId prev = entries.get(entries.size() - 1).getSerial();
		int year = TimeDateUtil.getFinantialYear();
		return new PurchaseId(year, prev.getFinantialYear() == year ? prev.getSerialIndex()+1 : 1);
	}
	
	public static PurchaseId fromJson(JsonObject object) {
		int finantialYear = object.get("year").getAsInt();
		int serialIndex = object.get("serial").getAsInt();
		return new PurchaseId(finantialYear, serialIndex);
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("year", this.getFinantialYear());
		json.addProperty("serial", this.getSerialIndex());
		return json;
	}
	
	public int getFinantialYear() {
		return finantialYear;
	}

	public int getSerialIndex() {
		return serialIndex;
	}
	
	@Override
	public String toString() {
		return (this.getFinantialYear() % 100) + "HEI-" + ConstPurchase.USER_NAME + "-" + this.getSerialIndex();
	}
}
