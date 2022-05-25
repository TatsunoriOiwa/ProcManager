package qwertzite.purchaseprocedures2.procedure.nodeelements;

import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import qwertzite.purchaseprocedures2.util.StringUtil;

public class ElementLabel extends NodeElement {
	
	private String value;
	
	public ElementLabel(String...format) {
		this(String.join(System.lineSeparator(), format));
	}
	
	public ElementLabel(String format) {
		super(format);
		this.value = format;
	}
	
	@Override
	public EnumInterfaceType getInterfaceType() {
		return EnumInterfaceType.LABEL;
	}

	@Override
	public String getStringValue(PurchaseEntry entry) {
		return StringUtil.substitute(value, entry.getVariables());
	}

	@Override
	public boolean setStringValue(PurchaseEntry entry, String value) {
		return false;
	}

}
