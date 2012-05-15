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

package de.tudresden.inf.lat.jcel.ontology.axiom.extension;

import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpressionVisitor;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataHasValue;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerDataSomeValuesFrom;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerNamedIndividual;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectIntersectionOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectInverseOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectOneOf;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectSomeValuesFrom;

/**
 * An object implementing this class analyzes an object property expression or a
 * class expression to detect what constructors are used in it.
 * 
 * @author Julian Mendez
 */
class IntegerExpressionAnalyzer implements
		IntegerClassExpressionVisitor<Boolean> {

	private boolean hasDatatype = false;
	private boolean hasInverseObjectProperty = false;
	private boolean hasNominal = false;

	/**
	 * Constructs a new expression analyzer.
	 */
	public IntegerExpressionAnalyzer() {
	}

	/**
	 * Tells whether the expression analyzer has detected data types.
	 * 
	 * @return <code>true</code> if and only if the expression analyzer has
	 *         detected data types
	 */
	public boolean hasDatatype() {
		return this.hasDatatype;
	}

	/**
	 * Tells whether the expression analyzer has detected inverse object
	 * properties.
	 * 
	 * @return <code>true</code> if and only if the expression analyzer has
	 *         detected inverse object properties
	 */
	public boolean hasInverseObjectProperty() {
		return this.hasInverseObjectProperty;
	}

	/**
	 * Tells whether the expression analyzer has detected nominals.
	 * 
	 * @return <code>true</code> if and only if the expression analyzer has
	 *         detected nominals
	 */
	public boolean hasNominal() {
		return this.hasNominal;
	}

	@Override
	public Boolean visit(IntegerClass classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerDataHasValue classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasDatatype = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerDataSomeValuesFrom classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasDatatype = true;
		return classExpression.getFiller().accept(this);
	}

	@Override
	public Boolean visit(IntegerNamedIndividual namedIndividual) {
		if (namedIndividual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerObjectIntersectionOf classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visit(classExpression.getOperands());
	}

	@Override
	public Boolean visit(IntegerObjectOneOf classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.hasNominal = true;
		return true;
	}

	@Override
	public Boolean visit(IntegerObjectSomeValuesFrom classExpression) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean fillerAcc = classExpression.getFiller().accept(this);
		IntegerObjectPropertyExpression prop = classExpression.getProperty();
		if (prop instanceof IntegerObjectInverseOf) {
			this.hasInverseObjectProperty = true;
		}
		return fillerAcc;
	}

	/**
	 * Visits a set of class expressions.
	 * 
	 * @param classSet
	 *            set of class expressions
	 * @return the visit result
	 */
	protected Boolean visit(Set<IntegerClassExpression> classSet) {
		if (classSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = true;
		for (IntegerClassExpression expr : classSet) {
			boolean accepted = expr.accept(this);
			ret = ret && accepted;
		}
		return ret;
	}

}