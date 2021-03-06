package com.leeriggins.awsapis.parser

import org.json4s._
import org.json4s.Extraction._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization._
import com.leeriggins.awsapis.models._
import com.leeriggins.awsapis.models.AwsApiType._

object Apis {
  val version = "2.1.23"
  val path = s"META-INF/resources/webjars/aws-sdk-js/${version}/apis"

  def filename(service: String, date: String, apiType: ApiType): String = {
    s"${path}/${service}-${date}.${apiType}.json"
  }

  def json(service: String, date: String, apiType: ApiType): String = {
    val inputStream = this.getClass.getClassLoader.getResourceAsStream(filename(service, date, apiType))
    val source = io.Source.fromInputStream(inputStream)
    try {
      source.mkString
    } finally {
      source.close()
    }
  }

  sealed abstract trait ApiType
  object ApiType {
    case object min extends ApiType
    case object normal extends ApiType
    case object paginators extends ApiType
  }

  def main(args: Array[String]): Unit = {
    implicit val formats = DefaultFormats + AwsApiTypeParser.Format + InputParser.Format + OutputParser.Format

    val text = json("autoscaling", "2011-01-01", ApiType.normal)
    println(text)
    println()

    val parsedText = parse(text)

    val api = parsedText.extract[Api]

    val reserialized = parse(write(api))

    val Diff(changed, added, removed) = parsedText diff reserialized

    println("Changed:")
    println(pretty(render(changed)))
    println()

    println("Added:")
    println(pretty(render(added)))
    println()

    println("Removed:")
    println(pretty(render(removed)))
    println()

  }

}
