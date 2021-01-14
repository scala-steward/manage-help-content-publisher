package htmlToJsonTranslator
import io.circe.generic.auto._
import io.circe.syntax._
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.jdk.CollectionConverters._
object Main extends App {

  case class ContentArray(
      `type`: String,
      contentString: String
  )
  case class Body(
      `type`: String,
      contentArray: Seq[ContentArray]
  )
  case class JsonBody(
      body: Seq[Body]
  )

  val html =
    """
    |  <body>
    |    <p>This is the main content</p><b>This is the important bit</b>
    |  </body>
      """.stripMargin

  generateJson(html, "body")

  def generateJson(searchIn: String, tagName: String): Unit = {
    val htmlSection: Elements = getChildElements(html, tagName)
    println(tagName + ":" + htmlSection)
    println("======================")

    val htmlSectionChildren = htmlSection.asScala.toSeq.head.children()
    //  println("bodyChildren:" + bodyChildren)
    //  println("======================")

    val bodyChildrenSize = htmlSectionChildren.size()
    println("bodyChildren size:" + bodyChildrenSize)
    println("======================")

    if (bodyChildrenSize > 0) {
      generateJsonForChildren(htmlSectionChildren)
    }
  }

  def generateJsonForChildren(htmlSectionChildren: Elements): Unit = {
    val fff = htmlSectionChildren.asScala.toSeq.map(a => ContentArray(`type` = a.tagName(), contentString = a.text))
    //  println("fff:" + fff)
    //  println("======================")

    val myBody = Body(
      `type` = "paragraph",
      contentArray = fff
    )

    val myJsonBody = JsonBody(body = Seq(myBody))
    println("myJsonBody:" + myJsonBody)
    println("myJsonBody.asJson.spaces2:" + myJsonBody.asJson.spaces2)
  }
  def getChildElements(searchIn: String, tagName: String): Elements = {
    Jsoup.parse(searchIn).select(tagName)
  }
}
