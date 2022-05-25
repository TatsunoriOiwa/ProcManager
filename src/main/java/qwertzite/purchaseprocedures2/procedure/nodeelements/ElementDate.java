package qwertzite.purchaseprocedures2.procedure.nodeelements;

import java.time.LocalDate;

import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import qwertzite.purchaseprocedures2.util.TimeDateUtil;

public class ElementDate extends NodeElement {
	private String variable;
	private LocalDate defaultValue;
	private boolean readOnly;
	
	public ElementDate(String nodeLabel, String variable, LocalDate defaultValue) {
		this(nodeLabel, variable, defaultValue, false);
	}
	
	public ElementDate(String nodeLabel, String variable, LocalDate defaultValue, boolean readOnly) {
		super(nodeLabel);
		this.variable = variable;
		this.defaultValue = defaultValue;
		this.readOnly = readOnly;
	}
	
	@Override
	public EnumInterfaceType getInterfaceType() {
		return EnumInterfaceType.DATE;
	}
	
	public boolean isReadOnly() { return this.readOnly; }

	@Override
	public String getStringValue(PurchaseEntry entry) {
		return TimeDateUtil.toISOString(entry.getVariable(this.variable, this.defaultValue).getDateValue());
	}

	@Override
	public boolean setStringValue(PurchaseEntry entry, String value) {
		return entry.getVariable(this.variable, this.defaultValue).setStringValue(value);
	}
	
	@Override
	public LocalDate getDateValue(PurchaseEntry entry) {
		return entry.getVariable(this.variable, this.defaultValue).getDateValue();
	}

	public void setDateValue(PurchaseEntry entry, LocalDate date) {
		entry.getVariable(this.variable, this.defaultValue).setDateValue(date);
	}

}
