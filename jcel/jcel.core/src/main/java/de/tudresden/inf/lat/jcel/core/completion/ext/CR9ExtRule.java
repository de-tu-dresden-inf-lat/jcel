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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;

/**
 * <p>
 * <ul>
 * <li>CR-9 : <b>if</b> <u>(r<sub>1</sub>, x, y) &isin; R</u>, (r<sub>2</sub>,
 * x, z) &isin; R, r<sub>1</sub> &#8849;<sub><i>T</i></sub> s, r<sub>2</sub>
 * &#8849;<sub><i>T</i></sub> s, y = (&#8868; , &psi;), z = (&#8868; , &phi;), y
 * &ne; z, f(s) <br />
 * <b>then</b> v := (&#8868; , &psi; &cup; &phi;) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, k) | (y, k) &isin; S} &cup; {(v,
 * k) | (z, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r<sub>1</sub>, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR9ExtRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR-9.
	 */
	public CR9ExtRule() {
	}

	@Override
	public Collection<XEntry> apply(ClassifierStatus status, REntry entry) {
		if (status == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (entry == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return Collections.unmodifiableCollection(applyRule(status,
				entry.getProperty(), entry.getLeftClass(),
				entry.getRightClass()));
	}

	private Collection<XEntry> applyRule(ClassifierStatus status, Integer r1,
			Integer x, Integer y) {
		Set<XEntry> ret = new HashSet<XEntry>();
		VNode psiNode = status.getNode(y);
		if (psiNode.getClassId().equals(status.getClassTopElement())) {
			for (Integer r2 : status
					.getObjectPropertiesWithFunctionalAncestor(r1)) {
				for (Integer z : status.getSecondByFirst(r2, x)) {
					VNode phiNode = status.getNode(z);
					if (phiNode.getClassId()
							.equals(status.getClassTopElement())) {
						if (!y.equals(z)) {
							VNodeImpl newNode = new VNodeImpl(
									status.getClassTopElement());
							newNode.addExistentialsOf(psiNode);
							newNode.addExistentialsOf(phiNode);
							Integer v = status.createOrGetNodeId(newNode);
							for (Integer p : status.getSubsumers(y)) {
								ret.add(new SEntryImpl(v, p));
							}
							for (Integer p : status.getSubsumers(z)) {
								ret.add(new SEntryImpl(v, p));
							}
							ret.add(new REntryImpl(r1, x, v));
						}
					}
				}
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		return getClass().equals(o.getClass());
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
