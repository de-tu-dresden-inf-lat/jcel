/*
 * Copyright 2009 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 * jcel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.core.axiom.normalized;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Axiom of the form:
 * <ul>
 * <li>r &#8849; s</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class RI2Axiom implements NormalizedIntegerAxiom {

	private Integer subProperty = null;
	private Integer superProperty = null;

	public RI2Axiom(Integer leftProp, Integer rightProp) {
		if (leftProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.subProperty = leftProp;
		this.superProperty = rightProp;
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof RI2Axiom) {
			RI2Axiom other = (RI2Axiom) o;
			ret = getSubProperty().equals(other.getSubProperty())
					&& getSuperProperty().equals(other.getSuperProperty());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(this.subProperty);
		ret.add(this.superProperty);
		return Collections.unmodifiableSet(ret);
	}

	public Integer getSubProperty() {
		return this.subProperty;
	}

	public Integer getSuperProperty() {
		return this.superProperty;
	}

	@Override
	public int hashCode() {
		return getSubProperty().hashCode() + 31 * getSuperProperty().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.RI2);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getSubProperty());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getSuperProperty());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}
}
