package bio4j.model.service

import ohnosequences.scarph._
import ohnosequences.typesets._

import argonaut._, Argonaut._
import shapeless._, poly._

object SchemaSerialize {

  object toJson extends Poly1 {

    implicit def propertyCase[P <: AnyProperty] = at[P]{ p =>
      Json(
        "label" := p.label,
        "type"  := p.classTag.runtimeClass.asInstanceOf[Class[p.Raw]].getCanonicalName
      )
    }

    implicit def vertexCase[VT <: AnyVertexType, Ps <: TypeSet](implicit
      p: ToList.Aux[Ps, AnyProperty]
    ) = at[(VT, Ps)]{ case (vt, ps) => 
      Json(
        "label" := vt.label,
        "properties" := p(ps).map(_.label)
      )
    }

    implicit def edgeCase[ET <: AnyEdgeType, Ps <: TypeSet](implicit
      p: ToList.Aux[Ps, AnyProperty]
    ) = at[(ET, Ps)]{ case (et, ps) =>
      Json(
        "label" := et.label,
        "properties" := p(ps).map(_.label),
        "source" := Json(
          "type" := et.sourceType.label,
          "arity" := (et match {
              case _: ManyIn => "many"
              case _: OneIn => "one"
            })
        ),
        "target" := Json(
          "type" := et.targetType.label,
          "arity" := (et match {
              case _: ManyOut => "many"
              case _: OneOut => "one"
            })
        )
      )
    }

    implicit def schemaCase[S <: AnyGraphSchema](implicit
      d: ToList.Aux[S#Dependencies, AnyGraphSchema],
      p: ListMapper.Aux[toJson.type, S#Properties, Json],
      v: ListMapper.Aux[toJson.type, S#VerticesWithProperties, Json],
      e: ListMapper.Aux[toJson.type, S#EdgesWithProperties, Json]
    ) = at[S]{ s =>
      Json(
        "label"        := s.label,
        "dependencies" := d(s.dependencies).map(_.label),
        "properties"   := p(s.properties),
        "vertexTypes"  := v(s.verticesWithProperties),
        "edgeTypes"    := e(s.edgesWithProperties)
      )
    }

  }
}
