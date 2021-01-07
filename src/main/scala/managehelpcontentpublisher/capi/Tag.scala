package managehelpcontentpublisher.capi

import scalaj.http.Http
import upickle.default.{Writer, macroW, read}

case class Trail(path: String, title: String, trailText: String)

object Trail {

  implicit val writer: Writer[Trail] = macroW

  def fromCapiTagResult(result: CapiTag.Result): Trail = Trail(
    path = Path.toManagePath(result.id),
    title = result.webTitle,
    trailText = result.fields.trailText
  )
}

case class Tag(name: String, trails: List[Trail])

object Tag {

  implicit val writer: Writer[Tag] = macroW

  def fromCapiPath(capiDomain: String, capiKey: String)(path: String): Tag = {

    val response =
      Http(s"https://$capiDomain/help/$path")
        .param("api-key", capiKey)
        .param("show-fields", "trailText")
        .asString

    val input = read[CapiTag.Tag](response.body)

    Tag(
      name = path,
      trails = input.response.results.map(Trail.fromCapiTagResult)
    )
  }
}
