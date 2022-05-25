package qwertzite.purchaseprocedures2.procedure.variable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.google.gson.JsonObject;

import qwertzite.purchaseprocedures2.Log;
import qwertzite.purchaseprocedures2.util.TimeDateUtil;


public class VariableDate extends Variable {
	
	private LocalDate value;
	
	public VariableDate(LocalDate value) {
		this.value = value;
	}
	
	@Override
	public JsonObject writeToJson(JsonObject json) {
		json.addProperty("has", this.value != null);
		if (this.value != null) {
			json.addProperty("year", value.getYear());
			json.addProperty("month", this.value.getMonthValue());
			json.addProperty("day", this.value.getDayOfMonth());
		}
		return json;
	}
	
	public static VariableDate fromJson(JsonObject json) {
		if (json.get("has").getAsBoolean()) {
			int year = json.get("year").getAsInt();
			int month = json.get("month").getAsInt();
			int day = json.get("day").getAsInt();
			return new VariableDate(LocalDate.of(year, month, day));
		} else {
			return new VariableDate(null);
		}
	}

	@Override
	public String getStringValue() {
		return this.value.format(DateTimeFormatter.ISO_DATE);
	}

	@Override
	public int getIntValue() {
		return this.value.getYear() * 10000 + this.value.getMonthValue() * 100 + this.value.getDayOfMonth();
	}

	@Override
	public boolean getBoolValue() {
		return false;
	}
	
	@Override
	public LocalDate getDateValue() {
		return this.value;
	}

	@Override
	public boolean setStringValue(String value) {
		try {
			this.value = LocalDate.parse(value);
			return true;
		} catch(DateTimeParseException e) {
			return false;
		}
	}

	@Override
	public void setBoolValue(boolean value) {
		Log.warn("Tried to set boolean value to VariableDate!");
	}
	
	@Override
	public void setDateValue(LocalDate date) {
		this.value = date;
	}
	
	@Override
	public String getDisplayString() {
		if (this.value == null) { return "not set"; }
		return this.value.getYear() + "/" + this.value.getMonthValue() + "/" + this.value.getDayOfMonth() + " (" + TimeDateUtil.jpWeek(this.value.getDayOfWeek()) + ")";
	}

}
