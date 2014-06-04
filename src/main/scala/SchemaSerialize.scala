package bio4j.modelservice

import bio4j._
import bio4j.model._
import bio4j.schemas._
import ohnosequences.scarph._
import ohnosequences.typesets._

import scala.collection.JavaConversions._
import argonaut._, Argonaut._

object SchemaSerialize {

  implicit val SchemaEncodeJson: EncodeJson[SimpleSchema] =
    EncodeJson(s =>
      ("label" := s.label) ->:
      ("propertyTypes" := s.propertyTypes.toList.map(_.label)) ->:
      ("vertexTypes" := s.vertexTypes.toList.map(_.label)) ->:
      ("edgeTypes" := s.edgeTypes.toList.map(_.label)) ->:
      jEmptyObject
    )

  implicit def PropertyEncodeJson: EncodeJson[AnyProperty] =
    EncodeJson(p =>
      ("label" := p.label) ->:
      // ("type" := p.Raw.???) ->:
      jEmptyObject
    )

}
