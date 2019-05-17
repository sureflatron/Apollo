package myapps.servicio_basico.util;/*package com.myapps.servicio_basico.util;

import java.io.IOException;
import javax.faces.component.*;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

@FacesComponent("com.myapps.vista_ccc.util.UIDisablePanel")
public class UIDisablePanel extends UIComponentBase {

	private enum PropertyKeys {
		disabled;
	}

	public UIDisablePanel() {
		setRendererType(null);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		boolean toDisable = isDisabled();
		processDisablePanel(this, toDisable);
		// super.encodeBegin(context);
	}

	public void processDisablePanel(UIComponent root, boolean toDisable) {

		
		 * The key point here is that a child component of <x:disablePanel> may already be disabled, in which case we don't want to enable it if the <x:disablePanel disabled= attribute is set to true.
		 

		for (UIComponent c : root.getChildren()) {
			// System.out.println("c.getClass(): " + c.getClass());
			if (c instanceof CommandButton || c instanceof InputTextarea || c instanceof SelectOneMenu || c instanceof InputText || c instanceof HtmlPanelGrid || c instanceof HtmlOutputText || c instanceof HtmlOutputLabel || c instanceof HtmlOutputText || c instanceof UIInput || c instanceof UICommand || c instanceof UISelectItem || c instanceof Fieldset || c instanceof Column
					|| c instanceof DataTable || c instanceof Tab || c instanceof TabView || c instanceof UISelectItems || c instanceof Panel) {

				if (toDisable) { // <x:disablePanel disabled="true">
					Boolean curState = (Boolean) c.getAttributes().get("disabled");
					if (curState == null || curState == false) {
						c.getAttributes().put("UIPanelDisableFlag", true);
						c.getAttributes().put("disabled", true);
					}
				} else { // <x:disablePanel disabled="false">
					if (c.getAttributes().get("UIPanelDisableFlag") != null) {
						c.getAttributes().remove("UIPanelDisableFlag");
						c.getAttributes().put("disabled", false);
					}
				}

				// System.out.println("INSTANCEOF: " + c.getClass());
				// if (c instanceof org.primefaces.component.selectonemenu.SelectOneMenu) {
				// ((org.primefaces.component.selectonemenu.SelectOneMenu) c).setDisabled(true);
				// } else if (c instanceof org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox) {
				// ((org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox) c).setDisabled(true);
				// } else if (c instanceof org.primefaces.component.inputtext.InputText) {
				// ((org.primefaces.component.inputtext.InputText) c).setReadonly(true);
				// } else if (c instanceof org.primefaces.component.inputtextarea.InputTextarea) {
				// ((org.primefaces.component.inputtextarea.InputTextarea) c).setReadonly(true);
				// } else if (c instanceof org.primefaces.component.commandlink.CommandLink) {
				// ((org.primefaces.component.commandlink.CommandLink) c).setDisabled(true);
				// } else if (c instanceof org.primefaces.component.datatable.DataTable) {
				// ((org.primefaces.component.datatable.DataTable) c).setDisabledSelection(true);
				// ((org.primefaces.component.datatable.DataTable) c).setEditable(false);
				// } else if (c instanceof org.primefaces.component.commandbutton.CommandButton) {
				// ((org.primefaces.component.commandbutton.CommandButton) c).setDisabled(true);
				// } else if (c instanceof org.primefaces.component.fileupload.FileUpload) {
				// ((org.primefaces.component.fileupload.FileUpload) c).setDisabled(true);
				// } else {
				// System.out.println("OTRO: " + c.getClass());
				// }

			} else {
//				System.out.println("OTRO: " + c.getClass());
			}

			if (c.getChildCount() > 0) {
				processDisablePanel(c, toDisable);
			}
		}

	}

	@Override
	public String getFamily() {
		// Got to override it but it doesn't get called.
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isDisabled() {
		return (boolean) getStateHelper().eval(PropertyKeys.disabled, false);
	}

	public void setDisabled(boolean disabled) {
		getStateHelper().put(PropertyKeys.disabled, disabled);
	}
}*/