package qwertzite.purchaseprocedures2.procedure.variable;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.JsonObject;

import qwertzite.purchaseprocedures2.Log;
import qwertzite.purchaseprocedures2.procedure.PurchaseId;

public abstract class Variable {
	
	private static Map<String, Function<JsonObject, ? extends Variable>> constructors = new HashMap<>();
	private static Map<Class<? extends Variable>, String> registryIDs = new HashMap<>();
	static {
		registerVariableType("boolean", VariableBoolean.class, VariableBoolean::fromJson);
		registerVariableType("int", VariableInt.class, VariableInt::fromJson);
		registerVariableType("string", VariableString.class, VariableString::fromJson);
		registerVariableType("serial", VariableSerialId.class, VariableSerialId::fromJson);
		registerVariableType("date", VariableDate.class, VariableDate::fromJson);
	}
	
	protected static <T extends Variable> void registerVariableType(String name, Class<T> clazz, Function<JsonObject, T> constructor) {
		constructors.put(name, constructor);
		registryIDs.put(clazz, name);
	}
	
	public static Variable fromJson(JsonObject json) {
		return constructors.get(json.get("type").getAsString()).apply(json);
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("type", registryIDs.get(this.getClass()));
		return this.writeToJson(json);
	}
	public abstract JsonObject writeToJson(JsonObject json);
	
	public abstract String getStringValue();
	public abstract int getIntValue();
	public abstract boolean getBoolValue();
	public PurchaseId getSerialValue() {
		Log.warn("This type of variable can not be interprited as a serial id.");
		return null;
	}
	public abstract LocalDate getDateValue();
	
	public abstract boolean setStringValue(String value);
	public abstract void setBoolValue(boolean value);
	public void setDateValue(LocalDate date) { Log.warn("Invalid Operation! Tried to set date value"); }
	
	public abstract String getDisplayString();
}
