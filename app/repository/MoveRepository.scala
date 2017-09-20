package repository

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.Action
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

@Singleton
class MoveRepository @Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends MongoController with ReactiveMongoComponents {

  def collection: JSONCollection = db.collection[JSONCollection]("moves")

  def create(move: String) = Action.async {
    val json = Json.obj("move" -> move)

    collection.insert(json).map(lastError =>
      Ok("Mongo LastError: %s".format(lastError)))
  }


}
