package controllers

import models.Moves
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.util.Random

class MoveController extends Controller {

  val moves = Map(0 -> "ROCK", 1 -> "PAPER", 2 -> "SCISSORS", 3 -> "DYNAMITE", 4 -> "WATERBOMB")

  def move() = Action {
    Ok(Json.toJson(getMove))
  }

  def lastOpponentMove() = Action.async(parse.json) { implicit request =>
    val move = (request.body \ "opponentLastMove").as[String]
    Moves.opponentMoves = Moves.opponentMoves :+ moves.find(_._2 == move).get._1
    Future.successful(Ok)
  }

  private def getMove: String = {

    val move = {
      if (Moves.myMoves.size < 10) moves(Random.nextInt(5))
      else if (Moves.myMoves.count(_ == 3).toFloat / Moves.myMoves.size < 0.1) moves(3) // if dynamite is less than 10% of moves played play a dynamite
      else if (Moves.myMoves.size > 500 && Moves.myMoves.size < 600) moves(Random.nextInt(5)) // play randomly between the 500th and 600th game
      else determineHistoricMove
    }
    Moves.myMoves = Moves.myMoves :+ moves.find(_._2 == move).get._1
    move
  }

  private def counterMove(move: Int): String = {
    move match {
      case 0 => moves(1)
      case 1 => moves(2)
      case 2 => moves(0)
      case 3 => moves(4)
      case 4 => moves(Random.nextInt(5))
    }
  }

  def determineHistoricMove: String = {

    val lengthOfChain = Random.nextInt(5) + 5 // get random value between 5 and 10
    if (Moves.opponentMoves.size > lengthOfChain * 5) {

      //try and find a sequence and counter the move played directly after sequence
      val currentSequence = Moves.opponentMoves.takeRight(lengthOfChain).mkString
      val wholeSequence = Moves.opponentMoves.mkString

      if (wholeSequence.contains(currentSequence)) {
        val index = wholeSequence.indexOf(currentSequence) + lengthOfChain
        println(wholeSequence)
        counterMove(wholeSequence.charAt(index).asDigit)
      }
      else {
        //if we can't find a sequence played previously look at highest played and counter
        counterMove(Moves.opponentMoves.groupBy(identity).maxBy(_._2.size)._1)
      }
    }
    else {
      moves(Random.nextInt(5))
    }
  }
}
