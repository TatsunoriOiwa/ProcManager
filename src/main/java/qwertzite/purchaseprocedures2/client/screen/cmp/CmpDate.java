package qwertzite.purchaseprocedures2.client.screen.cmp;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import qwertzite.purchaseprocedures2.util.TimeDateUtil;

public class CmpDate extends Composite {
	private Consumer<LocalDate> listener = e -> {};
	
	private Text txtYear;
	private Text txtMonth;
	private Text txtDay;
	private Label dayOfWeek;
	private Label[] separators = new Label[2];
	
	@SuppressWarnings("unused")
	private boolean enabled;
	
	private LocalDate currentDate;
	private int year;
	private int month;
	private int day;
	private boolean hasYear = false;
	private boolean hasMonth = false;
	private boolean hasDay = false;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CmpDate(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		txtYear = new Text(this, SWT.BORDER | style);
		GridData gd_txtYeat = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtYeat.widthHint = 40;
		txtYear.setLayoutData(gd_txtYeat);
		txtYear.setTextLimit(4);
		txtYear.addModifyListener(this::onYearChanged);
		txtYear.addFocusListener(FocusListener.focusGainedAdapter(e -> this.onTextFieldSelected(e, txtYear)));
		txtYear.addTraverseListener(e -> this.onTraverseField(e, null, txtYear, txtMonth));
		
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("/");
		separators[0] = label;
		
		txtMonth = new Text(this, SWT.BORDER | style);
		GridData gd_txtMonth = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtMonth.widthHint = 20;
		txtMonth.setLayoutData(gd_txtMonth);
		txtMonth.setTextLimit(2);
		txtMonth.addModifyListener(this::onMonthChanged);
		txtMonth.addFocusListener(FocusListener.focusGainedAdapter(e -> this.onTextFieldSelected(e, txtMonth)));
		txtMonth.addTraverseListener(e -> this.onTraverseField(e, txtYear, txtMonth, txtDay));
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("/");
		separators[1] = label;
		
		txtDay = new Text(this, SWT.BORDER | style);
		GridData gd_txtDay = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtDay.widthHint = 20;
		txtDay.setLayoutData(gd_txtDay);
		txtDay.setTextLimit(2);
		txtDay.addModifyListener(this::onDayChanged);
		txtDay.addFocusListener(FocusListener.focusGainedAdapter(e -> this.onTextFieldSelected(e, txtDay)));
		txtDay.addTraverseListener(e -> this.onTraverseField(e, txtMonth, txtDay, null));
		
		dayOfWeek = new Label(this, SWT.NONE);
		dayOfWeek.setText("(曜)");
	}
	
	public void setDateListener(Consumer<LocalDate> listener) {
		this.listener = listener;
	}
	
	private void onTextFieldSelected(FocusEvent e, Text text) {
		text.selectAll();
	}
	
	private void onTraverseField(TraverseEvent event, Text prev, Text self, Text next) {
		switch(event.detail) {
		case SWT.TRAVERSE_RETURN:
			break;
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			if (prev != null && self.getCaretPosition() == 0) { prev.setFocus(); }
			break;
		case SWT.TRAVERSE_ARROW_NEXT:
			if (next != null && self.getCaretPosition() == self.getText().length() && self.getSelectionCount() == 0) {
				next.setFocus();
			}
		}
	}
	
	private void onYearChanged(ModifyEvent event) {
		try {
			this.year = Integer.valueOf(this.txtYear.getText());
			this.txtYear.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			this.hasYear = true;
		} catch (NumberFormatException e) {
			this.txtYear.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			this.hasYear = false;
		}
		this.updateDayDisp();
	}
	
	private void onMonthChanged(ModifyEvent event) {
		try {
			this.month = Integer.valueOf(this.txtMonth.getText());
			if (this.month > 0 && this.month <= 12) {
				this.txtMonth.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				this.hasMonth = true;
			} else {
				this.txtMonth.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				this.hasMonth = false;
			}
		} catch (NumberFormatException e) {
			this.txtMonth.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			this.hasMonth = false;
		}
		this.updateDayDisp();
	}
	
	private void onDayChanged(ModifyEvent event) {
		try {
			this.day = Integer.valueOf(this.txtDay.getText());
			this.txtDay.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			this.hasDay = true;
		} catch (NumberFormatException e) {
			this.txtDay.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			this.hasDay = false;
		}
		this.updateDayDisp();
	}
	
	private void updateDayDisp() {
		if (this.hasYear && this.hasMonth && this.hasDay) {
			LocalDate date = LocalDate.MIN;
			date = date.withYear(this.year).withMonth(this.month);
			try {
				date = date.withDayOfMonth(this.day);
				this.txtDay.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			} catch (DateTimeException e) {
				this.txtDay.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				return;
			}
			this.setCurrentDateAndNotice(date);
		} else if (this.txtYear.getText().isEmpty() && this.txtMonth.getText().isEmpty() && this.txtDay.getText().isEmpty()) {
			this.setCurrentDateAndNotice(null);
		}
	}
	
	private void setCurrentDateAndNotice(LocalDate date) {// 全て空の場合　or ちゃんとした値が設定された場合
		if ((this.currentDate == null && date != null) || (this.currentDate != null && !this.currentDate.equals(date))) {
			this.dayOfWeek.setText(date == null ? "(　)" : ("(" + TimeDateUtil.jpWeek(date.getDayOfWeek()) + ")"));
			this.listener.accept(date);
			this.currentDate = date;
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.txtYear.setEnabled(enabled);
		this.txtMonth.setEnabled(enabled);
		this.txtDay.setEnabled(enabled);
		this.dayOfWeek.setForeground(SWTResourceManager.getColor(enabled ? SWT.COLOR_BLACK : SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
		this.separators[0].setForeground(SWTResourceManager.getColor(enabled ? SWT.COLOR_BLACK : SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
		this.separators[1].setForeground(SWTResourceManager.getColor(enabled ? SWT.COLOR_BLACK : SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
		this.enabled = enabled;
	}
	
	public void setDate(LocalDate date) {
		if (date != null) {
			this.currentDate = date;
			String str = String.valueOf(this.year = date.getYear());
			if (!this.txtYear.getText().equals(str)) this.txtYear.setText(str);
			str = String.valueOf(this.month = date.getMonthValue());
			if (!this.txtMonth.getText().equals(str)) this.txtMonth.setText(str);
			str = String.valueOf(this.day = date.getDayOfMonth());
			if (!this.txtDay.getText().equals(str)) this.txtDay.setText(str);
			this.dayOfWeek.setText("(" + TimeDateUtil.jpWeek(date.getDayOfWeek()) + ")");
			this.hasYear = this.hasMonth = this.hasDay = true;
		} else {
			this.txtYear.setText("");
			this.txtMonth.setText("");
			this.txtDay.setText("");
			this.dayOfWeek.setText("(　)");
			this.hasYear = this.hasMonth = this.hasDay = false;
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
