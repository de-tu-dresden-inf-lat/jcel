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

package de.tudresden.inf.lat.jcel.core.completion.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerEntityManager;

/**
 * <p>
 * <ul>
 * <li>CR bottom : <b>if</b> <u>(r, x, y) &isin; R</u>, (y, &#8869;) &isin; S <br />
 * <b>then</b> S := S &cup; {(x, &#8869;)}</li>
 * </ul>
 * </p>
 * 
 * Previous form:
 * <ul>
 * <li>CR4 : <b>if</b> (X, Y) &isin; R(r) <b>and</b> &#8869; &isin; S(Y)
 * <b>and</b> &#8869; &notin; S(X) <br />
 * <b>then</b> S(X) := S(X) &cup; {&#8869;}</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class CRBottomRule implements RObserverRule {

	/**
	 * Constructs a new completion rule CR bottom.
	 */
	public CRBottomRule() {
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

	private Collection<XEntry> applyRule(ClassifierStatus status, int r, int x,
			int y) {
		List<XEntry> ret = new ArrayList<XEntry>();
		if (status.getSubsumers(y).contains(IntegerEntityManager.bottomClassId)) {
			ret.add(new SEntryImpl(x, IntegerEntityManager.bottomClassId));
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
