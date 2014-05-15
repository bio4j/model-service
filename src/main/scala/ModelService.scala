package bio4j.modelservice

import com.bio4j.model._, go._, enzymedb._, isoforms._, ncbiTaxonomy._, proteinInteractions._, refseq._, uniprot._, uniprot_go._, uniprot_ncbiTaxonomy._, uniref._

import unfiltered.request._
import unfiltered.response._
import com.ohnosequences.typedGraphs.TypedGraph
import scala.collection.JavaConversions._

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

  def representSet[T](s: java.util.Set[T]): String = s.mkString("{",", ","}")

  // TODO: this should return some fancy JSON object
  def representModule(g: TypedGraph): String = {
    Seq(g.pkg,
        "dependencies: " + g.dependencies.map(_.pkg).mkString("{",", ","}"),
        "node types: " + g.nodeTypes.mkString("{",", ","}"),
        "relationship types: " + g.relationshipTypes.mkString("{",", ","}"),
        "property types: " + g.propertyTypes.mkString("{",", ","}")
    ).mkString("\n\n")
  }
}

object SillyPlan extends unfiltered.filter.Plan {
  import Modules._

  def intent = {
    case req @ Path(Seg("schema" :: id :: tail)) => req match {
      case GET(_) => {
        modules find { _.pkg == id } match {
          case None => NotFound ~> ResponseString("No schema with pkg id: " + id)
          case Some(module) => tail match {
            case Nil => ResponseString(representModule(module))
            case "dependencies"      :: _ => ResponseString(representSet(module.dependencies.map(_.pkg)))
            case "nodeTypes"         :: _ => ResponseString(representSet(module.nodeTypes))
            case "relationshipTypes" :: _ => ResponseString(representSet(module.relationshipTypes))
            case "propertyTypes"     :: _ => ResponseString(representSet(module.propertyTypes))
            case _ => MethodNotAllowed ~> ResponseString("No such method")
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
