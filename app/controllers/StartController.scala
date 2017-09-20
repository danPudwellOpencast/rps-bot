package controllers

import javax.inject.Inject
import javax.inject.Singleton

import models.Opponent
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

@Singleton
class StartController @Inject() extends Controller {
  def start() = Action.async(parse.json) { implicit request =>
    val name = (request.body \ "opponentName").as[String]
    Opponent.moves = Nil
    Future.successful(Ok)
  }
}
