package managehelpcontentpublisher.sfknowledge

object KnowledgeArticles {
  case class Records(
      Id: String,
      Body__c: String,
      PublishStatus: String,
      UrlName: String
  )

  case class RootInterface(
      totalSize: Int,
      done: Boolean,
      records: Seq[Records]
  )
}
