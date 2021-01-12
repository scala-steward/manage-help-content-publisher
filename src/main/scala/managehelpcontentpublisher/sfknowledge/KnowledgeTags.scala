package managehelpcontentpublisher.sfknowledge

object KnowledgeTags {
  case class Topic(
      Name: String
  )
  case class Records(
      Topic: Topic
  )
  case class RootInterface(
      records: Seq[Records]
  )
}
