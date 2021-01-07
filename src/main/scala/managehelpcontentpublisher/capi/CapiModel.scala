package managehelpcontentpublisher.capi

import upickle.default.{Reader, macroR}

object CapiArticle {

  case class Fields(
      headline: String,
      trailText: String,
      body: String
  )
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

  case class Article(response: Response)
  object Article {
    implicit val reader: Reader[Article] = macroR
  }
}

object CapiTag {

  case class Fields(trailText: String)
  object Fields {
    implicit val reader: Reader[Fields] = macroR
  }

  case class Result(
      id: String,
      webTitle: String,
      webPublicationDate: String,
      fields: Fields
  )
  object Result {
    implicit val reader: Reader[Result] = macroR
  }

  case class Response(results: List[Result])
  object Response {
    implicit val reader: Reader[Response] = macroR
  }

  case class Tag(response: Response)
  object Tag {
    implicit val reader: Reader[Tag] = macroR
  }
}
