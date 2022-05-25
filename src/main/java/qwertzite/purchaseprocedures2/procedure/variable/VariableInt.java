package qwertzite.purchaseprocedures2.procedure.variable;

import java.time.LocalDate;

import com.google.gson.JsonObject;

public class VariableInt extends Variable {
	
	private int value;
	
	public VariableInt(int value) {
		this.value = value;
	}
	
	public static VariableInt fromJson(JsonObject json) {
		return new VariableInt(json.get("value").getAsInt());
	}
	
	@Override
	public JsonObject writeToJson(JsonObject json) {
		json.addProperty("value", this.value);
		return json;
	}
	
	@Override
	public String getStringValue() {
		return String.valueOf(this.value);
	}

	@Override
	public int getIntValue() {
		return this.value;
	}
	
	public boolean getBoolValue() {
		return this.value != 0;
	}
	
	@Override
	public LocalDate getDateValue() {
		return LocalDate.of(this.value/10000%100, this.value/100%100, this.value%100);
	}
	
	@Override
	public boolean setStringValue(String value) {
		try {
			this.value = Integer.valueOf(value);
			return true;
		} catch (NumberFormatException exception) {
			return false;
		}
	}
	
	public boolean setIntValue(int value) {
		this.value = value;
		return true;
	}
	
	@Override
	public void setBoolValue(boolean value) {
		this.value = value ? 1 : 0;
	}
	
	@Override
	public String getDisplayString() {
		return String.format("%,d", this.getIntValue());
	}
}
