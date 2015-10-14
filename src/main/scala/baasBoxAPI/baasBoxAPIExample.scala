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

@JSExport
object Example {

  def uniqueName( actualValue: List[String]):String={
    val newname = s"user_test_${Random.alphanumeric take 10 mkString("")}"
    if( ! actualValue.contains(newname) ) newname
    else uniqueName(actualValue);
  }
  
  @JSExport
  def main(): Unit = {
    BaasBox.setEndPoint("http://localhost:9000")
    BaasBox.appcode = "1234567890";
    
    val response=BaasBox.login("admin", "admin")

    response.done(( event: LoginResponse) => {
          js.Dynamic.global.console.log(s"Loggin Done : ${event.username}  ${event.token}")    
          
          
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
                  BaasBox.fetchObjectsCount( "collection1_02" ).done(
                    (response : GenericResponse[CountResponse] ) =>{
                      g.console.log(s" we have ${response.data.count} documents");
                    }:Unit
                      )
                      
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
            
          }:Unit).fail( (event: GenericResponse[String]) =>{ 
            js.Dynamic.global.console.log(s"Fail : ${event.result}") 
            
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
        
     response.fail(( event: LoginResponse) => {
          js.Dynamic.global.console.log(s"Loggin Fail : ${event.username}")    
        }:Unit)
}
}


