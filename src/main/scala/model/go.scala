package bio4j.model

import ohnosequences.scarph._

object go {

  // vertices

  object Term          extends VertexType("Term")
  implicit val Term_id = Term has id
  implicit val Term_name = Term has name
  implicit val Term_description = Term has description
  object GoSlims       extends VertexType("GoSlims")
  object SubOntologies extends VertexType("SubOntologies")

  // edges

  // ontology rels
  object IsA                  extends ManyToMany(Term, "IsA", Term)
  object PartOf               extends ManyToMany(Term, "PartOf", Term)
  object HasPartOf            extends ManyToMany(Term, "HasPartOf", Term)
  object Regulates            extends ManyToMany(Term, "Regulates", Term)
  object PositivelyRegulates  extends ManyToMany(Term, "PositivelyRegulates", Term)
  object NegativelyRegulates  extends ManyToMany(Term, "NegativelyRegulates", Term)
  // subontologies
  object MolecularFunction    extends ManyToOne(Term, "MolecularFunction", SubOntologies)
  object CellularComponent    extends ManyToOne(Term, "CellularComponent", SubOntologies)
  object BiologicalProcess    extends ManyToOne(Term, "BiologicalProcess", SubOntologies)
  // slims
  object PlantSlim            extends ManyToOne(Term, "PlantSlim", GoSlims)
  // TODO: add others

  // properties

  object id extends Property[String]
  object name extends Property[String]
  object description extends Property[String]
}