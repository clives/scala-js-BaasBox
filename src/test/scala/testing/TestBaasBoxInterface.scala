package testing

import cgta.otest.{AssertionFailure, FunSuite}
import scala.scalajs.js
import scala.scalajs.js.JSON
import js.JSConverters._
import java.util.regex.Pattern.Begin
import scala.scalajs.js
import com.sun.org.apache.xalan.internal.xsltc.DOM
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.Any.fromFunction1
import scala.util.Random
import baasBoxAPI._
import baasBoxAPI.BaasBoxTools._
import scala.scalajs.js
import js.Dynamic.{ global => g }
//import utest.ExecutionContext..RunNow
//import utest.ExecutionContext._
import scala.scalajs.concurrent._
import scala.concurrent.duration._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.concurrent.Await
import scala.util.{Try, Success, Failure}

object TestBaasBoxInterface extends FunSuite {
  
  def uniqueName( actualValue: List[String]):String={
    val newname = s"user_test_${Random.alphanumeric take 10 mkString("")}"
    if( ! actualValue.contains(newname) ) newname
    else uniqueName(actualValue);
  }
  

    
  println("T")
  test("Signup a new user") {
    
      BaasBox.setEndPoint("http://localhost:9000")
      BaasBox.appcode = "1234567890";
      
      println("Start")
     val response=BaasBox.login("admin", "admin").toFuture()
     response.onFailure{ case x => println("fail "+x)}
       
     val ourfuture=response.flatMap { _ => println("Logged"); BaasBox.fetchUsers().toFuture }
     
     ourfuture.map{
       ourUsers => 
         Assert.fail("")
         val listUserName =  ourUsers.data.toList.map { user => user.infoUser.name }
         val ourNewUniqueName=uniqueName( listUserName )
         
         BaasBox.signup(ourNewUniqueName, ourNewUniqueName)
     } 
     ourfuture.onComplete(_  match{case Success(x) => println("succes "+x)  
       case Failure(x) =>   println("failure "+x.getMessage)  
     }  )
     
     ourfuture.onFailure{ case x => Assert.fail() }
     
    // while( !ourfuture.isCompleted){}
    //Await.ready(ourfuture, 3.seconds).value.get
  }
  test("ZigZag64") {
    
  }  
  
  
}