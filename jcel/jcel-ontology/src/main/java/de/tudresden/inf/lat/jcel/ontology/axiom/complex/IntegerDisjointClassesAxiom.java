/*
 *
 * Copyright 2009-2013 Julian Mendez
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

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * This class models an axiom stating that the contained classes are pairwise
 * disjoint. <br />
 * This is: A<sub>1</sub> &sqcap; A<sub>2</sub> &sqsube; &perp;, A<sub>1</sub>
 * &sqcap; A<sub>3</sub> &sqsube; &perp;, A<sub>2</sub> &sqcap; A<sub>3</sub>
 * &sqsube; &perp;, &hellip; , A<sub>n-1</sub> &sqcap; A<sub>n</sub> &sqsube;
 * &perp;
 * 
 * @author Julian Mendez
 */
public class IntegerDisjointClassesAxiom implements ComplexIntegerAxiom {

	private final Set<Integer> classesInSignature;
	private final Set<IntegerClassExpression> classExpressions;
	private final int hashCode;
	private final Set<Integer> objectPropertiesInSignature;

	/**
	 * Constructs a new disjoint classes axiom.
	 * 
	 * @param descSet
	 *            set of classes declared to be disjoint
	 */
	protected IntegerDisjointClassesAxiom(Set<IntegerClassExpression> descSet) {
		if (descSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classExpressions = Collections.unmodifiableSet(descSet);
		this.hashCode = descSet.hashCode();

		Set<Integer> classesInSignature = new HashSet<Integer>();
		for (IntegerClassExpression expression : this.classExpressions) {
			classesInSignature.addAll(expression.getClassesInSignature());
		}
		this.classesInSignature = Collections
				.unmodifiableSet(classesInSignature);

		Set<Integer> objectPropertiesInSignature = new HashSet<Integer>();
		for (IntegerClassExpression expression : this.classExpressions) {
			objectPropertiesInSignature.addAll(expression
					.getObjectPropertiesInSignature());
		}
		this.objectPropertiesInSignature = Collections
				.unmodifiableSet(objectPropertiesInSignature);
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerDisjointClassesAxiom)) {
			IntegerDisjointClassesAxiom other = (IntegerDisjointClassesAxiom) o;
			ret = getClassExpressions().equals(other.getClassExpressions());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.unmodifiableSet(this.classesInSignature);
	}

	/**
	 * Returns the set of disjoint classes in this axiom.
	 * 
	 * @return the set of disjoint classes in this axiom
	 */
	public Set<IntegerClassExpression> getClassExpressions() {
		return Collections.unmodifiableSet(this.classExpressions);
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
		return Collections.unmodifiableSet(this.objectPropertiesInSignature);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.DisjointClasses);
		sbuf.append(ComplexIntegerAxiomConstant.openPar);
		Set<IntegerClassExpression> classExpressionSet = getClassExpressions();
		for (IntegerClassExpression classExpression : classExpressionSet) {
			sbuf.append(classExpression.toString());
			sbuf.append(ComplexIntegerAxiomConstant.sp);
		}
		sbuf.append(ComplexIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}
