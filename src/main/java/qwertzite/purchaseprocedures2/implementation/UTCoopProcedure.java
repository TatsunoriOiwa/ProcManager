package qwertzite.purchaseprocedures2.implementation;

import qwertzite.purchaseprocedures2.procedure.ProcedureNode;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementLabel;

public class UTCoopProcedure {
	
	private static void node0(ProcedureNode node0) {
		node0.getElements().add(0, new ElementLabel("見積もり時に，見積もりのみであることを明記する"));
	}
	
	private static void node5(ProcedureNode node5) {
		node5.getElements().add(0, new ElementLabel("学生証を持参し，「校費で購入，割澤伸一，内線番号4641」と言う．"));
	}
	
	public static class UTCoopNormal extends DefaultProcedure {
		public UTCoopNormal() {
			super("coop_u100t", "大学生協　十万円以下");
			
			UTCoopProcedure.node0(this.getProcedureNode(0));
			UTCoopProcedure.node5(this.getProcedureNode(5));
			
			
		}
		
	}
	
	public static class UTCoopHighValue extends HighValueProcedure {
		
		public UTCoopHighValue() {
			super("coop_u100t", "大学生協　十万円以上");
			
			UTCoopProcedure.node0(this.getProcedureNode(0));
			UTCoopProcedure.node5(this.getProcedureNode(5));
			
		}
		
	}
}
