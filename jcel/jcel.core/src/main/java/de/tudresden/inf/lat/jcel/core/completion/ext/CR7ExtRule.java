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

package de.tudresden.inf.lat.jcel.core.completion.ext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-7 : <b>if</b> &exist; s<sup>-</sup> <i>.</i> A &#8849; B &isin;
 * <i>T</i> , <u>(r<sub>2</sub>, x, y) &isin; R</u>, x = (A', &phi;) , y = (B',
 * &psi;), <br />
 * r &#8728; r &#8849; r &isin; <i>T</i>, r<sub>1</sub>
 * &#8849;<sub><i>T</i></sub> r, r<sub>2</sub> &#8849;<sub><i>T</i></sub> r,
 * &exist; r<sub>1</sub><sup>-</sup> <i>.</i> A &isin; &phi;, r
 * &#8849;<sub><i>T</i></sub> s <br />
 * <b>then</b> v := (B', &psi; &cup; {&exist; r<sup>-</sup> <i>.</i> A}) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} ,
 * S := S &cup; {(v, k) | (y, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, B)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r<sub>2</sub>, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR7ExtRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-7.
	 */
	public CR7ExtRule() {
	}

	@Override
	public Collection<XEntry> apply(ClassifierStatus status, int property,
			int leftClass, int rightClass) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableCollection(applyRule(status, property,
				leftClass, rightClass));
	}

	private Collection<XEntry> applyRule(ClassifierStatus status, int r2,
			int x, int y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		VNode phiNode = status.getNode(x);
		VNode psiNode = status.getNode(y);
		for (int r : status.getSuperObjectProperties(r2)) {
			if (status.getExtendedOntology().getTransitiveObjectProperties()
					.contains(r)) {
				int rMinus = status.getInverseObjectPropertyOf(r);
				for (int s : status.getSuperObjectProperties(r)) {
					int sMinus = status.getInverseObjectPropertyOf(s);
					for (GCI3Axiom axiom : status.getExtendedOntology()
							.getGCI3rAxioms(sMinus)) {
						int a = axiom.getClassInSubClass();
						int b = axiom.getSuperClass();
						for (int r1 : status.getSubObjectProperties(r)) {
							int r1Minus = status.getInverseObjectPropertyOf(r1);
							if (phiNode.containsExistential(r1Minus, a)) {
								VNodeImpl newNode = new VNodeImpl(
										psiNode.getClassId());
								newNode.addExistentialsOf(psiNode);
								newNode.addExistential(rMinus, a);
								boolean inV = status.contains(newNode);
								int v = status.createOrGetNodeId(newNode);
								if (!inV) {
									for (int p : status.getSubsumers(y)) {
										ret.add(new SEntryImpl(v, p));
									}
								}
								ret.add(new SEntryImpl(v, b));
								ret.add(new REntryImpl(r2, x, v));
							}
						}
					}
				}
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		return (o != null) && getClass().equals(o.getClass());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
