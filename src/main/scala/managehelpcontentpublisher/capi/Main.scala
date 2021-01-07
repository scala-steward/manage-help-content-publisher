package managehelpcontentpublisher.capi

import upickle.default.write

object Main extends App {

  val apiKey = args(0)
  val capiDomain = args(1)
  val path = args(2)

  val article = write[Article](Article.fromCapiPath(capiDomain, apiKey)(path))
  println(article)
}
