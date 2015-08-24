/*
 *
 * Copyright (C) 2009-2015 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
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
 *
 */

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Axiom of the form:
 * <ul>
 * <li>r \u2291 s</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class RI2Axiom implements NormalizedIntegerAxiom {

	private final int subProperty;
	private final int superProperty;
	private final Set<Annotation> annotations;
	private final int hashCode;

	/**
	 * Constructs a new axiom RI-2.
	 * 
	 * @param leftPropertyId
	 *            object property identifier for the left-hand part of the axiom
	 * @param rightPropertyId
	 *            object property identifier for the right-hand part of the
	 *            axiom
	 * @param annotations
	 *            annotations
	 */
	RI2Axiom(int leftPropertyId, int rightPropertyId,
			Set<Annotation> annotations) {
		if (annotations == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.subProperty = leftPropertyId;
		this.superProperty = rightPropertyId;
		this.annotations = annotations;
		this.hashCode = this.subProperty + 0x1F
				* (this.superProperty + 0x1F * this.annotations.hashCode());
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = (this == obj);
		if (!ret && (obj instanceof RI2Axiom)) {
			RI2Axiom other = (RI2Axiom) obj;
			ret = (this.subProperty == other.subProperty)
					&& (this.superProperty == other.superProperty)
					&& this.annotations.equals(other.annotations);
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

	/**
	 * Returns the object property on the left-hand part of the axiom
	 * 
	 * @return the object property on the left-hand part of the axiom
	 */
	public int getSubProperty() {
		return this.subProperty;
	}

	/**
	 * Returns the object property on the right-hand part of the axiom
	 * 
	 * @return the object property on the right-hand part of the axiom
	 */
	public int getSuperProperty() {
		return this.superProperty;
	}

	@Override
	public Set<Annotation> getAnnotations() {
		return Collections.unmodifiableSet(this.annotations);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
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