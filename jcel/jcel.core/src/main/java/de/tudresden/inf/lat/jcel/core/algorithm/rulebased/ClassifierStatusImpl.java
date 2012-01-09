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

package de.tudresden.inf.lat.jcel.core.algorithm.rulebased;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntry;
import de.tudresden.inf.lat.jcel.core.graph.IntegerRelationMapImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerBidirectionalGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.IntegerSubsumerGraphImpl;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.ExtendedOntology;
import de.tudresden.inf.lat.jcel.ontology.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityManager;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerEntityType;

/**
 * An object of this class keeps the status of the classifier.
 * 
 * @author Julian Mendez
 */
public class ClassifierStatusImpl implements ClassifierStatus {

	private static final Integer bottomClassId = IntegerEntityManager.bottomClassId;
	private static final Integer bottomObjectPropertyId = IntegerEntityManager.bottomObjectPropertyId;
	private static final Integer topClassId = IntegerEntityManager.topClassId;
	private static final Integer topObjectPropertyId = IntegerEntityManager.topObjectPropertyId;

	private IntegerSubsumerGraphImpl classGraph = null;
	private Map<Integer, Set<Integer>> cognateFunctPropMap = new HashMap<Integer, Set<Integer>>();
	private final ExtendedOntology extendedOntology;
	private IntegerEntityManager idGenerator = null;
	private Map<VNodeImpl, Integer> invNodeSet = new HashMap<VNodeImpl, Integer>();
	private Map<Integer, VNodeImpl> nodeSet = new HashMap<Integer, VNodeImpl>();
	private IntegerSubsumerBidirectionalGraphImpl objectPropertyGraph = null;
	private IntegerRelationMapImpl relationSet = null;

	/**
	 * Constructs a new classifier status.
	 * 
	 * @param generator
	 *            identifier generator
	 * @param ontology
	 *            extended ontology
	 */
	public ClassifierStatusImpl(IntegerEntityManager generator,
			ExtendedOntology ontology) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (ontology == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.idGenerator = generator;
		this.extendedOntology = ontology;

		createClassGraph();
		createObjectPropertyGraph();
		createRelationSet();
		createSetOfNodes();
		createMapOfObjectPropertiesWithFunctionalAncestor();
	}

