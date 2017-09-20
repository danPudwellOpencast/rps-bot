package controllers

import models.Moves
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.util.Random

class MoveController extends Controller {

  def move() = Action {
    Ok(Json.toJson(getMove))
  }

  def lastOpponentMove() = Action.async(parse.json) { implicit request =>
    val move = (request.body \ "opponentLastMove").as[String]
    Moves.opponentMoves = Moves.opponentMoves :+ move
    Future.successful(Ok)
  }

  private def getMove: String = {
    val moves = Map(0 -> "ROCK", 1 -> "PAPER", 2 -> "SCISSORS", 3 -> "DYNAMITE", 4 -> "WATERBOMB")
    val move = moves(Random.nextInt(5))
    Moves.myMoves = Moves.myMoves :+ move
    move
  }
}
