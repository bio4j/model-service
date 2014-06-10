package bio4j.modelservice

import bio4j._
import bio4j.model._
import bio4j.schemas._
import ohnosequences.scarph._
import ohnosequences.typesets._

import scala.collection.JavaConversions._
import argonaut._, Argonaut._

import shapeless._, poly._ //, ops.hlist._

object SchemaSerialize {

  implicit def PropertyEncodeJson: EncodeJson[AnyProperty] =
    EncodeJson(p =>
      ("label" := p.label) ->:
      // ("type" := p.Raw.getClass.getName) ->:
      jEmptyObject
    )

  implicit def EdgeEncodeJson: EncodeJson[AnyEdgeType] =
    EncodeJson(e =>
      ("label" := e.label) ->:
      ("sourceType" := e.sourceType.label) ->:
      ("targetType" := e.targetType.label) ->:
      ("inArity" := (e match {
          case _: ManyIn => "many"
          case _: OneIn => "one"
        })) ->:
      ("outArity" := (e match {
          case _: ManyOut => "many"
          case _: OneOut => "one"
        })) ->:
      jEmptyObject
    )

  def SchemaEncodeJson[S <: AnySchema](s: S)(implicit
      dm: ToList.Aux[s.Dependencies, AnySchema],
      pm: ToList.Aux[s.PropertyTypes, AnyProperty],
      vm: ToList.Aux[s.VertexTypes, AnyVertexType],
      em: ToList.Aux[s.EdgeTypes, AnyEdgeType]
    ): EncodeJson[s.type] =
    EncodeJson(s =>
      ("label"         := s.label) ->:
      ("dependencies"  :=  s.dependencies.toList.map(_.label)) ->:
      ("propertyTypes" := s.propertyTypes.toList.map(_.label)) ->:
      ("vertexTypes"   :=   s.vertexTypes.toList.map(_.label)) ->:
      ("edgeTypes"     :=     s.edgeTypes.toList.map(_.asJson)) ->:
      jEmptyObject
    )

}
