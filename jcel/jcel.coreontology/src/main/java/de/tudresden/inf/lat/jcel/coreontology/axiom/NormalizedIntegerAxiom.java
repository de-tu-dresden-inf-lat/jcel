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

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerAxiom;

/**
 * This interface is for normalized axioms, that is, resulting axioms of the
 * normalization process.
 * 
 * @author Julian Mendez
 */
public interface NormalizedIntegerAxiom extends IntegerAxiom {

	/**
	 * Applies a visitor to this normalized integer axiom.
	 * 
	 * @param <T>
	 *            the return type of the visitor's methods
	 * @param visitor
	 *            visitor
	 * @return the visit result
	 */
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor);

}