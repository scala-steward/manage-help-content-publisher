package managehelpcontentpublisher

case class Topic(path: String, name: String)

object Topic {

  def fromSalesforceDataCategory(cat: ArticleDataCategory): Topic = Topic(
    path = cat.name.stripSuffix("__c"),
    name = cat.label
  )
}
