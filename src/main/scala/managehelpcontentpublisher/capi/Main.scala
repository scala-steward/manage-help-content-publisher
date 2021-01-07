package managehelpcontentpublisher.capi

import upickle.default.write

object Main extends App {

  val capiKey = args(0)
  val capiDomain = args(1)
  val path = args(2)

//  val article = write[Article](Article.fromCapiPath(capiDomain, capiKey)(path))
  val tag = write[Tag](Tag.fromCapiPath(capiDomain, capiKey)(path))
  println(tag)
}
