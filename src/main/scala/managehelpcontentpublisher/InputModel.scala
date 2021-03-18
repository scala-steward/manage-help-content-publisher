package managehelpcontentpublisher

import upickle.default._

case class InputModel(article: InputArticle, dataCategories: Seq[DataCategory])

object InputModel {
  implicit val reader: Reader[InputModel] = macroR
}

case class DataCategory(name: String)

object DataCategory {
  implicit val reader: Reader[DataCategory] = macroR
}
case class InputArticle(
    title: String,
    body: String,
    urlName: String,
    dataCategories: Seq[String]
)

object InputArticle {
  implicit val reader: Reader[InputArticle] = macroR
}
