package managehelpcontentpublisher.sfknowledge

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import scalaj.http._

object Main extends App {
  val knowledgeArticleId = "ka03N0000005quMQAQ";
  case class SfAuthDetails(access_token: String, instance_url: String)

  val optConfig = for {
    sfUserName <- Option(System.getenv("username"))
    sfClientId <- Option(System.getenv("clientId"))
    sfClientSecret <- Option(System.getenv("clientSecret"))
    sfPassword <- Option(System.getenv("password"))
    sfToken <- Option(System.getenv("token"))
    sfAuthUrl <- Option(System.getenv("authUrl"))

  } yield Config(
    SalesforceConfig(
      userName = sfUserName,
      clientId = sfClientId,
      clientSecret = sfClientSecret,
      password = sfPassword,
      token = sfToken,
      authUrl = sfAuthUrl
    )
  )

  val xxx = for {
    config <- optConfig.toRight(new RuntimeException("Missing config value"))
    sfAuthDetails <- decode[SfAuthDetails](auth(config.salesforceConfig))

    knowledgeArticles <- getSfKnowledgeArticles(
      sfAuthDetails
    )

  } yield {
    val knowledgeArticleRecords = knowledgeArticles.records
    println(
      "knowledgeArticleRecords(0).asJson.spaces2: " + knowledgeArticleRecords(
        0
      ).asJson.spaces2
    )

    val fff = ArticlesWithFormattedTags(knowledgeArticleRecords)
    println("fff: " + fff)

    ccc(knowledgeArticleRecords)
  }

  def getTags(
      sfAuthentication: SfAuthDetails
  ): Either[Error, KnowledgeTags.RootInterface] = {
    val tagsQuery =
      "Select Topic.name from TopicAssignment where EntityId = '" + knowledgeArticleId + "'"

    val tagsQueryResponse = doSfGetWithQuery(sfAuthentication, tagsQuery)
    println("TAG queryResponse:" + tagsQueryResponse)
    val tagsResponse = decode[KnowledgeTags.RootInterface](tagsQueryResponse)

    println("TAG response:" + tagsResponse)
    tagsResponse
  }

  def getSfKnowledgeArticles(
      sfAuthentication: SfAuthDetails
  ): Either[Error, KnowledgeArticles.RootInterface] = {

    val limit = 200;

    val articlesQuery =
      "SELECT Id,Body__c,PublishStatus,UrlName,(Select topic.name from TopicAssignments) from Knowledge__kav " +
        "where ID='ka03N0000005quMQAQ'"

    val articlesQueryResponse =
      doSfGetWithQuery(sfAuthentication, articlesQuery)

    println("ARTICLE queryResponse:" + articlesQueryResponse)

    val articlesResponse =
      decode[KnowledgeArticles.RootInterface](articlesQueryResponse) map { billingAccountsObject =>
        billingAccountsObject
      }

    println("ARTICLE response:" + articlesResponse)
    articlesResponse
  }

  def doSfGetWithQuery(sfAuthDetails: SfAuthDetails, query: String): String = {
    Http(s"${sfAuthDetails.instance_url}/services/data/v50.0/query/")
      .param("q", query)
      .option(HttpOptions.readTimeout(30000))
      .header("Authorization", s"Bearer ${sfAuthDetails.access_token}")
      .method("GET")
      .asString
      .body
  }

  def auth(salesforceConfig: SalesforceConfig): String = {
    Http(s"${System.getenv("authUrl")}/services/oauth2/token")
      .postForm(
        Seq(
          "grant_type" -> "password",
          "client_id" -> salesforceConfig.clientId,
          "client_secret" -> salesforceConfig.clientSecret,
          "username" -> salesforceConfig.userName,
          "password" -> s"${salesforceConfig.password}${salesforceConfig.token}"
        )
      )
      .asString
      .body
  }

  def ccc(recordList: Seq[KnowledgeArticles.Records]): Unit = {

    val tags = recordList
      .map(a => a.TopicAssignments)
      .map(a => a.records)
      .map(a => a.map(b => b.Topic.Name))
      .head
    println("tags:" + tags)

    tags
  }
  object ArticlesWithFormattedTags {
    def apply(
        recordList: Seq[KnowledgeArticles.Records]
    ): Unit = {

      val recordListWithincrementedGDPRAttempts =
        recordList.map(a => a.copy(FormattedTags = Some(a.TopicAssignments.records.map(b => b.Topic.Name))))
    }
  }
}
