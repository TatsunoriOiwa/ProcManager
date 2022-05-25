package qwertzite.purchaseprocedures2.procedure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Procedure {
	private static final List<Procedure> PROCEDURE_LIST = new ArrayList<>();
	private static final Map<String, Procedure> PROCEDURES = new HashMap<>();
	
	private final String procedureRegistryId;
	private String procedureName;
	private String description;
	private List<ProcedureNode> nodes;
	
	public Procedure(String registryId, String procedureName) {
		this.procedureRegistryId = registryId;
		this.procedureName = procedureName;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}
	
	public List<ProcedureNode> getProcedureNodes() {
		return this.nodes;
	}
	
	protected void setProcedureNodes(List<ProcedureNode> nodes) { 
		this.nodes = nodes;
		this.updateNodeIndex();
	}
	
	private void updateNodeIndex() {
		int index = 0;
		for (ProcedureNode node : this.nodes) {
			node.nodeIndex = index;
			index++;
		}
	}
	
	public String getProcedureRegistryId() { return this.procedureRegistryId; }
	public String getProcedureName() { return this.procedureName; }
	public String getProcedureDescription() { return this.description; }
	
	public ProcedureNode getProcedureNode(int index) {
		if (this.getProcedureNodes().size() <= index) { return null; }
		return this.getProcedureNodes().get(index);
	}
	
	public static void registerProcedure(Procedure procedure) {
		PROCEDURES.put(procedure.getProcedureRegistryId(), procedure);
		PROCEDURE_LIST.add(procedure);
	}
	
	public static Procedure getProcedureFromName(String procedure) {
		return PROCEDURES.get(procedure);
	}
	
	public static List<Procedure> getProcedureList() {
		return PROCEDURE_LIST;
	}
}
