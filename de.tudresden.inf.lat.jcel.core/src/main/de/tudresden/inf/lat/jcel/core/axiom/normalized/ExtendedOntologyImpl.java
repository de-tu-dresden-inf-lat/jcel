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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class models an ontology with the property to look up by axiom type.
 * 
 * @author Julian Mendez
 */
public class ExtendedOntologyImpl implements ExtendedOntology,
		NormalizedIntegerAxiomVisitor<Boolean> {

	private Set<Integer> classSet = null;
	private Map<Integer, Set<GCI0Axiom>> mapOfGCI0 = null;
	private Map<Integer, Set<GCI1Axiom>> mapOfGCI1 = null;
	private Map<Integer, Set<GCI2Axiom>> mapOfGCI2 = null;
	private Map<Integer, Map<Integer, Set<GCI3Axiom>>> mapOfGCI3AB = null;
	private Map<Integer, Map<Integer, Set<GCI3Axiom>>> mapOfGCI3rA = null;
	private Map<Integer, Set<NominalAxiom>> mapOfNominalAxiom = null;
	private Map<Integer, Set<RangeAxiom>> mapOfRangeAxiom = null;
	private Map<Integer, Set<RI2Axiom>> mapOfRI2 = null;
	private Map<Integer, Set<RI3Axiom>> mapOfRI3ByLeft = null;
	private Map<Integer, Set<RI3Axiom>> mapOfRI3ByRight = null;
	private Set<Integer> objectPropertySet = null;
	private Set<Integer> setOfFunctionalObjectProperties = null;
	private Set<Integer> setOfReflexiveObjectProperties = null;
	private Set<Integer> setOfTransitiveObjectProperties = null;

	public ExtendedOntologyImpl() {
		clear();
	}

	@Override
	public void addClass(Integer classId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.classSet.add(classId);
	}

	private void addEntities(NormalizedIntegerAxiom axiom) {
		this.objectPropertySet.addAll(axiom.getObjectPropertiesInSignature());
		this.classSet.addAll(axiom.getClassesInSignature());
	}

	private void addGCI0Axiom(Integer classId, GCI0Axiom axiom) {
		if (this.mapOfGCI0.get(classId) == null) {
			this.mapOfGCI0.put(classId, new HashSet<GCI0Axiom>());
		}
		this.mapOfGCI0.get(classId).add(axiom);
	}

	private void addGCI1Axiom(Integer classId, GCI1Axiom axiom) {
		if (this.mapOfGCI1.get(classId) == null) {
			this.mapOfGCI1.put(classId, new HashSet<GCI1Axiom>());
		}
		this.mapOfGCI1.get(classId).add(axiom);
	}

	private void addGCI2Axiom(Integer classId, GCI2Axiom axiom) {
		if (this.mapOfGCI2.get(classId) == null) {
			this.mapOfGCI2.put(classId, new HashSet<GCI2Axiom>());
		}
		this.mapOfGCI2.get(classId).add(axiom);
	}

	private void addGCI3Axiom(
			Map<Integer, Map<Integer, Set<GCI3Axiom>>> internalMap,
			GCI3Axiom axiom, Integer firstKey, Integer secondKey) {
		Map<Integer, Set<GCI3Axiom>> map = internalMap.get(firstKey);
		if (map == null) {
			map = new HashMap<Integer, Set<GCI3Axiom>>();
			internalMap.put(firstKey, map);
		}
		Set<GCI3Axiom> set = map.get(secondKey);
		if (set == null) {
			set = new HashSet<GCI3Axiom>();
			map.put(secondKey, set);
		}
		set.add(axiom);
	}

	private void addNominalAxiom(Integer individualId, NominalAxiom axiom) {
		if (this.mapOfNominalAxiom.get(individualId) == null) {
			this.mapOfNominalAxiom.put(individualId,
					new HashSet<NominalAxiom>());
		}
		this.mapOfNominalAxiom.get(individualId).add(axiom);
	}

	@Override
	public void addObjectProperty(Integer objectProperty) {
		if (objectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.objectPropertySet.add(objectProperty);
	}

	private void addRangeAxiom(Integer propertyId, RangeAxiom axiom) {
		if (this.mapOfRangeAxiom.get(propertyId) == null) {
			this.mapOfRangeAxiom.put(propertyId, new HashSet<RangeAxiom>());
		}
		this.mapOfRangeAxiom.get(propertyId).add(axiom);
	}

	private void addRI2Axiom(Integer classId, RI2Axiom axiom) {
		if (this.mapOfRI2.get(classId) == null) {
			this.mapOfRI2.put(classId, new HashSet<RI2Axiom>());
		}
		this.mapOfRI2.get(classId).add(axiom);
	}

	private void addTo(Integer property, RI3Axiom axiom,
			Map<Integer, Set<RI3Axiom>> map) {
		Set<RI3Axiom> axiomSet = map.get(property);
		if (axiomSet == null) {
			axiomSet = new HashSet<RI3Axiom>();
			map.put(property, axiomSet);
		}
		axiomSet.add(axiom);
	}

	@Override
	public void clear() {
		this.classSet = new HashSet<Integer>();
		this.objectPropertySet = new HashSet<Integer>();
		this.mapOfGCI0 = new HashMap<Integer, Set<GCI0Axiom>>();
		this.mapOfGCI1 = new HashMap<Integer, Set<GCI1Axiom>>();
		this.mapOfGCI2 = new HashMap<Integer, Set<GCI2Axiom>>();
		this.mapOfGCI3rA = new HashMap<Integer, Map<Integer, Set<GCI3Axiom>>>();
		this.mapOfGCI3AB = new HashMap<Integer, Map<Integer, Set<GCI3Axiom>>>();
		this.mapOfRI2 = new HashMap<Integer, Set<RI2Axiom>>();
		this.mapOfRI3ByLeft = new HashMap<Integer, Set<RI3Axiom>>();
		this.mapOfRI3ByRight = new HashMap<Integer, Set<RI3Axiom>>();
		this.mapOfNominalAxiom = new HashMap<Integer, Set<NominalAxiom>>();
		this.mapOfRangeAxiom = new HashMap<Integer, Set<RangeAxiom>>();
		this.setOfTransitiveObjectProperties = new HashSet<Integer>();
		this.setOfFunctionalObjectProperties = new HashSet<Integer>();
		this.setOfReflexiveObjectProperties = new HashSet<Integer>();
	}

	@Override
	public Set<Integer> getClassSet() {
		return Collections.unmodifiableSet(this.classSet);
	}

	@Override
	public Set<Integer> getFunctionalObjectProperties() {
		return Collections
				.unmodifiableSet(this.setOfFunctionalObjectProperties);
	}

	@Override
	public Set<GCI0Axiom> getGCI0Axioms(Integer classId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<GCI0Axiom> ret = this.mapOfGCI0.get(classId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI1Axiom> getGCI1Axioms(Integer classId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<GCI1Axiom> ret = this.mapOfGCI1.get(classId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI2Axiom> getGCI2Axioms(Integer classId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<GCI2Axiom> ret = this.mapOfGCI2.get(classId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI3Axiom> getGCI3ABAxioms(Integer leftClassId,
			Integer rightClassId) {
		if (leftClassId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightClassId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<GCI3Axiom> ret = Collections.emptySet();
		Map<Integer, Set<GCI3Axiom>> map = this.mapOfGCI3AB.get(leftClassId);
		if (map != null) {
			ret = map.get(rightClassId);
			if (ret == null) {
				ret = Collections.emptySet();
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI3Axiom> getGCI3rAAxioms(Integer propertyId,
			Integer leftClassId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (leftClassId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<GCI3Axiom> ret = Collections.emptySet();
		Map<Integer, Set<GCI3Axiom>> map = this.mapOfGCI3rA.get(propertyId);
		if (map != null) {
			ret = map.get(leftClassId);
			if (ret == null) {
				ret = Collections.emptySet();
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<Integer> getObjectPropertySet() {
		return Collections.unmodifiableSet(this.objectPropertySet);
	}

	@Override
	public Set<Integer> getReflexiveObjectProperties() {
		return Collections.unmodifiableSet(this.setOfReflexiveObjectProperties);
	}

	@Override
	public Set<RI2Axiom> getRI2Axioms(Integer elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<RI2Axiom> ret = this.mapOfRI2.get(elem);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<RI3Axiom> getRI3AxiomsByLeft(Integer elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<RI3Axiom> ret = this.mapOfRI3ByLeft.get(elem);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<RI3Axiom> getRI3AxiomsByRight(Integer elem) {
		if (elem == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<RI3Axiom> ret = this.mapOfRI3ByRight.get(elem);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<Integer> getTransitiveObjectProperties() {
		return Collections
				.unmodifiableSet(this.setOfTransitiveObjectProperties);
	}

	@Override
	public void load(Set<NormalizedIntegerAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		clear();
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			axiom.accept(this);
			addEntities(axiom);
		}
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[");
		sbuf.append("map of GCI0 =" + this.mapOfGCI0.toString());
		sbuf.append("map of GCI1 =" + this.mapOfGCI1.toString());
		sbuf.append("map of GCI2 =" + this.mapOfGCI2.toString());
		sbuf.append("map of GCI3 =" + this.mapOfGCI3rA.toString());
		sbuf.append("]");
		return sbuf.toString();
	}

	@Override
	public Boolean visit(FunctionalObjectPropertyAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.setOfFunctionalObjectProperties.add(axiom.getProperty());
		return true;
	}

	/**
	 * Intersection extension.
	 */
	@Override
	public Boolean visit(GCI0Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addGCI0Axiom(axiom.getSubClass(), axiom);
		return true;
	}

	/**
	 * Intersection extension.
	 */
	@Override
	public Boolean visit(GCI1Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<Integer> operandSet = axiom.getOperands();
		for (Integer currentOperand : operandSet) {
			Set<Integer> currentSet = new HashSet<Integer>();
			currentSet.addAll(operandSet);
			currentSet.remove(currentOperand);
			addGCI1Axiom(currentOperand, axiom);
		}
		return true;
	}

	/**
	 * Existential extension
	 */
	@Override
	public Boolean visit(GCI2Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addGCI2Axiom(axiom.getSubClass(), axiom);
		return true;
	}

	/**
	 * Symmetric existential extension.
	 */
	@Override
	public Boolean visit(GCI3Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addGCI3Axiom(this.mapOfGCI3rA, axiom, axiom.getPropertyInSubClass(),
				axiom.getClassInSubClass());
		addGCI3Axiom(this.mapOfGCI3AB, axiom, axiom.getClassInSubClass(), axiom
				.getSuperClass());
		return true;
	}

	@Override
	public Boolean visit(NominalAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addNominalAxiom(axiom.getIndividual(), axiom);
		return true;
	}

	@Override
	public Boolean visit(RangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addRangeAxiom(axiom.getProperty(), axiom);
		return true;
	}

	@Override
	public Boolean visit(RI1Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.setOfReflexiveObjectProperties.add(axiom.getSuperProperty());
		return true;
	}

	@Override
	public Boolean visit(RI2Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addRI2Axiom(axiom.getSubProperty(), axiom);
		return true;
	}

	@Override
	public Boolean visit(RI3Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer left = axiom.getLeftSubProperty();
		Integer right = axiom.getRightSubProperty();
		addTo(left, axiom, this.mapOfRI3ByLeft);
		addTo(right, axiom, this.mapOfRI3ByRight);
		if (axiom.getSuperProperty().equals(left)
				&& axiom.getSuperProperty().equals(right)) {
			this.setOfTransitiveObjectProperties.add(left);
		}
		return true;
	}

}
