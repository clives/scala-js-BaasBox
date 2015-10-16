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
  
//  implicit def readerEmailToJsObject( ourjs: GenericResponse[js.Object]): email ={
//    read[email](JSON.stringify(ourjs.data))
//  }
  // ---
  
  
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
    //BaasBox.followUser("test_user").map{ response =>
      
          g.console.log("fetch")
          BaasBox.fetchFollowers("test_user").map{
            response => 
              g.console.log("result")
              response.data.toList.foreach{ value=> g.console.log(value) }
          }
     //   }
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
    
    jQuery("#fileinfo").submit( submitFile  _ )

    response.done(( event: LoginResponse) => {
          js.Dynamic.global.console.log(s"Loggin Done : ${event.username}  ${event.token}")    
          
          
          //
          // File
          //
          
          val ID_OBJECT ="e53d8709-e1ac-4eac-a77b-e832bf2a536e"
          BaasBox.loadObject( "utest_collection", ID_OBJECT).map{
          objectdata =>             
            objectdata.data
        }
          
          
//           val formatData=new org.scalajs.dom.FormData()
//          //formatData.append("fileName", "test.js", "test.js")
//          formatData.append("test.js", js.Dynamic.literal("body"->"testbody---", "info" -> "testInfo--"), "testScript.js2")
//          //File:
//          BaasBox.uploadFile(formatData)
          
          
          //Documents
          BaasBox.save(    js.Dynamic.literal("body"->"testbody", "info" -> "testInfo") , "collection1_02").done(
            ( event: SaveDocumentResponse)=>{  
                  js.Dynamic.global.console.log(s"save Done") 
                  js.Dynamic.global.console.log(s"save Done, version:  ${event.`@version`}, author: ${event._author}, id: ${event.id}") 
                  
                  
                  //get back our document:
                  BaasBox.loadObject("collection1_02", event.id).done(
                      ( event: GenericResponse[js.Object]) =>{
                        js.Dynamic.global.console.log(s"we have our document: "+event.data) 
                      }:Unit
                  )
                  
                  
                  //check nbr of documents in the collection:
                  val fetchOperation=BaasBox.fetchObjectsCount( "collection1_02" ).toFuture()
                  
                  fetchOperation.map{ result => g.console.log(s"Future -  we have ${result.data.count} documents") }
                    
                      
                  BaasBox.updateObject(event.id, "collection1_02",js.Dynamic.literal("body"->"testbody---", "info" -> "testInfo--") )
                  
                  g.console.log(s"About to update field on id: ${event.id}")
                  BaasBox.updateField(event.id, "collection1_02", "newField", "test_update").done( ( event: SaveDocumentResponse) =>{
                    g.console.log(s"About to delete document:  ${event.id}")
                    BaasBox.deleteObject(event.id, "collection1_02")
                  }:Unit
                  )
                  
                  
            }:Unit
          )
          
          
//          BaasBox.fetchCurrentUser().done( (event: OneUser) =>{
//            js.Dynamic.global.console.log(s"Done : ${event.data.infoUser.name}") 
//          }:Unit);
          
          
          BaasBox.createCollection( "collection1_02").done(( event: GenericResponse[String]) =>{ 
            js.Dynamic.global.console.log(s"Done : ${event.result}") 
            
          }:Unit).fail( (event: ErrorResponse) =>{ 
            js.Dynamic.global.console.log(s"Fail : ${event.responseText}") 
            
          }:Unit)
          
          
          BaasBox.sendPushNotification( PushMessage("test", List("admin") )).done(
              ( response: IPushResponse ) => {     
          }:Unit)
          
          BaasBox.fetchUsers().done(( event: Users) =>{
            
            event.data.toList.foreach { user => js.Dynamic.global.console.log(s"Done : ${user.infoUser.name}") }
            
            val listUserName=event.data.toList.map { user => user.infoUser.name }
            
            val ournewuser=uniqueName(listUserName)
            
            js.Dynamic.global.console.log( "new user:"+ournewuser)
            
            //we cannot reuse same email with same value. have to be unique
            BaasBox.signup(ournewuser, ournewuser);//, AdditionalFields( js.Dynamic.literal("email"->"namLLL@gmail.com")   ))
          }:Unit);                   
        }:Unit)
        
     response.fail(( event:  js.Object) => {
          js.Dynamic.global.console.log(s"Loggin Fail bb_code: ${event}")    
        }:Unit)
}
}


