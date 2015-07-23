import com.distancetracker.api.{DeviceApi, GPSApi}
import com.mongodb.casbah.Imports._
import com.example.app._
import com.swagger.{DistanceTrackerSwagger, ResourcesApp}
import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra.LifeCycle
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {

    // connecting with default settings localhost on port 27017
    val mongoClient = MongoClient("localhost", 27017)
    val mongoCollection = mongoClient("distance_tracker")("users")
    val dtSwagger = new DistanceTrackerSwagger

    // pass a reference to the Mongo collection into your servlet when you mount it at application start:
    context.mount(new MongoController(mongoCollection), "/*")

    context.mount(new DeviceApi(dtSwagger), "/Device/*")
    context.mount(new GPSApi(dtSwagger), "/GPS/*")

    //swagger
    context.mount(new ResourcesApp(dtSwagger), "/api-docs")

  }
}