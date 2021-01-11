package managehelpcontentpublisher.sfknowledge
import io.circe._
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.parser._
import scalaj.http.{Http, HttpOptions}

object Main extends App {
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
    val sfRecords = knowledgeArticles.records

    val abc = sfAuthDetails
    getTags(abc)
    //println("sfRecords: " + sfRecords)

    //println("sfRecords(0).asJson.spaces2: " + sfRecords(0).asJson.spaces2)
  }

  def getTags(
      sfAuthentication: SfAuthDetails
  ): Either[Error, KnowledgeTags.RootInterface] = {
    val tagsQuery =
      "Select Id, EntityId, Topic.name from TopicAssignment where EntityId = 'ka03N0000005pneQAA'"

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
      "SELECT Id,  Body__c, PublishStatus, UrlName from Knowledge__kav where Id in ('ka03N0000005pneQAA')"

    val articlesQueryResponse =
      doSfGetWithQuery(sfAuthentication, articlesQuery)
    println("ARTICLE queryResponse:" + articlesQueryResponse)
    val articlesResponse =
      decode[KnowledgeArticles.RootInterface](articlesQueryResponse)

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

}
