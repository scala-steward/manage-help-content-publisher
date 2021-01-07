package managehelpcontentpublisher.capi

import scalaj.http.Http
import upickle.default.{Writer, macroW, read}

case class Article(title: String, body: String, topics: List[String])

object Article {

  implicit val writer: Writer[Article] = macroW

  def fromCapiPath(capiDomain: String, capiKey: String)(path: String): Article = {

    val response =
      Http(s"https://$capiDomain/$path")
        .param("api-key", capiKey)
        .param("show-fields", "all")
        .param("show-tags", "all")
        .asString

    val input = read[CapiArticle](response.body)

    Article(
      title = input.response.content.fields.headline,
      body = input.response.content.fields.body,
      topics = input.response.content.tags.filter(_.`type` == "keyword").map(_.id)
    )
  }
}
