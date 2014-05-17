package bio4j.modelservice

import com.bio4j.model._, go._, enzymedb._, isoforms._, ncbiTaxonomy._, proteinInteractions._, refseq._, uniprot._, uniprot_go._, uniprot_ncbiTaxonomy._, uniref._

import unfiltered.request._
import unfiltered.response._
import com.ohnosequences.typedGraphs._
import scala.collection.JavaConversions._
import argonaut._, Argonaut._
import argonaut.integrate.unfiltered._

object Modules {
  implicit def NodeTypeEncodeJson: EncodeJson[NodeType[_, _]] =
    EncodeJson(t =>
      ("type" := t.value.toString) ->:
      jEmptyObject
    )

  implicit def NodeTypeWithPropsEncodeJson(props: List[PropertyType[_,_,_,_,_]]): EncodeJson[NodeType[_, _]] =
    EncodeJson(t =>
      ("type" := t.value.toString) ->:
      ("propertyTypes" := props.filter(t == _.elementType).map(_.fullName)) ->:
      jEmptyObject
    )

  implicit def RelationshipTypeEncodeJson: EncodeJson[RelationshipType[_, _, _, _, _, _]] =
    EncodeJson(t =>
      ("type" := t.value.toString) ->:
      ("arity" := t.arity.toString) ->:
      ("sourceType" := t.sourceType.toString) ->:
      ("targetType" := t.targetType.toString) ->:
      jEmptyObject
    )

  implicit def PropertyTypeEncodeJson: EncodeJson[PropertyType[_, _, _, _, _]] =
    EncodeJson(t =>
      // FIXME: cannot retrieve namefield :
      // ("name" := t.name.toString) ->:
      ("elementType" := t.elementType.toString) ->:
      ("fullName" := t.fullName.toString) ->:
      jEmptyObject
    )

  implicit def ModuleEncodeJson: EncodeJson[TypedGraph] =
    EncodeJson(g =>
      ("pkg" := g.pkg) ->:
      ("dependencies" := g.dependencies.toList.map(_.pkg)) ->:
      // ("nodeTypes" := g.nodeTypes.toList.map(_.asJson)) ->:
      ("nodeTypes" := g.nodeTypes.toList.map(NodeTypeWithPropsEncodeJson(g.propertyTypes.toList).encode)) ->:
      ("relationshipTypes" := g.relationshipTypes.toList.map(_.asJson)) ->:
      // ("propertyTypes" := g.propertyTypes.toList.map(_.asJson)) ->:
      jEmptyObject
    )

  object go extends GoGraph

  val modules = Set[TypedGraph](
    go,
    EnzymeDBModule.enzymeDB,
    IsoformsModule.isoforms, 
    NcbiTaxonomyModule.ncbiTaxonomy, 
    ProteinInteractionsModule.proteinInteractions, 
    RefSeqModule.refseq, 
    UniProtModule.uniprot, 
    UniProt_GoModule.uniprot_go, 
    UniProt_NcbiTaxonomyModule.uniprot_ncbiTaxonomy, 
    UniRefModule.uniref
  )
}

object SillyPlan extends unfiltered.filter.Plan {
  import Modules._

  // TODO: take a closer look at the integration between argonaut and unfiltered
  def intent = {
    case req @ Path(Seg("schema" :: id :: path)) => req match {
      case GET(_) => modules find { _.pkg == id } match {
        case Some(module) => {
          val jmodule = module.asJson
          if (path.isEmpty) JsonResponse(jmodule, spaces2)
          else (jmodule -|| path) match {
                case Some(f) => JsonResponse(f, spaces2)
                case _ => NotFound ~> ResponseString("No such field: " + path.mkString("/"))
              } 
        }
        case _ => NotFound ~> ResponseString("No schema with pkg id: " + id)
      }
      case _ => MethodNotAllowed ~> ResponseString("Must be GET")
    }
  }
}

object Server {
  def main(args: Array[String]) {
    unfiltered.jetty.Http.local(8080).filter(SillyPlan).run()
  }
}
