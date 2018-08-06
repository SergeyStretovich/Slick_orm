package pkg

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent._
import scala.concurrent.duration._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver
import slick.jdbc.JdbcBackend.Database
import com.typesafe.config.ConfigFactory
import slick.lifted.TableQuery
import slick.driver.PostgresDriver.api._
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

import scala.util.{Failure, Success}

class Users(tag: Tag) extends Table[(Int,String, String,String)](tag, "users") {
  def id = column[Int]("id",O.PrimaryKey)
  def email = column[String]("email")
  def nickname = column[String]("nickname")
  def password = column[String]("password")
  def * = (id, email,nickname,password)
}

class Vehicles(tag: Tag) extends Table[(Int,String, String)](tag, "vehicles") {
  def id_user = column[Int]("id_user")
  def vtype = column[String]("vtype")
  def brand = column[String]("brand")

  def * = (id_user, vtype, brand )
}

case class UserVehicle(id:Int,name:String,vehicleType:String,vehicleManufacturer:String)

class DBAccess {
  def getUserVehicles(db:JdbcBackend.DatabaseDef):Future[Seq[(Int,String,String,String)]]={
    println("___select from database___")
      val usersdb = TableQuery[Users]
      val vehiclesdb = TableQuery[Vehicles]

      val q = for ((u, v) <- usersdb join vehiclesdb
        on (_.id === _.id_user)
      ) yield (u.id, u.nickname, v.vtype, v.brand)
      val dbAction = q.result
      val items = db.run(dbAction)

    items
  }
}

object Program extends App{

  val db:JdbcBackend.DatabaseDef = Database.forURL(ConfigFactory.load().getString("mydb.db.url"),
    ConfigFactory.load().getString("mydb.db.user"),
    ConfigFactory.load().getString("mydb.db.password"),
    driver=ConfigFactory.load().getString("mydb.db.driver"))


  val mdB  = new DBAccess()
  val items=mdB.getUserVehicles(db)

/*
   items.onComplete(
     x =>
     x match {
       case Success(items) => {
         println("sc")
         //Option[List[UserVehicle]](posts.map(gh=>UserVehicle(gh._1,gh._2,gh._3,gh._4)).toList)
        val rc= items.map(gh=>UserVehicle(gh._1,gh._2,gh._3,gh._4))
         println(rc.size)
         rc.foreach(println(_))
       }
       case _=>
         {
           println("yes")
         }
       case Failure(t) => println("An error has occurred: " + t.getMessage)
     }
   )
   */

  //-----------------------------
  /*{
       x.foreach(hy => {
         hy.map(gh => UserVehicle(gh._1,gh._2,gh._3,gh._4))
       })
     }

   )*/

/*
  items.onComplete(
    xTry => {
      xTry.map(sEq => {
        /*
        sEq.foreach(row => println(row))
        */
        val rc= sEq.map(gh=>UserVehicle(gh._1,gh._2,gh._3,gh._4))
        println(rc.size)
        rc.foreach(println(_))
      })
    }
  )
*/
  //items.onSuccess { case s => println(s"Result: $s") }
/*
  items.onSuccess { case sEq => {
    val rc= sEq//.map(gh=>UserVehicle(gh._1,gh._2,gh._3,gh._4)).toList
    println(rc.size)
    rc.foreach(println(_))
  } }
*/
  val sEq  = Await.result(items, 5.seconds)
  val rc= sEq.map(gh=>UserVehicle(gh._1,gh._2,gh._3,gh._4)).toList
  println(rc.size)
  rc.foreach(println(_))

//   hy.map(gh => UserVehicle(gh._1,gh._2,gh._3,gh._4))
  db.close
}









