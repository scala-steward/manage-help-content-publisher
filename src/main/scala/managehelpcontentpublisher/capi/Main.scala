package managehelpcontentpublisher.capi

import scalaj.http.Http
import upickle.default.{Reader, Writer, macroR, macroW, read, write}

object Main extends App {

  val apiKey = args(0)
  val capiDomain = args(1)
  val path = args(2)

  case class Fields(headline: String, trailText: String, body: String)
  object Fields {
    implicit val reader: Reader[Fields] = macroR
  }
  case class Tag(`type`: String, id: String)
  object Tag {
    implicit val reader: Reader[Tag] = macroR
  }
  case class Content(
      `type`: String,
      id: String,
      fields: Fields,
      tags: List[Tag]
  )
  object Content {
    implicit val reader: Reader[Content] = macroR
  }
  case class Response(content: Content)
  object Response {
    implicit val reader: Reader[Response] = macroR
  }
  case class CapiArticle(response: Response)
  object CapiArticle {
    implicit val reader: Reader[CapiArticle] = macroR
  }

  case class Article(title: String, body: String, topics: List[String])
  object Article {
    implicit val writer: Writer[Article] = macroW
  }

  val response =
    Http(s"https://$capiDomain/$path")
      .param("api-key", apiKey)
      .param("show-fields", "all")
      .param("show-tags", "all")
      .asString

  println(response.code)
  println(response.body)

  val input = read[CapiArticle](response.body)
  val article = write[Article](
    Article(
      title = input.response.content.fields.headline,
      body = input.response.content.fields.body,
      topics = input.response.content.tags.filter(_.`type` == "keyword").map(_.id)
    )
  )
  println(article)
}
