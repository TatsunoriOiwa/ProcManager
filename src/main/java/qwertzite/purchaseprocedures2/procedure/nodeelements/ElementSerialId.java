package qwertzite.purchaseprocedures2.procedure.nodeelements;

import qwertzite.purchaseprocedures2.Log;
import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class ElementSerialId extends NodeElement {

	
	public ElementSerialId(String label) {
		super(label);
	}
	
	@Override
	public EnumInterfaceType getInterfaceType() {
		return EnumInterfaceType.TEXT;
	}
	
	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public String getStringValue(PurchaseEntry entry) {
		return entry.getSerial().toString();
	}

	@Override
	public boolean setStringValue(PurchaseEntry entry, String value) {
		Log.warn("Serial ID cannot be modified! value={}", value);
		return true;
	}

}
