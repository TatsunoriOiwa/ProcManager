package qwertzite.purchaseprocedures2.procedure;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import qwertzite.purchaseprocedures2.procedure.variable.Variable;
import qwertzite.purchaseprocedures2.procedure.variable.VariableBoolean;
import qwertzite.purchaseprocedures2.procedure.variable.VariableDate;
import qwertzite.purchaseprocedures2.procedure.variable.VariableInt;
import qwertzite.purchaseprocedures2.procedure.variable.VariableSerialId;
import qwertzite.purchaseprocedures2.procedure.variable.VariableString;

public class PurchaseEntry {
	public static final String VARNAME_STEP = "system.step";
	public static final String VARNAME_ENTRY_ID = "system.purchase_id";
	public static final String VARNAME_SUBJECT = "system.subject";
	public static final String VARNAME_NOTE = "system.note";
	
	public static final EntryComparator COMPARATOR = new EntryComparator();
	
	private final Procedure procedure;
	private EnumProcedureException exception;
	private final Map<String, Variable> variables = new HashMap<>();
	
	public PurchaseEntry(Procedure procedure, PurchaseId serial, String subject) {
		this.procedure = procedure;
		this.exception = procedure == null ? null : EnumProcedureException.NORMAL;
		this.variables.put(VARNAME_STEP, new VariableInt(0));
		this.variables.put(VARNAME_ENTRY_ID, new VariableSerialId(serial));
		this.variables.put(VARNAME_SUBJECT, new VariableString(subject));
		this.variables.put(VARNAME_NOTE, new VariableString(""));
	}
	
	private PurchaseEntry(Procedure procedure, Map<String, Variable> variables, EnumProcedureException exception) {
		this.procedure = procedure;
		this.exception = exception;
		this.variables.putAll(variables);
	}
	
	public static PurchaseEntry fromJson(JsonObject json) {
		String procedureId = json.get("procedure").getAsString();
		Procedure procedure = Procedure.getProcedureFromName(procedureId);
		EnumProcedureException exception = EnumProcedureException.valueOf(json.get("exception").getAsString());
		Map<String, Variable> variables = new HashMap<>();
		JsonObject var = json.getAsJsonObject("variables");
		for (Map.Entry<String, JsonElement> e : var.entrySet()) {
			variables.put(e.getKey(), Variable.fromJson(e.getValue().getAsJsonObject()));
		}
		return new PurchaseEntry(procedure, variables, exception);
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("procedure", this.getProcedure().getProcedureRegistryId());
		json.addProperty("exception", this.exception.name());
		JsonObject var = new JsonObject();
		for (Map.Entry<String, Variable> e : this.variables.entrySet()) {
			var.add(e.getKey(), e.getValue().toJson());
		}
		json.add("variables", var);
		return json;
	}
	
	public boolean isMissingEntry() { return this.getProcedure() == null; }
	public Procedure getProcedure() { return this.procedure; }
	public EnumProcedureException getExceptionStatus() { return this.exception; }
	public void setExceptionStatus(EnumProcedureException exception) { this.exception = exception; }
	public Map<String, Variable> getVariables() { return this.variables; }
	
	public Variable getVariable(String key, String defaultValue) {
		if (!this.variables.containsKey(key)) {
			this.variables.put(key, new VariableString(defaultValue));
		}
		return this.variables.get(key);
	}
	
	public Variable getVariable(String key, int defaultValue) {
		if (!this.variables.containsKey(key)) {
			this.variables.put(key, new VariableInt(defaultValue));
		}
		return this.variables.get(key);
	}
	
	public Variable getVariable(String key, boolean defaultValue) {
		if (!this.variables.containsKey(key)) {
			this.variables.put(key, new VariableBoolean(defaultValue));
		}
		return this.variables.get(key);
	}
	
	public Variable getVariable(String key, LocalDate defaultValue) {
		if (!this.variables.containsKey(key)) {
			this.variables.put(key, new VariableDate(defaultValue));
		}
		return this.variables.get(key);
	}
	
	public boolean hasStringVariable(String key) {
		return this.variables.containsKey(key) && this.variables.get(key) instanceof VariableString;
	}
	
	public boolean hasIntVariable(String key) {
		return this.variables.containsKey(key) && this.variables.get(key) instanceof VariableInt;
	}
	
	public boolean hasBoolVariable(String key) {
		return this.variables.containsKey(key) && this.variables.get(key) instanceof VariableBoolean;
	}
	
	public Variable getExistingVariable(String key) {
		return this.variables.get(key);
	}
	
	public int getCurrentStep() {
		return this.getExistingVariable(VARNAME_STEP).getIntValue();
	}
	
	public String getStepName() {
		if (this.isMissingEntry()) { return "　不明　"; }
		switch (this.getExceptionStatus()) {
		case ABORTED:
			return "　中止　";
		case DISUSED:
			return "　廃番　";
		case NORMAL:
		default:
			int step = this.getCurrentStep();
			ProcedureNode node = this.getProcedure().getProcedureNode(step);
			if (node == null) { return "手続完了"; }
			else { return node.getStepName(); }
		}
	}
	
	public void gotoNextStep() {
		VariableInt var = (VariableInt) this.getExistingVariable(VARNAME_STEP);
		var.setIntValue(var.getIntValue() + 1);
	}
	
	public PurchaseId getSerial() {
		return this.getExistingVariable(VARNAME_ENTRY_ID).getSerialValue();
	}
	
	public String getSubject() {
		return this.getExistingVariable(VARNAME_SUBJECT).getStringValue();
	}
	
	public String getGeneralNote() {
		return this.getVariable(VARNAME_NOTE, "").getStringValue();
	}
	
	public void setGeneralNote(String value) {
		this.getVariable(VARNAME_NOTE, "").setStringValue(value);
	}
	
	public static class EntryComparator implements Comparator<PurchaseEntry> {
		@Override
		public int compare(PurchaseEntry o1, PurchaseEntry o2) {
			int i = Integer.compare(o1.getSerial().getFinantialYear(), o2.getSerial().getFinantialYear());
			if (i != 0) { return i; }
			return Integer.compare(o1.getSerial().getSerialIndex(), o2.getSerial().getSerialIndex());
		}
	}
}
