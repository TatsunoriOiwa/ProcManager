package qwertzite.purchaseprocedures2.implementation;

import java.util.List;

import qwertzite.purchaseprocedures2.procedure.ProcedureNode;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementBoolButton;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementLabel;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementTextTemplate;

public class HighValueProcedure extends DefaultProcedure {
	
	public HighValueProcedure() {
		this("over_100t", "10万円以上　標準");
	}
	
	public HighValueProcedure(String registryId, String procedureName) {
		super(registryId, procedureName);
		this.setDescription(
				  "十万円以上の物品，一般の場合\n"
				+ "十万円を超える場合，備品扱いになる．");
		
		List<ProcedureNode> nodes = this.getProcedureNodes();
		
		ProcedureNode node0 = nodes.get(0);
		node0.addElement(new ElementLabel("値切る必要はない"));
		
		ProcedureNode node1 = nodes.get(1);
		((ElementTextTemplate) node1.getElements().get(3))
		.insertLines(3, "10万円を超える物品です．");
		
		ProcedureNode node3 = nodes.get(3);
		((ElementTextTemplate) node3.getElements().get(4))
		.insertLines(5,
				"",
				"また高額商品の為、以下の書類と情報が必要です。",
				"・商品カタログページ：ＰＤＦ画像でご送付ください。(メーカー名・品番・商品名含む)");
		
		ProcedureNode node5 = nodes.get(5);
		node5.getElements().add(new ElementBoolButton("カタログ/webページの写し(価格の記載は無くてよい)",	 "submission_checklist8_catalog", false));
		node5.getElements().add(new ElementBoolButton("新たな付箋に設置場所・製造元社名を記載し貼り付ける",	 "submission_checklist9_place", false));
		node5.addCompletionCondition(node5.getCompletionCondition().and(e -> {
			return e.hasBoolVariable("submission_checklist8_catalog") && e.getExistingVariable("submission_checklist8_catalog").getBoolValue() &&
					e.hasBoolVariable("submission_checklist9_place") && e.getExistingVariable("submission_checklist9_place").getBoolValue();
		}));
	}
	
	
}
