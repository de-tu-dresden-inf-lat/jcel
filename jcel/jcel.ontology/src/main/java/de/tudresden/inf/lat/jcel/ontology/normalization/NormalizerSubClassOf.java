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

package de.tudresden.inf.lat.jcel.ontology.normalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiomFactory;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.ontology.axiom.extension.IntegerOntologyObjectFactory;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * 
 * @author Julian Mendez
 */
class NormalizerSubClassOf implements NormalizationRule {

	private final IntegerOntologyObjectFactory ontologyObjectFactory;

	public NormalizerSubClassOf(IntegerOntologyObjectFactory factory) {
		this.ontologyObjectFactory = factory;
	}

	@Override
	public Set<IntegerAxiom> apply(IntegerAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = Collections.emptySet();
		if (axiom instanceof IntegerSubClassOfAxiom) {
			ret = new HashSet<IntegerAxiom>();
			Collection<NormalizedIntegerAxiom> normalizedAxioms = simplify((IntegerSubClassOfAxiom) axiom);
			for (NormalizedIntegerAxiom normalizedAxiom : normalizedAxioms) {
				ret.add(normalizedAxiom);
			}
		}
		return ret;
	}

	private IntegerEntityManager getIdGenerator() {
		return getOntologyObjectFactory().getEntityManager();
	}

	private NormalizedIntegerAxiomFactory getNormalizedAxiomFactory() {
		return getOntologyObjectFactory().getNormalizedAxiomFactory();
	}

	private Integer getObjectPropertyId(IntegerObjectPropertyExpression propExpr) {
		return propExpr.accept(new ObjectPropertyIdFinder(getIdGenerator()));
	}

	public IntegerOntologyObjectFactory getOntologyObjectFactory() {
		return this.ontologyObjectFactory;
	}

	private Collection<NormalizedIntegerAxiom> simplify(
			IntegerSubClassOfAxiom axiom) {
		Collection<NormalizedIntegerAxiom> ret = new ArrayList<NormalizedIntegerAxiom>();
		IntegerClassExpression subClass = axiom.getSubClass();
		IntegerClassExpression superClass = axiom.getSuperClass();

		if (subClass.isLiteral() && superClass.isLiteral()) {
			ret.add(getNormalizedAxiomFactory().createGCI0Axiom(
					((IntegerClass) subClass).getId(),
					((IntegerClass) superClass).getId()));

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectIntersectionOf)
				&& subClass.hasOnlyClasses()) {

			IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) subClass;
			Set<IntegerClassExpression> operands = intersection.getOperands();
			if (operands.size() == 0) {
				ret.add(getNormalizedAxiomFactory().createGCI0Axiom(
						IntegerEntityManager.topClassId,
						((IntegerClass) superClass).getId()));

			} else if (operands.size() == 1) {
				ret.add(getNormalizedAxiomFactory().createGCI0Axiom(
						((IntegerClass) operands.iterator().next()).getId(),
						((IntegerClass) superClass).getId()));

			} else if (operands.size() == 2) {
				Iterator<IntegerClassExpression> it = operands.iterator();
				int leftSubClassId = ((IntegerClass) it.next()).getId();
				int rightSubClassId = ((IntegerClass) it.next()).getId();
				int superClassId = ((IntegerClass) superClass).getId();
				ret.add(getNormalizedAxiomFactory().createGCI1Axiom(
						leftSubClassId, rightSubClassId, superClassId));

			}

		} else if (subClass.isLiteral() && !superClass.isLiteral()
				&& (superClass instanceof IntegerObjectSomeValuesFrom)
				&& superClass.hasOnlyClasses()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) superClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			Integer property = getObjectPropertyId(restriction.getProperty());
			ret.add(getNormalizedAxiomFactory()
					.createGCI2Axiom(((IntegerClass) subClass).getId(),
							property, filler.getId()));

		} else if (!subClass.isLiteral() && superClass.isLiteral()
				&& (subClass instanceof IntegerObjectSomeValuesFrom)
				&& subClass.hasOnlyClasses()) {

			IntegerObjectSomeValuesFrom restriction = (IntegerObjectSomeValuesFrom) subClass;
			IntegerClass filler = (IntegerClass) restriction.getFiller();
			Integer property = getObjectPropertyId(restriction.getProperty());
			ret.add(getNormalizedAxiomFactory().createGCI3Axiom(property,
					filler.getId(), ((IntegerClass) superClass).getId()));

		}
		return ret;
	}

}