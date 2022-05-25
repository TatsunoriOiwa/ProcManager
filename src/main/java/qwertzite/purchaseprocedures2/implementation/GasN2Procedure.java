package qwertzite.purchaseprocedures2.implementation;

import java.util.List;

import qwertzite.purchaseprocedures2.procedure.ProcedureNode;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementBoolButton;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementLabel;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementTextTemplate;

public class GasN2Procedure extends DefaultProcedure {
	
	public GasN2Procedure() {
		super("gas_N2", "低温液化室 N2 47L");
		this.setDescription("低温液化室より窒素ガス47Lを購入する際の手順");
		
		List<ProcedureNode> nodes = this.getProcedureNodes();
		
		ProcedureNode node1 = nodes.get(1);
		((ElementTextTemplate) node1.getElements().get(3))
		.insertLines(8, "支払方法：伝票発行")
		.replaceLine(9, "購入先： 低温液化室 (外注)")
		.removeLine(10);
		
		ProcedureNode node3 = nodes.get(3);
		node3.getElements().clear();
		node3.addElement(
				new ElementBoolButton("納期は土日・祝日・月末を避ける", "ordering_dod_ok", false),
				new ElementLabel(
				  "内線 63514に電話をする\n"
				+ "\n"
				+ "いつもお世話になっております．\n"
				+ "新領域人間環境学専攻　割澤・福井・米谷研究室の大岩と申します．\n"
				+ "\n"
				+ "ガスボンベを購入したいのですが，\n"
				+ "窒素47Lを{quantity}本お願いします．"))
		.addCompletionCondition(e -> {
			return e.hasBoolVariable("ordering_dod_ok") && e.getExistingVariable("ordering_dod_ok").getBoolValue();
		});
		
		ProcedureNode node5 = nodes.get(5);
		node5.getElements().add(0, new ElementLabel("カードを持参しボンベ庫に行く"));
		node5.getElements().add(1, new ElementLabel("受け取り時に，伝票発行を選択する．"));
	}
}
