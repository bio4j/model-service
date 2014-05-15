package bio4j.modelservice

import com.bio4j.model._, go._, enzymedb._, isoforms._, ncbiTaxonomy._, proteinInteractions._, refseq._, uniprot._, uniprot_go._, uniprot_ncbiTaxonomy._, uniref._

import unfiltered.request._
import unfiltered.response._
import com.ohnosequences.typedGraphs.TypedGraph
import scala.collection.JavaConversions._
import argonaut._, Argonaut._
import argonaut.integrate.unfiltered._

object Modules {
  val modules = Set[TypedGraph](
    GoModule.go,
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

  implicit def ModuleEncodeJson: EncodeJson[TypedGraph] =
    EncodeJson((g: TypedGraph) =>
      ("pkg" := g.pkg) ->:
      ("dependencies" := g.dependencies.map(_.pkg).toList) ->:
      // TODO: we need to go deeper (proper serialization for node/rel/prop-Types)
      ("nodeTypes" := g.nodeTypes.map(_.toString).toList) ->:
      ("relationshipTypes" := g.relationshipTypes.map(_.toString).toList) ->:
      ("propertyTypes" := g.propertyTypes.map(_.toString).toList) ->:
      jEmptyObject
    )
}

object SillyPlan extends unfiltered.filter.Plan {
  import Modules._

  // TODO: take a look at the integration between argonaut and unfiltered
  def intent = {
    case req @ Path(Seg("schema" :: id :: tail)) => req match {
      case GET(_) => modules find { _.pkg == id } match {
        case None => NotFound ~> ResponseString("No schema with pkg id: " + id)
        case Some(module) => {
          val jmodule = module.asJson
          tail match {
            case Nil => JsonResponse(jmodule, spaces2)
            case field :: _ => (jmodule -| field) match {
                case Some(f) => JsonResponse(f, spaces2)
                case _ => NotFound ~> ResponseString("No such field: " + field)
              } 
          }
        }
      }
      case _ => MethodNotAllowed ~> ResponseString("Must be GET")
    }
  }
}

/* Embedded server */
object Server {
  def main(args: Array[String]) {
    unfiltered.jetty.Http.local(8080).filter(SillyPlan).run()
  }
}
