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
          js.Dynamic.global.console.log(s"Done : ${event.username}  ${event.token}")    
          
//          BaasBox.fetchCurrentUser().done( (event: OneUser) =>{
//            js.Dynamic.global.console.log(s"Done : ${event.data.infoUser.name}") 
//          }:Unit);
          
          
          BaasBox.createCollection( "collection1_02").done(( event: GenericResponse) =>{ 
            js.Dynamic.global.console.log(s"Done : ${event.result}") 
            
          }:Unit).fail( (event: GenericResponse) =>{ 
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


