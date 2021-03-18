package managehelpcontentpublisher

case class Article(title: String, body: String, path: String, topics: Seq[Topic])

object Article {

  def fromInput(input: InputArticle): Article = Article(
    title = input.title,
    body = input.body,
    path = input.urlName,
    topics = input.dataCategories.map(Topic.fromSalesforceDataCategory)
  )
}
