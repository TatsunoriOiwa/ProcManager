package qwertzite.purchaseprocedures2.procedure.nodeelements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import qwertzite.purchaseprocedures2.util.StringUtil;

public class ElementTextTemplate extends NodeElement {
	
	private List<String> lines = new ArrayList<>();
	private String format;
	private boolean multiline;
	
	public ElementTextTemplate(String nodeLabel, String...format) {
		super(nodeLabel);
		this.lines.clear();
		this.lines.addAll(Arrays.asList(format));
		this.updateFormatString();
	}
	
	public ElementTextTemplate appendLines(String...lines) {
		this.lines.addAll(Arrays.asList(lines));
		this.updateFormatString();
		return this;
	}
	
	public ElementTextTemplate insertLines(int index, String... lines) {
		this.lines.addAll(index, Arrays.asList(lines));
		this.updateFormatString();
		return this;
	}
	public ElementTextTemplate replaceLine(int index, String line) {
		this.lines.remove(index);
		this.lines.add(index, line);
		this.updateFormatString();
		return this;
	}
	
	public ElementTextTemplate removeLine(int index) {
		this.lines.remove(index);
		this.updateFormatString();
		return this;
	}
	
	private void updateFormatString() {
		this.format = String.join(System.lineSeparator(), this.lines);
		this.multiline = this.lines.size() > 1;
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
	public boolean isMultiLine() {
		return this.multiline;
	}
	
	@Override
	public String getStringValue(PurchaseEntry entry) {
		return StringUtil.substitute(this.format, entry.getVariables());
	}

	@Override
	public boolean setStringValue(PurchaseEntry entry, String value) {
		return false;
	}

}
