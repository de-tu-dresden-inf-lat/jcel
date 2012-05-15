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

package de.tudresden.inf.lat.jcel.adapter;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * An object of this class is a visitor used to translate an
 * <code>OWLAxiom</code> in the OWL API 2 into an <code>OWLAxiom</code> in the
 * OWL API 3. This translator does not translate annotation axioms, import
 * declarations and <code>SWRLRule</code>s. This is an auxiliary class used by
 * <code>SimpleTranslator</code>.
 * 
 * @author Julian Mendez
 * 
 */
public class AxiomTranslator implements
		org.semanticweb.owl.model.OWLAxiomVisitorEx<OWLAxiom> {

	private SimpleTranslator translator = null;

	public AxiomTranslator(SimpleTranslator trans) {
		this.translator = trans;
	}

	public OWLDataFactory getOWLDataFactory() {
		return this.translator.getOWLDataFactory();
	}

	public SimpleTranslator getTranslator() {
		return this.translator;
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLAsymmetricObjectPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLAxiomAnnotationAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(org.semanticweb.owl.model.OWLClassAssertionAxiom axiom) {
		return getOWLDataFactory().getOWLClassAssertionAxiom(
				getTranslator().translate(axiom.getDescription()),
				getTranslator().translate(axiom.getIndividual()));

	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom axiom) {
		return getOWLDataFactory().getOWLDataPropertyAssertionAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getSubject()),
				getTranslator().translate(axiom.getObject()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDataPropertyDomainAxiom axiom) {
		return getOWLDataFactory().getOWLDataPropertyDomainAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getDomain()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDataPropertyRangeAxiom axiom) {
		return getOWLDataFactory().getOWLDataPropertyRangeAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getRange()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDataSubPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLSubDataPropertyOfAxiom(
				getTranslator().translate(axiom.getSubProperty()),
				getTranslator().translate(axiom.getSuperProperty()));
	}

	@Override
	public OWLAxiom visit(org.semanticweb.owl.model.OWLDeclarationAxiom axiom) {
		return getOWLDataFactory().getOWLDeclarationAxiom(
				getTranslator().translate(axiom.getEntity()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDifferentIndividualsAxiom axiom) {
		return getOWLDataFactory().getOWLDifferentIndividualsAxiom(
				getTranslator().translateToNamedIndividuals(axiom.getIndividuals()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDisjointClassesAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLDisjointClassesAxiom(
				getTranslator().translateToClassExpressions(axiom.getDescriptions()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom axiom) {
		return getOWLDataFactory().getOWLDisjointDataPropertiesAxiom(
				getTranslator().translateToDataPropertyExpressions(axiom.getProperties()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom axiom) {
		return getOWLDataFactory().getOWLDisjointObjectPropertiesAxiom(
				getTranslator().translateToObjectPropertyExpressions(axiom.getProperties()));
	}

	@Override
	public OWLAxiom visit(org.semanticweb.owl.model.OWLDisjointUnionAxiom axiom) {
		return getOWLDataFactory().getOWLDisjointUnionAxiom(
				getTranslator().translate(axiom.getOWLClass()),
				getTranslator().translateToClassExpressions(axiom.getDescriptions()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLEntityAnnotationAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLEquivalentClassesAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLEquivalentClassesAxiom(
				getTranslator().translateToClassExpressions(axiom.getDescriptions()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom axiom) {
		return getOWLDataFactory().getOWLEquivalentDataPropertiesAxiom(
				getTranslator().translateToDataPropertyExpressions(axiom.getProperties()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLEquivalentObjectPropertiesAxiom(
				getTranslator().translateToObjectPropertyExpressions(axiom.getProperties()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLFunctionalDataPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLFunctionalObjectPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(org.semanticweb.owl.model.OWLImportsDeclaration axiom) {
		throw new TranslationException(new IllegalStateException(
				"Import declation axiom should have been already processed."));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLInverseFunctionalObjectPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom axiom) {
		return getOWLDataFactory().getOWLInverseObjectPropertiesAxiom(
				getTranslator().translate(axiom.getFirstProperty()),
				getTranslator().translate(axiom.getSecondProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLIrreflexiveObjectPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom axiom) {
		return getOWLDataFactory().getOWLNegativeDataPropertyAssertionAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getSubject()),
				getTranslator().translate(axiom.getObject()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom axiom) {
		return getOWLDataFactory().getOWLNegativeObjectPropertyAssertionAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getSubject()),
				getTranslator().translate(axiom.getObject()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom axiom) {
		return getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getSubject()),
				getTranslator().translate(axiom.getObject()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLSubPropertyChainOfAxiom(
				getTranslator().translate(axiom.getPropertyChain()),
				getTranslator().translate(axiom.getSuperProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLObjectPropertyDomainAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getDomain()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLObjectPropertyRangeAxiom(
				getTranslator().translate(axiom.getProperty()),
				getTranslator().translate(axiom.getRange()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLObjectSubPropertyAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLSubObjectPropertyOfAxiom(
				getTranslator().translate(axiom.getSubProperty()),
				getTranslator().translate(axiom.getSuperProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLOntologyAnnotationAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLReflexiveObjectPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLSameIndividualsAxiom axiom) {
		return getOWLDataFactory().getOWLSameIndividualAxiom(
				getTranslator().translateToNamedIndividuals(axiom.getIndividuals()));
	}

	@Override
	public OWLAxiom visit(org.semanticweb.owl.model.OWLSubClassAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLSubClassOfAxiom(
				getTranslator().translate(axiom.getSubClass()),
				getTranslator().translate(axiom.getSuperClass()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom axiom) {
		return getOWLDataFactory().getOWLSymmetricObjectPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(
			org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom axiom)
			throws TranslationException {
		return getOWLDataFactory().getOWLTransitiveObjectPropertyAxiom(
				getTranslator().translate(axiom.getProperty()));
	}

	@Override
	public OWLAxiom visit(org.semanticweb.owl.model.SWRLRule axiom) {
		return null;
	}
}