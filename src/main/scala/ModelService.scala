package bio4j.modelservice

import unfiltered.request._
import unfiltered.response._
// import com.ohnosequences.typedGraphs._
import scala.collection.JavaConversions._
import argonaut._, Argonaut._
import argonaut.integrate.unfiltered._

import bio4j.modelservice.SchemaSerialize._
import bio4j.model._
import bio4j.schemas._
import ohnosequences.typesets._
import ohnosequences.scarph._

object SillyPlan extends unfiltered.filter.Plan {
  // TODO: take a closer look at the integration between argonaut and unfiltered
  object Convert {
    import shapeless._, poly._

    object toJson extends Poly1 {
      implicit def default[S <: AnySchema](implicit
          dm: ToList.Aux[S#Dependencies, AnySchema],
          pm: ToList.Aux[S#PropertyTypes, AnyProperty],
          vm: ToList.Aux[S#VertexTypes, AnyVertexType],
          em: ToList.Aux[S#EdgeTypes, AnyEdgeType]
        ) = at[S](s => s.asJson(SchemaEncodeJson(s)))
    }
  }
  val jsons: List[Json] = allSchemas.mapList(Convert.toJson)

  def intent = {
    case req @ Path(Seg("schema" :: id :: path)) => req match {
      case GET(_) => jsons find { _.field("label") == Some(id.asJson) } match {
        case Some(sch) => {
          // val jsch = sch.asJson(SchemaEncodeJson(sch))
          if (path.isEmpty) JsonResponse(sch, spaces2)
          else (sch -|| path) match {
                case Some(f) => JsonResponse(f, spaces2)
                case _ => NotFound ~> ResponseString("Wrong path: " + path.mkString("/"))
              } 
        }
        case _ => {
          println(jsons)
          NotFound ~> ResponseString("No schema with label: " + id)
        }
      }
      case _ => MethodNotAllowed ~> ResponseString("Must be GET")
    }
  }
}

object Server {
  def main(args: Array[String]) {
    unfiltered.jetty.Http.anylocal.filter(SillyPlan).run()
  }
}
