/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2020 Apromore Pty Ltd.
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
package org.apromore.processmining.models.graphbased;

import org.apromore.processmining.models.graphbased.directed.AbstractDirectedGraphEdge;

public abstract class AbstractGraphEdge<S, T> extends AbstractGraphElement implements
		Comparable<AbstractGraphEdge<S, T>> {

	protected final int hash;
	protected final S source;
	protected final T target;

	public AbstractGraphEdge(S source, T target) {
		super();
		this.source = source;
		this.target = target;
		this.hash = source.hashCode() + 37 * target.hashCode();
	}

	@Override
    public int hashCode() {
		// Hashcode based on source and target, which
		// respects contract that this.equals(o) implies
		// this.hashCode()==o.hashCode()
		return hash;
	}

	@Override
    public boolean equals(Object o) {
		if (!(this.getClass().equals(o.getClass()))) {
			return false;
		}
		AbstractDirectedGraphEdge<?, ?> edge = (AbstractDirectedGraphEdge<?, ?>) o;

		return edge.source.equals(source) && edge.target.equals(target);

	}

	public S getSource() {
		return source;
	}

	public T getTarget() {
		return target;
	}
	
	@Override
    public String toString() {
	    return source.toString() + "->" + target.toString();
	}

}
