package repository

import javax.inject.{Inject, Singleton}

import models.Opponent
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MoveRepository @Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends MongoController with ReactiveMongoComponents {

  def collection: JSONCollection = db.collection[JSONCollection]("opponents")

  def start(name: String) = {

    val opponent = Opponent(name)
    collection.insert(opponent).map(lastError =>
      println(lastError))

  }

//  def create(move: String) = Action.async {
//
//    val query =
//
//    collection.insert(json).map(lastError =>
//      Ok("Mongo LastError: %s".format(lastError)))
//  }

}