	/**
	 * Adds a new triplet to the set R.
	 * 
	 * @param entry
	 *            the new triplet
	 * @return <code>true</code> if the triplet was effectively added,
	 *         <code>false</code> otherwise
	 */
	public boolean addToR(REntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.relationSet.contains(entry.getProperty(),
				entry.getLeftClass(), entry.getRightClass())) {
			this.relationSet.add(entry.getProperty(), entry.getLeftClass(),
					entry.getRightClass());
			ret = true;
		}
		return ret;
	}

	/**
	 * Adds a new pair to the set S.
	 * 
	 * @param entry
	 *            the new pair
	 * @return <code>true</code> if the pair was effectively added,
	 *         <code>false</code> otherwise
	 */
	public boolean addToS(SEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (!this.classGraph.getElements().contains(entry.getSubClass())) {
			this.classGraph.add(entry.getSubClass());
		}
		if (!this.classGraph.getSubsumers(entry.getSubClass()).contains(
				entry.getSuperClass())) {
			this.classGraph.addAncestor(entry.getSubClass(),
					entry.getSuperClass());
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean contains(VNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return (this.invNodeSet.get(node) != null);
	}

	private void createClassGraph() {
		this.classGraph = new IntegerSubsumerGraphImpl(bottomClassId,
				topClassId);
		for (Integer index : getExtendedOntology().getClassSet()) {
			this.classGraph.addAncestor(index, topClassId);
		}
		this.classGraph.addAncestor(topClassId, topClassId);

		this.nodeSet.clear();
		this.invNodeSet.clear();
		for (Integer elem : this.classGraph.getElements()) {
			VNodeImpl node = new VNodeImpl(elem);
			this.nodeSet.put(elem, node);
			this.invNodeSet.put(node, elem);
		}
	}

	private void createMapOfObjectPropertiesWithFunctionalAncestor() {
		for (Integer s : this.extendedOntology.getFunctionalObjectProperties()) {
			Collection<Integer> cognates = getSubObjectProperties(s);
			for (Integer r : cognates) {
				Set<Integer> currentSet = this.cognateFunctPropMap.get(r);
				if (currentSet == null) {
					currentSet = new HashSet<Integer>();
					this.cognateFunctPropMap.put(r, currentSet);
				}
				currentSet.addAll(cognates);
			}
		}

	}

	private void createObjectPropertyGraph() {
		this.objectPropertyGraph = new IntegerSubsumerBidirectionalGraphImpl(
				bottomObjectPropertyId, topObjectPropertyId);
		for (Integer index : this.extendedOntology.getObjectPropertySet()) {
			this.objectPropertyGraph.addAncestor(index, topObjectPropertyId);
			Integer inverseProp = this.idGenerator
					.createOrGetInverseObjectPropertyOf(index);
			this.objectPropertyGraph.addAncestor(inverseProp,
					topObjectPropertyId);
		}
		for (Integer property : this.extendedOntology.getObjectPropertySet()) {
			Set<RI2Axiom> axiomSet = this.extendedOntology
					.getRI2rAxioms(property);
			for (RI2Axiom axiom : axiomSet) {
				this.objectPropertyGraph.addAncestor(axiom.getSubProperty(),
						axiom.getSuperProperty());
			}
		}
		makeTransitiveClosure(this.objectPropertyGraph);
	}

	@Override
	public Integer createOrGetNodeId(VNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer ret = this.invNodeSet.get(node);
		if (ret == null) {
			ret = node.getClassId();
			if (!node.isEmpty()) {
				ret = getIdGenerator().createAnonymousEntity(
						IntegerEntityType.CLASS, true);
				VNodeImpl newNode = new VNodeImpl(node.getClassId());
				newNode.addExistentialsOf(node);
				this.nodeSet.put(ret, newNode);
				this.invNodeSet.put(newNode, ret);
			}
		}
		return ret;
	}

	private void createRelationSet() {
		Collection<Integer> collection = getObjectPropertyGraph().getElements();
		this.relationSet = new IntegerRelationMapImpl();
		for (Integer index : collection) {
			this.relationSet.add(index);
		}
	}

	private void createSetOfNodes() {
		for (Integer classId : getExtendedOntology().getClassSet()) {
			createOrGetNodeId(new VNodeImpl(classId));
		}
	}

	/**
	 * Deletes the class graph.
	 */
	protected void deleteClassGraph() {
		this.classGraph = null;
	}

	/**
	 * Deletes the object property graph.
	 */
	protected void deleteObjectPropertyGraph() {
		this.objectPropertyGraph = null;
	}

	@Override
	public Integer geObjectPropertyBottomElement() {
		return this.objectPropertyGraph.getBottomElement();
	}

	@Override
	public Integer getClassBottomElement() {
		return this.classGraph.getBottomElement();
	}

	/**
	 * Returns the class graph.
	 * 
	 * @return the class graph
	 */
	protected IntegerSubsumerGraphImpl getClassGraph() {
		return this.classGraph;
	}

	@Override
	public Integer getClassTopElement() {
		return this.classGraph.getTopElement();
	}

	/**
	 * Returns the number of nodes in the relation set.
	 * 
	 * @return the number of nodes in the relation set
	 */
	public long getDeepSizeOfR() {
		return this.relationSet.getDeepSize();
	}

	/**
	 * Returns the number of nodes in the subsumer set.
	 * 
	 * @return the number of nodes in the subsumer set
	 */
	public long getDeepSizeOfS() {
		return this.classGraph.getDeepSize();
	}

	/**
	 * Returns the number of elements in the node set.
	 * 
	 * @return the number of elements in the node set
	 */
	public long getDeepSizeOfV() {
		long ret = 0;
		for (Integer nodeId : this.nodeSet.keySet()) {
			VNodeImpl node = this.nodeSet.get(nodeId);
			ret += node.getDeepSize();
		}
		return ret;
	}

	@Override
	public ExtendedOntology getExtendedOntology() {
		return this.extendedOntology;
	}

	@Override
	public Collection<Integer> getFirstBySecond(Integer propertyId,
			Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getBySecond(propertyId, classId);
	}

	/**
	 * Returns the identifier generator.
	 * 
	 * @return the identifier generator
	 */
	protected IntegerEntityManager getIdGenerator() {
		return this.idGenerator;
	}

	@Override
	public Integer getInverseObjectPropertyOf(Integer propertyId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.idGenerator.createOrGetInverseObjectPropertyOf(propertyId);
	}

	@Override
	public VNode getNode(Integer nodeId) {
		if (nodeId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.nodeSet.get(nodeId);
	}

	@Override
	public Collection<Integer> getObjectPropertiesByFirst(Integer cA) {
		if (cA == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getRelationsByFirst(cA);
	}

	@Override
	public Collection<Integer> getObjectPropertiesBySecond(Integer cA) {
		if (cA == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getRelationsBySecond(cA);
	}

	@Override
	public Set<Integer> getObjectPropertiesWithFunctionalAncestor(
			Integer objectProperty) {
		if (objectProperty == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<Integer> ret = this.cognateFunctPropMap.get(objectProperty);
		if (ret == null) {
			ret = Collections.emptySet();
		} else {
			ret = Collections.unmodifiableSet(ret);
		}
		return ret;
	}

	/**
	 * Returns the object property graph.
	 * 
	 * @return the object property graph
	 */
	protected IntegerSubsumerBidirectionalGraphImpl getObjectPropertyGraph() {
		return this.objectPropertyGraph;
	}

	@Override
	public Integer getObjectPropertyTopElement() {
		return this.objectPropertyGraph.getTopElement();
	}

	/**
	 * Returns the set of relations.
	 * 
	 * @return the set of relations
	 */
	protected IntegerRelationMapImpl getRelationSet() {
		return this.relationSet;
	}

	@Override
	public Collection<Integer> getSecondByFirst(Integer propertyId,
			Integer classId) {
		if (propertyId == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.relationSet.getByFirst(propertyId, classId);
	}

	/**
	 * Returns the number of nodes.
	 * 
	 * @return the number of nodes
	 */
	public long getSizeOfV() {
		return this.nodeSet.size();
	}

	@Override
	public Collection<Integer> getSubObjectProperties(Integer objectProperty) {
		return Collections.unmodifiableCollection(this.objectPropertyGraph
				.getSubsumees(objectProperty));
	}

	@Override
	public Collection<Integer> getSubsumers(Integer classId) {
		if (classId == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return this.classGraph.getSubsumers(classId);
	}

	@Override
	public Collection<Integer> getSuperObjectProperties(Integer objectProperty) {
		return Collections.unmodifiableCollection(this.objectPropertyGraph
				.getSubsumers(objectProperty));
	}

	private void makeTransitiveClosure(
			IntegerSubsumerBidirectionalGraphImpl graph) {
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = false;
			for (Integer elem : graph.getElements()) {
				Collection<Integer> subsumerSet = graph.getSubsumers(elem);
				Set<Integer> allSubsumers = new HashSet<Integer>();
				allSubsumers.add(elem);
				for (Integer otherElem : subsumerSet) {
					allSubsumers.addAll(graph.getSubsumers(otherElem));
				}
				allSubsumers.removeAll(subsumerSet);
				if (!allSubsumers.isEmpty()) {
					hasChanged = true;
					for (Integer subsumer : allSubsumers) {
						graph.addAncestor(elem, subsumer);
					}
				}
			}
		}
	}

}
