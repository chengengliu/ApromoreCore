/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2020 The University of Melbourne.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.apromore.processmining.models.graphbased.directed;

import org.apromore.processmining.models.graphbased.AbstractGraphNode;

public abstract class AbstractDirectedGraphNode extends AbstractGraphNode implements DirectedGraphNode {

	public AbstractDirectedGraphNode() {
		super();
	}

	public abstract AbstractDirectedGraph<?, ?> getGraph();

	public int compareTo(DirectedGraphNode node) {
		int comp = getId().compareTo(node.getId());
		//		assert (Math.abs(comp) == Math.abs(getLabel().compareTo(getLabel())));
		return comp;

	}

}
