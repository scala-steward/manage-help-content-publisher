package managehelpcontentpublisher.sfknowledge

object KnowledgeArticles {
  case class Records(
      Body__c: String,
      PublishStatus: String,
      UrlName: String,
      TopicAssignments: TopicAssignments,
      FormattedTags: Option[Seq[String]] = None
  )

  case class RootInterface(
      records: Seq[Records]
  )

  case class TopicAssignments(
      records: Seq[KnowledgeTags.Records]
  )
}
