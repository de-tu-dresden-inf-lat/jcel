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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerEquivalentClassesAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;

/**
 * 
 * <ul>
 * <li>NR-1.5 : C &equiv; D \u219D C \u2291 D, D \u2291 C</li>
 * </ul>
 * <br>
 * 
 * @author Julian Mendez
 */
class NormalizerNR1_5 implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	/**
	 * Constructs a new normalizer of rule NR-1.5.
	 * 
	 * @param factory
	 *            factory
	 */
	public NormalizerNR1_5(IntegerOntologyObjectFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.ontologyObjectFactory = factory;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerEquivalentClassesAxiom) {
			ret = applyRule((IntegerEquivalentClassesAxiom) axiom);
		}
		return ret;
	}

	private Set<IntegerAxiom> applyRule(
			IntegerEquivalentClassesAxiom equivalentAxiom) {
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		Set<IntegerClassExpression> classExpressionSet = equivalentAxiom
				.getClassExpressions();
		for (IntegerClassExpression firstClassExpression : classExpressionSet) {
			for (IntegerClassExpression secondClassExpression : classExpressionSet) {
				if (!firstClassExpression.equals(secondClassExpression)) {
					IntegerSubClassOfAxiom subClassAxiom = getOntologyObjectFactory()
							.getComplexAxiomFactory().createSubClassOfAxiom(
									firstClassExpression,
									secondClassExpression,
									equivalentAxiom.getAnnotations());
					ret.add(subClassAxiom);
				}
			}
		}
		return ret;
	}

	private IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

}