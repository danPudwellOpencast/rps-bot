package controllers

import javax.inject.Inject
import javax.inject.Singleton

import models.Game
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

@Singleton
class StartController @Inject() extends Controller {
  def start() = Action.async(parse.json) { implicit request =>
    Game.name = (request.body \ "opponentName").as[String]
    Game.dynamiteCount = (request.body \ "dynamiteCount").as[Int]
    Game.pointsToWin = (request.body \ "pointsToWin").as[Int]
    Game.opponentMoves = Nil
    Game.myMoves = Nil
    Future.successful(Ok)
  }
}
