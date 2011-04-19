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

package de.tudresden.inf.lat.jcel.ontology.datatype;

/**
 * This interface is implemented by classes that model class expressions with
 * integer numbers.
 * 
 * @author Julian Mendez
 */
public interface IntegerClassExpression extends IntegerDatatype {

	public <T> T accept(IntegerClassExpressionVisitor<T> visitor);

	/**
	 * Tells whether or not this class expression contains a bottom.
	 * 
	 * @return <code>true</code> if and only if this class expression contains a
	 *         bottom
	 */
	public boolean containsBottom();

	/**
	 * Tells whether or not this class expression contains only literals.
	 * 
	 * @return <code>true</code> if and only if this class expression contains
	 *         only literals
	 */
	public boolean hasOnlyLiterals();

	/**
	 * Tells whether or not this class expression contains only intersections
	 * and literals.
	 * 
	 * @return <code>true</code> if and only if this class expression contains
	 *         only intersections and literals.
	 */
	public boolean isIntersectionOfLiterals();

	/**
	 * Tells whether or not this class expression is a literal.
	 * 
	 * @return <code>true</code> if and only if this class expression is a
	 *         literal
	 */
	public boolean isLiteral();

}
