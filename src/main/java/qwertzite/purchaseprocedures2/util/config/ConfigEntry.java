package qwertzite.purchaseprocedures2.util.config;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigEntry {

	private final String name;
	private final String def;
	private final Consumer<String> setter;
	private final Supplier<?> accessor;
	
	public ConfigEntry(String name, int defVal, Consumer<String> setter, Supplier<?> accessor) {
		this(name, String.valueOf(defVal), setter, accessor);
	}
	
	public ConfigEntry(String name, boolean i, Consumer<String> setter, Supplier<?> accessor) {
		this(name, String.valueOf(i), setter, accessor);
	}

	public ConfigEntry(String name, String defVal, Consumer<String> setter, Supplier<?> accessor) {
		this.name = name;
		this.def = defVal;
		this.setter = setter;
		this.accessor = accessor;
	}
	
	public void setValue(Map<String, String> map) {
		String val = map.get(this.name);
		this.setter.accept(val != null ? val : def);
	}
	
	public String getString() {
		return this.name + AbstractConfig.SEPARATOR + String.valueOf(this.accessor.get());
	}

}
