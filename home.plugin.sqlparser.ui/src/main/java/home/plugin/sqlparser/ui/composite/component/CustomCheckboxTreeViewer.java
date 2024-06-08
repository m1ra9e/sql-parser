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
package home.plugin.sqlparser.ui.composite.component;

import java.util.Arrays;
import java.util.function.Function;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public final class CustomCheckboxTreeViewer {

    public static CheckboxTreeViewer create(Composite parent, GridData gridData,
            IContentProvider contentProvider, TreeViewerColumnParams... colsParams) {
        var treeViewer = new CheckboxTreeViewer(parent,
                SWT.FILL | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);

        Tree tree = treeViewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);

        Arrays.stream(colsParams).forEach(colParams -> addColumn(treeViewer,
                colParams.name(), colParams.widht(), colParams.style(),
                colParams.elementTextGetter()));

        treeViewer.setContentProvider(contentProvider);

        treeViewer.getControl().setLayoutData(gridData);

        return treeViewer;
    }

    private static void addColumn(CheckboxTreeViewer treeViewer, String name,
            int widht, int style, Function<Object, String> elementTextGetter) {
        var treeViewerCol = new TreeViewerColumn(treeViewer, style);

        TreeColumn treeCol = treeViewerCol.getColumn();
        treeCol.setText(name);
        treeCol.setWidth(widht);
        treeCol.setAlignment(SWT.LEFT);

        treeViewerCol.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                return elementTextGetter.apply(element);
            }
        });
    }

    private CustomCheckboxTreeViewer() {
    }
}
