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

  implicit def SchemaEncodeJson[S <: AnySchema](s: S)(implicit
      dm: ToList.Aux[S#Dependencies, AnySchema],
      pm: ToList.Aux[S#PropertyTypes, AnyProperty],
      vm: ToList.Aux[S#VertexTypes, AnyVertexType],
      em: ToList.Aux[S#EdgeTypes, AnyEdgeType]
    ): EncodeJson[S] = EncodeJson(s =>
      ("label"         := s.label) ->:
      ("dependencies"  :=  dm(s.dependencies).map(_.label)) ->:
      ("propertyTypes" := pm(s.propertyTypes).map(_.label)) ->:
      ("vertexTypes"   :=   vm(s.vertexTypes).map(_.label)) ->:
      ("edgeTypes"     :=     em(s.edgeTypes).map(_.asJson)) ->:
      jEmptyObject
    )

}
