package models

import play.api.libs.json.Json


case class Opponent(name: String, moves: List[String] = Nil)

object Opponent {
  implicit val formats = Json.format[Opponent]
}