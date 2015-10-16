package baasBoxAPI

import scala.scalajs.js
import scala.scalajs.js.JSON
import js.JSConverters._
import java.util.regex.Pattern.Begin
import scala.scalajs.js
import com.sun.org.apache.xalan.internal.xsltc.DOM
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.Any.fromFunction1
import scala.util.Random
import BaasBoxTools._
import scala.scalajs.js
import js.Dynamic.{ global => g }
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import org.scalajs.jquery.jQuery
import org.scalajs.dom.raw.HTMLFormElement
import scala.annotation.meta.field
import upickle._
import js.JSConverters._

@JSExport
object Example {


  //
  // DAO
  //
  case class email( email: String);
  
  implicit def writerEmailToJsObject( ourinstance: email): js.Object ={
  		JSON.parse(write[email](ourinstance)).asInstanceOf[js.Object]
  }
  
  implicit def readerEmailToJsObject( ourjs:  GenericResponse[js.Object]): email ={
  
  		g.console.log("Stringify"+JSON.stringify(ourjs.data))
  
  		read[email](JSON.stringify(ourjs.data))
  }
  
  
  def uniqueName( actualValue: List[String]):String={
  		val newname = s"user_test_${Random.alphanumeric take 10 mkString("")}"
  		if( ! actualValue.contains(newname) ) newname
  		else uniqueName(actualValue);
  }
  
  @JSExport
  def testInsertDocument():Unit={
  		BaasBox.login("test_user", "test_user").map{
  			_ => BaasBox.save( email("test@yahoo.com"), "utest_collection").map{
  				response => 
  				g.console.log("Our Object id:"+response.id)
  
  				BaasBox.loadObject("utest_collection", response.id).map{
  					ourresponse =>
  					val ourmail: email= ourresponse
  					g.console.log("our email:"+ourmail);
  				}.onFailure{ case x =>
  				g.console.log("Fail");
  				g.console.log(x.toString)
  				}
  			}
  		}
  }
  
  @JSExport
  def testLoginFacebook():Unit={
  		BaasBox.loginFacebook( AuthenticationSocialNetwork("token", "token")).map{
  			result=> g.console.log(result.http_code)
  		}
  }
  
  @JSExport
  def testResetPassword():Unit={
  		BaasBox.login("test_user", "test_user").map{ _ =>
  		BaasBox.resetPassword();
  		}
  }
  
  @JSExport
  def testUpdateUserProfile():Unit={
  		BaasBox.login("test_user", "test_user").map{ _ =>
  		BaasBox.updateUserProfile( AdditionalFields( js.Dynamic.literal("email"-> "test@yahoo.com") ))
  		}
  }
  
  @JSExport
  def submitFile( event:org.scalajs.dom.Event ): Unit = {
  		g.console.log("submit file:"+jQuery("#fileinfo").asInstanceOf[HTMLFormElement])
  		event.preventDefault()
  		val ourformdata=new org.scalajs.dom.FormData(jQuery("#fileinfo").asInstanceOf[js.Array[HTMLFormElement]](0))
  		BaasBox.uploadFile(ourformdata).map{
  			response => g.console.log("upload ok: id:"+response)
  		}.onFailure{ case x => g.console.log("fail upload")}
  }
  
  @JSExport
  def testFetchFollower():Unit ={
  		g.console.log("click")
  		BaasBox.login("test_user2", "test_user2").map{ _ =>
  		g.console.log("fetch")
  		BaasBox.fetchFollowers("test_user").map{
  			response => 
  			g.console.log("result")
  			response.data.toList.foreach{ value=> g.console.log(value) }
  		}
  		}.onFailure{ case x => g.console.log("Failure")}
  }
  
  
  trait simpleDocument extends js.Object{
  	val msg: String =js.native
  }
  
  @JSExport
  def main(): Unit = {
  		BaasBox.setEndPoint("http://localhost:9000")
  		BaasBox.appcode = "1234567890";
  
  		val response=BaasBox.login("admin", "admin")
  
  }
}


