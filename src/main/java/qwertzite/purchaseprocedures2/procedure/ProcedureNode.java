package qwertzite.purchaseprocedures2.procedure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import qwertzite.purchaseprocedures2.procedure.nodeelements.NodeElement;

public class ProcedureNode {
	
	int nodeIndex;
	private final List<NodeElement> elements = new ArrayList<>();
	private String nodetitle;
	private String stepName;
	private String completeText;
	
	private Predicate<PurchaseEntry> predicateCompleted = e -> true;
	private Predicate<PurchaseEntry> predicateOnEntryModified = e -> false;
	
	public ProcedureNode(String title, String stepName, String completeText) {
		this.nodetitle = title;
		this.stepName = stepName;
		this.completeText = completeText;
	}
	
	public ProcedureNode addElement(NodeElement... elements) {
		Collections.addAll(this.elements, elements);
		return this;
	}
	
	public ProcedureNode addCompletionCondition(Predicate<PurchaseEntry> predicate) {
		this.predicateCompleted = predicate;
		return this;
	}
	
	public Predicate<PurchaseEntry> getCompletionCondition() { return this.predicateCompleted; }
	
	/**
	 *  predicate returns true if entry GUI needs to be updated.
	 * @param consumer
	 * @return
	 */
	public ProcedureNode addModifyListener(Predicate<PurchaseEntry> consumer) {
		this.predicateOnEntryModified = consumer;
		return this;
	}
	
	public int getNodeIndex() { return this.nodeIndex; }
	public String getNodeTitle() { return this.nodetitle; }
	public List<NodeElement> getElements() { return this.elements; }
	public String getCompleteText() { return this.completeText; }
	public String getStepName() { return this.stepName; }
	
	public boolean isCompleted(PurchaseEntry entry) { return entry.getCurrentStep() == this.nodeIndex && this.predicateCompleted.test(entry); }
	public boolean onEntryModified(PurchaseEntry entry) { return this.predicateOnEntryModified.test(entry); }
}
