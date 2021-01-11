package managehelpcontentpublisher.sfknowledge

object KnowledgeTags {
  case class Topic(
      Name: String
  )
  case class Records(
      Id: String,
      EntityId: String,
      Topic: Topic
  )
  case class RootInterface(
      totalSize: Double,
      done: Boolean,
      records: Seq[Records]
  )
}
