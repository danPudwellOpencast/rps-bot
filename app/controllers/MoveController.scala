package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.util.Random

class MoveController extends Controller {
  def move() = Action {
    Ok(Json.toJson(getMove))
  }

  def lastOpponentMove() = Action {
    Ok
  }

  private def getMove: String = {
    val moves = Map(0 -> "ROCK", 1 -> "PAPER", 2 -> "SCISSORS", 3 -> "DYNAMITE", 4 -> "WATERBOMB")
    moves(Random.nextInt(4))
  }
}
