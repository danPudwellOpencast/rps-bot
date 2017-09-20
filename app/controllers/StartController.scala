package controllers

import javax.inject.Inject
import javax.inject.Singleton

import play.api.mvc.{Action, Controller}
import repository.MoveRepository
import scala.concurrent.Future

@Singleton
class StartController @Inject()(repo: MoveRepository) extends Controller {
  def start() = Action.async(parse.json) { implicit request =>
    val name = (request.body \ "opponentName").as[String]
    repo.start(name)
    Future.successful(Ok)
  }
}
