package managehelpcontentpublisher

case class Topic(path: String, name: String)

object Topic {

  def fromSalesforceDataCategory(cat: String): Topic = Topic(
    path = cat.stripSuffix("__c"),
    name = "TODO" // TODO: fill in when request body complete
  )
}
