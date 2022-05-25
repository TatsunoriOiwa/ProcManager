package qwertzite.purchaseprocedures2.procedure.nodeelements;

import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class ElementBoolButton extends NodeElement {
	
	private String variable;
	private boolean defaultValue;
	
	public ElementBoolButton(String nodeLabel, String variable, boolean defaultValue) {
		super(nodeLabel);
		this.variable = variable;
		this.defaultValue = defaultValue;
	}

	@Override
	public EnumInterfaceType getInterfaceType() {
		return EnumInterfaceType.BUTTON;
	}

	@Override
	public String getStringValue(PurchaseEntry entry) {
		return entry.getVariable(this.variable, this.defaultValue).getBoolValue() ? "true" : "false";
	}

	@Override
	public boolean setStringValue(PurchaseEntry entry, String value) {
		return entry.getVariable(this.variable, value.isEmpty()).setStringValue(value);
	}
	
	@Override
	public boolean getBoolValue(PurchaseEntry entry) {
		return entry.getVariable(this.variable, this.defaultValue).getBoolValue();
	}
	@Override
	public void setBoolValue(PurchaseEntry entry, boolean value) {
		entry.getVariable(this.variable, value).setBoolValue(value);
	}

}
