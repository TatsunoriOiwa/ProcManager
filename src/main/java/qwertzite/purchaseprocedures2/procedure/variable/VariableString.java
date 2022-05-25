package qwertzite.purchaseprocedures2.procedure.variable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.google.gson.JsonObject;

import qwertzite.purchaseprocedures2.util.TimeDateUtil;

public class VariableString extends Variable {
	
	private String value;
	
	public VariableString(String value) {
		this.value = value;
	}
	
	public static VariableString fromJson(JsonObject json) {
		return new VariableString(json.get("value").getAsString());
	}
	
	@Override
	public JsonObject writeToJson(JsonObject json) {
		json.addProperty("value", this.value);
		return json;
	}
	
	@Override
	public String getStringValue() {
		return this.value;
	}

	/**
	 *  returns the length of value
	 */
	@Override
	public int getIntValue() {
		return this.value.length();
	}
	
	public boolean getBoolValue() {
		return Boolean.valueOf(this.value);
	}
	
	@Override
	public LocalDate getDateValue() {
		try {
			return TimeDateUtil.fromISOString(this.value);
		} catch (DateTimeParseException e) {
			return null;
		}
	}
	
	@Override
	public boolean setStringValue(String value) {
		this.value = value;
		return true;
	}
	
	@Override
	public void setBoolValue(boolean value) {
		this.value = String.valueOf(value);
	}
	
	@Override
	public String getDisplayString() {
		return this.getStringValue();
	}
}
