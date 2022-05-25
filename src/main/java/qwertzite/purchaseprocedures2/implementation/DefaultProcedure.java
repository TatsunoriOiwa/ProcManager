package qwertzite.purchaseprocedures2.implementation;

import java.time.LocalDate;
import java.util.LinkedList;

import qwertzite.purchaseprocedures2.procedure.Procedure;
import qwertzite.purchaseprocedures2.procedure.ProcedureNode;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementBoolButton;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementDate;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementSerialId;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementTextInt;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementTextString;
import qwertzite.purchaseprocedures2.procedure.nodeelements.ElementTextTemplate;
import qwertzite.purchaseprocedures2.util.TimeDateUtil;

/**
 * 
 * @author ellip
 * @date 2020/12/04
 */
public class DefaultProcedure extends Procedure {
	
	public DefaultProcedure() {
		this("under_100t", "10万円以下　標準");
	}
	
	protected DefaultProcedure(String registryId, String procedureName) {
		super(registryId, procedureName);
		this.setDescription("十万円以下の物品，一般の場合");
		
		LinkedList<ProcedureNode> nodes = new LinkedList<>();
		nodes.add(new ProcedureNode("新規", "新規手続", " CONFIRMED ") // 0
				.addElement(
						new ElementSerialId("発注番号"),
						new ElementTextString("品目", "itemname_full", false),
						new ElementTextString("略称", "itemname_short", false),
						new ElementTextInt("数量", "quantity", 0, false),
						new ElementTextInt("総額 (税込)", "total_amount", 0, false),
						new ElementTextString("納品予定日", "date_of_delivery", false))
				.addCompletionCondition(e -> {
					return e.hasStringVariable("itemname_full") && !e.getExistingVariable("itemname_full").getStringValue().isEmpty() &&
							e.hasIntVariable("quantity") && e.getExistingVariable("quantity").getIntValue() > 0 &&
							e.hasIntVariable("total_amount") && e.getExistingVariable("total_amount").getIntValue() > 0;
				}));
		
		nodes.add(new ProcedureNode("購入申請", "購入申請", " COMPLETED ") // 1
				.addElement(
						new ElementTextTemplate("TO", "kometani@edu.k.u-tokyo.ac.jp"),
						new ElementTextTemplate("CC", "secretary@lelab.t.u-tokyo.ac.jp"),
						new ElementTextTemplate("TITLE", "【購入許可申請】 {itemname_short} x{quantity}"),
						new ElementTextTemplate("BODY",
								"米谷先生",
								"",
								"下記の品を発注致したく，許可を宜しくお願いします。",
								"",
								"【発注番号(候補)】： {" + PurchaseEntry.VARNAME_ENTRY_ID + "}",
								"品物・数量： {itemname_full} x{quantity}", // 5
								"購入目的： ",
								"総額： ￥{total_amount}(税込)",
								"購入先： ",
								"担当： ",
								"予定納期： {date_of_delivery}", // 10
								"",
								"")));
		nodes.add(new ProcedureNode("許可待ち", "許可待ち", " APPROVED ")); // 2
		
		nodes.add(new ProcedureNode("発注", "発注連絡", " COMPLETED ") // 3
				.addElement(
						new ElementDate("納期", "dod_ordering", null),
						new ElementBoolButton("納期は土日・祝日・月末を避ける", "ordering_dod_ok", false),
						new ElementTextString("納期備考", "dod_note", false),
						new ElementTextTemplate("TITLE", "{itemname_short}の発注について"),
						new ElementTextTemplate("BODY",
								"XX商事，XX様",							// 00
								"",
								"東京大学割澤・福井・米谷研究室の大岩です。",
								"いつもお世話になっております。",
								"下記の品を発注致しますので，ご対応を宜しくお願いします。",
								"",										//05
								"品名・個数：見積書No. XXX-YYYYの品",
								"{itemname_full} x{quantity}",
								"総額： ￥{total_amount}(税込)",
								"納期： {dod_ordering} {dod_note}",
								"支払い⽅法： 校費（月末締め・翌月25日銀行振り込み）",	// 10
								"納入場所： 東京大学柏キャンパス環境棟",
								"新領域・人間環境学専攻割澤・福井・米谷研究室 301号室",
								"",
								"",
								"以下、財務書類に関しましてお願い事項です。よろしくお願いいたします。",
								"",										// 15
								"【送付に関して】",
								"〒277-8563 千葉県柏市柏の葉5-1-5 東京大学 環境棟3Ｆ 320室（不在の場合は301室）",
								"宛にご送付ください。郵送宛て名には「東京大学新領域割澤研究室」という研究室名を必ずお入れください。",
								"",
								"【財務書類に関して】",						// 20
								"見積書原本、納品書、請求書の原本（要：社印の押印・日付の記載）を下さい。",
								"これらは商品同梱でお願いします。",
								"商品同梱が不可能であり別途発送を行う予定でしたら、その旨をお知らせください。",
								"宛名は「東京大学」宛でご作成ください。",
								"請求書類の備考等欄に発注番号({" + PurchaseEntry.VARNAME_ENTRY_ID + "}) を入れて下さい。", // 25
								"",
								"【振込に関して】",
								"振込予定日は請求書到着月の翌月25日を予定としております。",
								"請求書が月末までに届きませんと振込月が一か月ずれてしまうことがありますので、早めのご発送をお願いできれば幸いです。",
								"",										// 30
								"",
								"【発注番号】 {" + PurchaseEntry.VARNAME_ENTRY_ID + "}",
								""),
						new ElementBoolButton("総額を明記　(税込)", "ordering_checklist1_total_amount", false),
						new ElementBoolButton("宛先は部屋番号まで", "ordering_checklist2_room_no", false),
						new ElementBoolButton("発注番号を記載する", "ordering_checklist3_purchase_id", false))
				.addModifyListener(e -> { return true; })
				.addCompletionCondition(e -> {
					return e.hasBoolVariable("ordering_dod_ok") && e.getExistingVariable("ordering_dod_ok").getBoolValue() &&
							e.hasBoolVariable("ordering_checklist1_total_amount") && e.getExistingVariable("ordering_checklist1_total_amount").getBoolValue() &&
							e.hasBoolVariable("ordering_checklist2_room_no") && e.getExistingVariable("ordering_checklist2_room_no").getBoolValue() &&
							e.hasBoolVariable("ordering_checklist3_purchase_id") && e.getExistingVariable("ordering_checklist3_purchase_id").getBoolValue() &&
							e.hasBoolVariable("ordering_checklist4_sender") && e.getExistingVariable("ordering_checklist4_sender").getBoolValue();
				}));
		
		nodes.add(new ProcedureNode("発注完了報告", "発注報告", " COMPLETED ") // 4
				.addElement(
						new ElementTextTemplate("TO", "kometani@edu.k.u-tokyo.ac.jp"),
						new ElementTextTemplate("CC", "secretary@lelab.t.u-tokyo.ac.jp"),
						new ElementTextTemplate("TITLE", "【発注完了報告】{" + PurchaseEntry.VARNAME_ENTRY_ID + "} {itemname_short} x{quantity}"),
						new ElementTextTemplate("BODY",
								"米谷先生",
								"",
								"発注番号 {" + PurchaseEntry.VARNAME_ENTRY_ID + "} の下記の品を発注致しましたのでご報告致します。",
								"",
								"{itemname_full} x{quantity}",
								"価格： ￥{total_amount}(税込)",
								"予定納期： {dod_ordering} {dod_note}",
								"",
								"")));
		
		nodes.add(new ProcedureNode("受領・検収", "受領検収", " COMPLETED ") // 5
				.addElement(
						new ElementDate("実際の受け取り日", "dod_actual", null),
						new ElementDate("納品書に記載の日付", "dod_paper", null),
						new ElementDate("納品書の受け取り日", "dod_received", null),
						new ElementDate("サインすべき日付", "signature_date", null, true),
						new ElementBoolButton("受領書がある場合サインをして相手方に送る(FAX, メールで十分)",	 "submission_checklist0_received", false),
						new ElementBoolButton("A４でない場合A4コピー用紙に貼り付け",	 "submission_checklist1_A4", false),
						new ElementBoolButton("納品書を一番上に",					 "submission_checklist2_order", false),
						new ElementBoolButton("クリップで止める",						 "submission_checklist3_clip", false),
						new ElementBoolButton("書類は原本か　(PDFならば秘書に相談)",	 "submission_checklist4_non_pdf", false),
						new ElementBoolButton("3書類の宛先は「東京大学」か",			 "submission_checklist5_ut", false),
						new ElementBoolButton("3書類の品名・個数・金額は同じか",		 "submission_checklist6_same", false),
						new ElementBoolButton("付箋に発注番号を記載して貼り付ける",	 "submission_checklist7_stick", false))
				.addModifyListener(e -> {
					LocalDate actual = e.getVariable("dod_actual", (LocalDate)null).getDateValue();
					LocalDate paper = e.getVariable("dod_paper", (LocalDate)null).getDateValue();
					@SuppressWarnings("unused")
					LocalDate handed = e.getVariable("dod_received", (LocalDate)null).getDateValue();
					
					if (actual != null && paper != null) {
						LocalDate signature = TimeDateUtil.last(paper, actual);
						boolean flag = !signature.equals(e.getVariable("signature_date", (LocalDate)null).getDateValue());
						if (flag) { e.getVariable("signature_date", signature).setDateValue(signature); }
						return flag;
					} else {
						if (e.getVariable("signature_date", (LocalDate)null).getDateValue() != null) {
							e.getVariable("signature_date",(LocalDate)null).setDateValue(null);
							return true;
						}
						return false;
					}
				})
				.addCompletionCondition(e -> {
					return e.hasBoolVariable("submission_checklist0_received") && e.getExistingVariable("submission_checklist0_received").getBoolValue() &&
							e.hasBoolVariable("submission_checklist1_A4") && e.getExistingVariable("submission_checklist1_A4").getBoolValue() &&
							e.hasBoolVariable("submission_checklist2_order") && e.getExistingVariable("submission_checklist2_order").getBoolValue() &&
							e.hasBoolVariable("submission_checklist3_clip") && e.getExistingVariable("submission_checklist3_clip").getBoolValue() &&
							e.hasBoolVariable("submission_checklist4_non_pdf") && e.getExistingVariable("submission_checklist4_non_pdf").getBoolValue() &&
							e.hasBoolVariable("submission_checklist5_ut") && e.getExistingVariable("submission_checklist5_ut").getBoolValue() &&
							e.hasBoolVariable("submission_checklist6_same") && e.getExistingVariable("submission_checklist6_same").getBoolValue() &&
							e.hasBoolVariable("submission_checklist7_stick") && e.getExistingVariable("submission_checklist7_stick").getBoolValue();
				}));
		
		nodes.add(new ProcedureNode("米谷先生　サイン", "先生署名", " COMPLETED ")); // 6
		nodes.add(new ProcedureNode("書類提出", "書類提出", " COMPLETED ")); // 7
		
		this.setProcedureNodes(nodes);
	}
	
}
