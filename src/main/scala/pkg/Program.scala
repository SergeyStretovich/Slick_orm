package pkg

import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent._
import scala.concurrent.duration._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
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
  def getUserVehicles():Unit={
    println("___select from database___")
    val dbConfig: DatabaseConfig[PostgresDriver] = DatabaseConfig.forConfig("mydb")
    val db = dbConfig.db
    println("db " + db)
    try {
      val usersdb = TableQuery[Users]
      val vehiclesdb = TableQuery[Vehicles]
      /*
val act=usersdb.result
    val myres=db.run(act)
    myres.onComplete(x=>{
      x.foreach(hy=>println(hy))
    })
*/
      val q = for ((u, v) <- usersdb join vehiclesdb
        on (_.id === _.id_user)
      ) yield (u.id, u.nickname, v.vtype, v.brand)
      val dbAction = q.result
      // val f: Future[Seq[String]] = db.run(dbAction)
      val f = db.run(dbAction)
      f.onComplete(
        x => {
          x.foreach(hy => {
            hy.foreach(gh => println(gh))
          })
        }
      )
    }
    catch {
      case _: Throwable => println("got some exception")
    }
    finally
      db.close
  }
}

object Program extends App{
  val mdB  = new DBAccess
  mdB.getUserVehicles()
}


//   <logger name="scala.slick" level="INFO" />



































































