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
package home.plugin.sqlparser.ui.composite.component.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public final class CustonFileDialog {

    public static FileDialog create(Shell parent,
            String[] filterNames, String[] filterExtensions) {
        var dialog = new FileDialog(parent, SWT.OPEN);
        dialog.setFilterNames(filterNames);
        dialog.setFilterExtensions(filterExtensions);
        return dialog;
    }

    private CustonFileDialog() {
    }
}
