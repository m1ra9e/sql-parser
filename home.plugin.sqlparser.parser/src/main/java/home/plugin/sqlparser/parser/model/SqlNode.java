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
package home.plugin.sqlparser.parser.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class SqlNode {

    public static final String ROOT_NODE_NAME = "root_select";

    private final List<SqlNode> children = new ArrayList<>();

    private final SqlNode parent;
    private final String name;
    private final SqlNodeType type;

    private SqlNode(SqlNode parent, String name, SqlNodeType type) {
        this.parent = parent;
        this.name = name;
        this.type = type;
    }

    public static SqlNode createRootNode() {
        return new SqlNode(null, SqlNode.ROOT_NODE_NAME, SqlNodeType.ROOT);
    }

    /**
     * Create SqlNode with checking its parameters and adding created SqlNode to the
     * parent SqlNode, if it exists
     *
     * @param parent - parent node for new SqlNode object
     * @param name   - name for new SqlNode object
     * @param type   - type of new SqlNode object
     *
     * @return SqlNode object
     */
    public static SqlNode create(SqlNode parent, String name, SqlNodeType type) {
        checkParam(parent, name);

        var node = new SqlNode(parent, name, type);

        if (parent != null) {
            parent.addChild(node);
        }

        return node;
    }

    private static void checkParam(SqlNode parent, String name) {
        if (ROOT_NODE_NAME.equals(name)) {
            return;
        }

        if (parent == null) {
            throw new IllegalArgumentException("parent is null for " + name);
        }

        if (parent.getType() == SqlNodeType.COLUMN) {
            throw new IllegalArgumentException("%s [%s] can't be parent node"
                    .formatted(parent.getName(), parent.getType()));
        }
    }

    public SqlNode getParent() {
        return parent;
    }

    public List<SqlNode> getChildren() {
        return children;
    }

    public void addChild(SqlNode child) {
        children.add(child);
    }

    public String getName() {
        return name;
    }

    public SqlNodeType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(children,
                name,
                // "parent" commented because of stack overflow in UI
                // parent,
                type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SqlNode)) {
            return false;
        }
        var other = (SqlNode) obj;
        return Objects.equals(children, other.children)
                && Objects.equals(name, other.name)
                // "parent" commented because of stack overflow in UI
                // && Objects.equals(parent, other.parent)
                && type == other.type;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        print(sb, "", "");
        return sb.toString();
    }

    private void print(StringBuilder sb, String prefix, String childrenPrefix) {
        sb.append(prefix).append(name).append('\n');

        Iterator<SqlNode> childrenIterator = children.iterator();
        while (childrenIterator.hasNext()) {
            SqlNode child = childrenIterator.next();
            if (childrenIterator.hasNext()) {
                child.print(sb, childrenPrefix + "|- ", childrenPrefix + "|   ");
            } else {
                child.print(sb, childrenPrefix + "+- ", childrenPrefix + "    ");
            }
        }
    }
}
