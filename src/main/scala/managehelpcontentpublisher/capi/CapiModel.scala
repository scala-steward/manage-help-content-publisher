package managehelpcontentpublisher.capi

import upickle.default.{Reader, macroR}

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

case class CapiArticle(response: Response)
object CapiArticle {
  implicit val reader: Reader[CapiArticle] = macroR
}
