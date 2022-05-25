package qwertzite.purchaseprocedures2.procedure.variable;

import java.time.LocalDate;

import com.google.gson.JsonObject;

public class VariableBoolean extends Variable {
	
	private boolean value;
	
	public VariableBoolean(boolean value) {
		this.value = value;
	}
	
	public static VariableBoolean fromJson(JsonObject json) {
		return new VariableBoolean(json.get("value").getAsBoolean());
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
		return this.value ? 1 : 0;
	}
	
	public boolean getBoolValue() {
		return this.value;
	}

	@Override
	public LocalDate getDateValue() {
		return null;
	}

	@Override
	public boolean setStringValue(String value) {
		try {
			this.value = Boolean.valueOf(value);
			return true;
		} catch (NumberFormatException exception) {
			return false;
		}
	}
	
	@Override
	public void setBoolValue(boolean value) {
		this.value = value;
	}
	
	@Override
	public String getDisplayString() {
		return this.getStringValue();
	}
}
