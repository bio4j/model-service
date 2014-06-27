package bio4j.model.service

import unfiltered.request._
import unfiltered.response._
import argonaut._, Argonaut._

object SillyPlan extends unfiltered.filter.Plan {
  // recursive traversing of JSON
  def traverse(j: Option[Json], path: List[String]): Option[Json] = {
    path match {
      case Nil => j
      case head :: tail => j flatMap {
        // here we dispatch on JSON Array vs. JSON Object
        _.arrayOrObject(j,
          { arr: JsonArray  =>
            // if it's an array, we search for an object with such label
            traverse(arr find { _.field("label") == Some(head.asJson) }, tail)
          }, 
          { obj: JsonObject => 
            // if it's an object, we try to get a filed with such label
            traverse(obj(head), tail)
          }
        )
      }
    }
  }

  // we take the set of all defined schemas and convert it to a big JSON object
  val jsonSchemas = Json("schemas" := bio4j.model.schemas.mapList(SchemaSerialize.toJson))

  def intent = {
    case GET(Path(Seg(path))) => traverse(Some(jsonSchemas), path)  match {
      // FIXME: There is something wrong with return types if the status codes are used
      case Some(result) => Ok ~> JsonContent ~>  ResponseString(result.spaces4)
      case None => NotFound ~> ResponseString("Wrong path: " + path.mkString("/"))
    }
    case _ => MethodNotAllowed ~> ResponseString("Method must be GET")
  }
}

object Server {
  def main(args: Array[String]) {
    unfiltered.jetty.Http.local(8080).filter(SillyPlan).run()
  }
}
