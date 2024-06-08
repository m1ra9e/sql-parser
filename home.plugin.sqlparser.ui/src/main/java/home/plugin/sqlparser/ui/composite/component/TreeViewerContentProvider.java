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

import org.eclipse.jface.viewers.ITreeContentProvider;

import home.plugin.sqlparser.parser.model.SqlNode;
import home.plugin.sqlparser.parser.model.SqlNodeType;

public final class TreeViewerContentProvider implements ITreeContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        SqlNode sqlNode = (SqlNode) parentElement;
        return sqlNode.getChildren().toArray(new Object[0]);
    }

    @Override
    public Object getParent(Object element) {
        SqlNode sqlNode = (SqlNode) element;
        return sqlNode.getParent();
    }

    @Override
    public boolean hasChildren(Object element) {
        SqlNode sqlNode = (SqlNode) element;
        return sqlNode.getType() != SqlNodeType.COLUMN;
    }
}
