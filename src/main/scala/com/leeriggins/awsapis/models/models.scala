package com.leeriggins.awsapis.models

import org.json4s._

/** The API for a single AWS service like AutoScaling or EC2. */
case class Api(
  version: Option[String],
  metadata: Metadata,
  documentation: Option[String],
  operations: Map[String, Operation],
  shapes: Map[String, AwsApiType])

case class Metadata(
  apiVersion: String,
  checksumFormat: Option[String],
  endpointPrefix: String,
  globalEndpoint: Option[String],
  jsonVersion: Option[String],
  serviceAbbreviation: Option[String],
  serviceFullName: String,
  signingName: Option[String],
  signatureVersion: String,
  xmlNamespace: Option[String],
  targetPrefix: Option[String],
  timestampFormat: Option[String],
  protocol: String)

case class Operation(
  http: Option[Http],
  input: Option[Input],
  output: Option[Output],
  errors: Option[List[Error]],
  documentation: Option[String],
  documentationUrl: Option[String],
  deprecated: Option[Boolean],
  name: Option[String],
  alias: Option[String])

case class Error(
  shape: String,
  error: Option[ErrorInfo],
  exception: Option[Boolean],
  fault: Option[Boolean],
  documentation: Option[String],
  xmlOrder: Option[List[String]])

case class ErrorInfo(
  code: Option[String],
  httpStatusCode: Option[Int],
  senderFault: Option[Boolean])

case class Http(
  method: Option[String],
  requestUri: Option[String],
  responseCode: Option[Int])

/** Describes the input type of a method. */
case class Input(
  payload: Option[String],
  `type`: AwsApiType,
  xmlNamespace: Option[XmlNamespace])

/** Describes the output type of a method. */
case class Output(
  resultWrapper: Option[String],
  payload: Option[String],
  `type`: AwsApiType)

case class XmlNamespace(
  uri: Option[String],
  prefix: Option[String])

/** Parent trait for all types defined in the AWS APIs. */
abstract sealed trait AwsApiType {
  val location: Option[String]
  val locationName: Option[String]
  val documentation: Option[String]
  val deprecated: Option[Boolean]
}

/** A type that has a size. */
abstract sealed trait Sized {
  val min: Option[BigInt]
  val max: Option[BigInt]
}

/** Represents a (typically primitive) type that can be boxed. */
abstract sealed trait AwsApiBoxedType extends AwsApiType {
  val box: Option[Boolean]
}

/** Represents a String. Can either be explicitly specified as "type": "string" or implicit by omitting any type or shape fields. */
abstract sealed trait StringType extends AwsApiBoxedType {
  val xmlAttribute: Option[Boolean]
  val streaming: Option[Boolean]
  val sensitive: Option[Boolean]
  val pattern: Option[String]
}

object AwsApiType {
  case class ListType(
    location: Option[String],
    locationName: Option[String],
    member: AwsApiType,
    min: Option[BigInt],
    max: Option[BigInt],
    documentation: Option[String],
    flattened: Option[Boolean],
    deprecated: Option[Boolean]) extends AwsApiType with Sized

  case class MapType(
    location: Option[String],
    locationName: Option[String],
    key: AwsApiType,
    value: AwsApiType,
    min: Option[BigInt],
    max: Option[BigInt],
    documentation: Option[String],
    flattened: Option[Boolean],
    deprecated: Option[Boolean]) extends AwsApiType with Sized

  case class StructureType(
    location: Option[String],
    locationName: Option[String],
    required: List[String] = List(),
    members: Option[Map[String, AwsApiType]],
    documentation: Option[String],
    payload: Option[String],
    sensitive: Option[Boolean],
    deprecated: Option[Boolean],
    xmlNamespace: Option[XmlNamespace],
    xmlOrder: Option[List[String]],
    wrapper: Option[Boolean]) extends AwsApiType

  /** Describes an error that may be returned. Typically modeled as a structure but represented here as a separate type. */
  case class ErrorType(
    location: Option[String],
    locationName: Option[String],
    required: List[String] = List(),
    members: Option[Map[String, AwsApiType]],
    xmlOrder: Option[List[String]],
    error: Option[ErrorInfo],
    exception: Boolean,
    fault: Option[Boolean],
    documentation: Option[String],
    deprecated: Option[Boolean]) extends AwsApiType

  /** Describes a reference to a type defined in the service's shapes. */
  case class ShapeType(
    location: Option[String],
    locationName: Option[String],
    shape: String,
    box: Option[Boolean],
    documentation: Option[String],
    flattened: Option[Boolean],
    deprecated: Option[Boolean],
    xmlNamespace: Option[XmlNamespace],
    xmlOrder: Option[List[String]],
    xmlAttribute: Option[Boolean],
    queryName: Option[String],
    streaming: Option[Boolean],
    wrapper: Option[Boolean]) extends AwsApiType

  case class IntegerType(
    location: Option[String],
    locationName: Option[String],
    min: Option[BigInt],
    max: Option[BigInt],
    box: Option[Boolean],
    documentation: Option[String],
    deprecated: Option[Boolean]) extends AwsApiBoxedType with Sized

  case class LongType(
    location: Option[String],
    locationName: Option[String],
    min: Option[BigInt],
    max: Option[BigInt],
    box: Option[Boolean],
    documentation: Option[String],
    deprecated: Option[Boolean]) extends AwsApiBoxedType with Sized

  case class DoubleType(
    location: Option[String],
    locationName: Option[String],
    box: Option[Boolean],
    documentation: Option[String],
    deprecated: Option[Boolean]) extends AwsApiBoxedType

  case class FloatType(
    location: Option[String],
    locationName: Option[String],
    box: Option[Boolean],
    documentation: Option[String],
    deprecated: Option[Boolean]) extends AwsApiBoxedType

  case class TimestampType(
    location: Option[String],
    locationName: Option[String],
    box: Option[Boolean],
    documentation: Option[String],
    timestampFormat: Option[String],
    deprecated: Option[Boolean]) extends AwsApiBoxedType

  case class BooleanType(
    location: Option[String],
    locationName: Option[String],
    box: Option[Boolean],
    documentation: Option[String],
    deprecated: Option[Boolean]) extends AwsApiBoxedType

  case class BlobType(
    location: Option[String],
    locationName: Option[String],
    box: Option[Boolean],
    min: Option[BigInt],
    max: Option[BigInt],
    documentation: Option[String],
    streaming: Option[Boolean],
    sensitive: Option[Boolean],
    deprecated: Option[Boolean]) extends AwsApiBoxedType with Sized

  case class EnumType(
    location: Option[String],
    locationName: Option[String],
    box: Option[Boolean],
    min: Option[BigInt],
    max: Option[BigInt],
    documentation: Option[String],
    symbols: List[String],
    deprecated: Option[Boolean]) extends AwsApiBoxedType with Sized

  case class DefaultStringType(
    location: Option[String],
    xmlAttribute: Option[Boolean],
    locationName: Option[String],
    min: Option[BigInt],
    max: Option[BigInt],
    pattern: Option[String],
    box: Option[Boolean],
    documentation: Option[String],
    streaming: Option[Boolean],
    sensitive: Option[Boolean],
    deprecated: Option[Boolean]) extends StringType with Sized

  case class ExplicitStringType(
    location: Option[String],
    xmlAttribute: Option[Boolean],
    locationName: Option[String],
    min: Option[BigInt],
    max: Option[BigInt],
    pattern: Option[String],
    box: Option[Boolean],
    documentation: Option[String],
    streaming: Option[Boolean],
    sensitive: Option[Boolean],
    deprecated: Option[Boolean]) extends StringType with Sized

}
