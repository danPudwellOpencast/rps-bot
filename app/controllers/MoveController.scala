package controllers

import models.Game
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.util.Random

class MoveController extends Controller {

  val ROCK = 0
  val PAPER = 1
  val SCISSORS = 2
  val DYNAMITE = 3
  val WATERBOMB = 4

  val rockPaperScissors = Map(ROCK -> "ROCK", PAPER -> "PAPER", SCISSORS -> "SCISSORS")
  val rockPaperScissorsDynamite = rockPaperScissors + (DYNAMITE -> "DYNAMITE")
  val allMoves = rockPaperScissorsDynamite + (WATERBOMB -> "WATERBOMB")

  def move() = Action {
    Ok(Json.toJson(getMove))
  }

  def lastOpponentMove() = Action.async(parse.json) { implicit request =>
    val move = (request.body \ "opponentLastMove").as[String]
    Game.opponentMoves = Game.opponentMoves :+ allMoves.find(_._2 == move).get._1
    Future.successful(Ok)
  }

  private def checkDynamiteUsage(move: Int): Int = {
    if (Game.myMoves.count(_ == 3) >= Game.dynamiteCount) // make sure we don't play more than X dynamite and substitute id we are over limit
      Random.nextInt(rockPaperScissors.size)
    else move
  }

  private def getMove: String = {

    val move = checkDynamiteUsage({
      if (Game.myMoves.size < 10) Random.nextInt(rockPaperScissorsDynamite.size)
      else if (Game.myMoves.size % 10 == 0) DYNAMITE // if dynamite is less than 10% of moves played then play a dynamite
      else determineHistoricMove
    })

    Game.myMoves = Game.myMoves :+ move
    allMoves(move)
  }

  private def counterMove(move: Int): Int = {
    move match {
      case ROCK => PAPER
      case PAPER => SCISSORS
      case SCISSORS => ROCK
      case DYNAMITE => Random.nextInt(rockPaperScissorsDynamite.size)
      case WATERBOMB => Random.nextInt(rockPaperScissorsDynamite.size)
    }
  }

  private def determineHistoricMove: Int = {

    val lengthOfChain = Random.nextInt(5) + 5 // get random value between 5 and 10
    if (Game.opponentMoves.size > lengthOfChain * 5) {

      //try and find a sequence and counter the move played directly after sequence
      val currentSequence = Game.opponentMoves.takeRight(lengthOfChain).mkString
      val wholeSequence = Game.opponentMoves.mkString

      val index = wholeSequence.indexOf(currentSequence) + lengthOfChain
      if (index+1 > wholeSequence.size) {
        counterMove(Game.opponentMoves.groupBy(identity).maxBy(_._2.size)._1)
      } else {
        counterMove(wholeSequence.charAt(index).asDigit)
      }
    }
    else {
      Random.nextInt(rockPaperScissorsDynamite.size)
    }
  }
}
