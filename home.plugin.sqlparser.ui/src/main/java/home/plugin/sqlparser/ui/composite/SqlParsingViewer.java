/*******************************************************************************
 * Copyright 2023-2024 Lenar Shamsutdinov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package home.plugin.sqlparser.ui.composite;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import home.plugin.sqlparser.parser.SqlFileParser;
import home.plugin.sqlparser.parser.model.SqlNode;
import home.plugin.sqlparser.ui.composite.component.CustomButton;
import home.plugin.sqlparser.ui.composite.component.CustomCheckboxTreeViewer;
import home.plugin.sqlparser.ui.composite.component.CustomPanel;
import home.plugin.sqlparser.ui.composite.component.CustomText;
import home.plugin.sqlparser.ui.composite.component.TreeViewerColumnParams;
import home.plugin.sqlparser.ui.composite.component.TreeViewerContentProvider;
import home.plugin.sqlparser.ui.composite.component.dialog.CustonFileDialog;

public final class SqlParsingViewer {

    private static final String TEXT_ROW_TEMPLATE = "[%s] %s";
    private static final String NO_SELECTED_ELEMENTS = " --- NO SELECTED ELEMENTS ---";

    private final Composite parent;

    private Composite buttonPanel;
    private Composite treePanel;
    private Composite textPanel;

    private Button btnOpen;
    private CheckboxTreeViewer treeViewer;
    private Text text;

    private final List<SqlNode> selectedSqlNodes = new LinkedList<>();

    private SqlParsingViewer(Composite parent) {
        this.parent = parent;
    }

    public static SqlParsingViewer create(Composite parent) {
        var sqlParsingViewer = new SqlParsingViewer(parent);
        sqlParsingViewer.builderViewer();
        return sqlParsingViewer;
    }

    private void builderViewer() {
        configParentComposite();
        createPanels();
        createPanelsElemets();
    }

    private void configParentComposite() {
        parent.setLayout(new GridLayout(2, false));
        setBackgroundColor(parent, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
    }

    private void setBackgroundColor(Composite parent, Color color) {
        parent.setBackground(color);
        color.dispose();
    }

    private void createPanels() {
        buttonPanel = CustomPanel.create(parent,
                new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1),
                new GridLayout(2, false),
                new Color(parent.getDisplay(), 250, 155, 100));

        treePanel = CustomPanel.create(parent,
                new GridData(SWT.FILL, SWT.FILL, true, true),
                new GridLayout(),
                Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

        textPanel = CustomPanel.create(parent,
                new GridData(SWT.FILL, SWT.FILL, true, true),
                new GridLayout(),
                Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
    }

    private void createPanelsElemets() {
        new Label(buttonPanel, SWT.LEFT).setText("Open Sql file ->");
        createButton();
        createTree();
        createText();
    }

    private void createButton() {
        btnOpen = CustomButton.create(buttonPanel, "Open", new GridData(80, 30));

        btnOpen.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                var dialog = CustonFileDialog.create(parent.getShell(),
                        new String[] { "SQL", "All files (*)" },
                        new String[] { "*.sql", "*" });

                String path = dialog.open();

                if (path != null) {
                    text.setText("");

                    SqlNode sqlNode = SqlFileParser.parse(Paths.get(path));

                    treeViewer.setInput(sqlNode);
                    treeViewer.expandAll();

                    text.setText(NO_SELECTED_ELEMENTS);
                }
            }
        });
    }

    private void createTree() {
        treeViewer = CustomCheckboxTreeViewer.create(treePanel,
                new GridData(SWT.FILL, SWT.FILL, true, true),
                new TreeViewerContentProvider(),
                new TreeViewerColumnParams("Name", 200, SWT.LEFT,
                        element -> ((SqlNode) element).getName()),
                new TreeViewerColumnParams("Type", 120, SWT.RIGHT,
                        element -> ((SqlNode) element).getType().toString()));

        treeViewer.addCheckStateListener(event -> {
            var selectedSqlNode = (SqlNode) event.getElement();

            if (event.getChecked()) {
                selectedSqlNodes.add(selectedSqlNode);
            } else {
                selectedSqlNodes.remove(selectedSqlNode);
            }

            String result;
            if (!selectedSqlNodes.isEmpty()) {
                result = selectedSqlNodes.stream()
                        .map(n -> TEXT_ROW_TEMPLATE.formatted(n.getType(), n.getName()))
                        .collect(Collectors.joining("\n"));
            } else {
                result = NO_SELECTED_ELEMENTS;
            }

            text.setText(result);
        });
    }

    private void createText() {
        text = CustomText.create(textPanel, NO_SELECTED_ELEMENTS, new GridData(GridData.FILL_BOTH));
    }

    public void setFocusOnButton() {
        btnOpen.setFocus();
    }
}
