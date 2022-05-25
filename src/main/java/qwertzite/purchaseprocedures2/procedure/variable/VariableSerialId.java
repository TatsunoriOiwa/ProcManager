package qwertzite.purchaseprocedures2.procedure.variable;

import java.time.LocalDate;

import com.google.gson.JsonObject;

import qwertzite.purchaseprocedures2.Log;
import qwertzite.purchaseprocedures2.procedure.PurchaseId;

public class VariableSerialId extends Variable {
	
	private final PurchaseId value;
	
	public VariableSerialId(PurchaseId value) {
		this.value = value;
	}
	
	public static VariableSerialId fromJson(JsonObject json) {
		return new VariableSerialId(PurchaseId.fromJson(json.getAsJsonObject("value")));
	}
	
	
	@Override
	public JsonObject writeToJson(JsonObject json) {
		json.add("value", this.value.toJson());
		return json;
	}
	
	@Override
	public String getStringValue() {
		return this.value.toString();
	}

	@Override
	public int getIntValue() {
		return (value.getFinantialYear() << 16) + value.getSerialIndex();
	}
	
	public boolean getBoolValue() {
		return false;
	}
	
	@Override
	public LocalDate getDateValue() {
		Log.warn("Serial ID cannot be converted into Date.");
		return null;
	}
	
	@Override
	public PurchaseId getSerialValue() {
		return this.value;
	}

	@Override
	public boolean setStringValue(String value) {
		Log.warn("Purchase ID cannot be modified!");
		return false;
	}
	
	@Override
	public void setBoolValue(boolean value) {
		Log.warn("Purchase ID cannot be modified!");
	}
	
	@Override
	public String getDisplayString() {
		return this.getStringValue();
	}
}
