package qwertzite.purchaseprocedures2.procedure.nodeelements;

import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class ElementTextInt extends NodeElement {

	private boolean readonly;
	private String variable;
	private int defaultValue;
	
	public ElementTextInt(String label, String variable, int defaultValue, boolean readonly) {
		super(label);
		this.readonly = readonly;
		this.variable = variable;
		this.defaultValue = defaultValue;
	}

	@Override
	public EnumInterfaceType getInterfaceType() {
		return EnumInterfaceType.TEXT;
	}
	
	@Override
	public String getStringValue(PurchaseEntry entry) {
		return String.valueOf(entry.getVariable(this.variable, this.defaultValue).getIntValue());
	}
	
	@Override
	public boolean setStringValue(PurchaseEntry entry, String value) {
		return entry.getVariable(this.variable, value).setStringValue(value);
	}
}
