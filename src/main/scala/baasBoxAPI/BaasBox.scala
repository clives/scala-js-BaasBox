package baasBoxAPI
import scala.scalajs.js
import js.annotation._
import scala.scalajs.js.typedarray.ArrayBuffer
import upickle.Js
import derive.key
import upickle.default._
import scala.annotation.meta.field
import scala.scalajs.js
import scala.scalajs.js.JSON
import js.JSConverters._

object BaasBoxTools{
  //Coud not use PushMessage directly was giving me { message$x1 : "value" }
  implicit def PushMessageToString( pushmsg: PushMessage):js.Object={    
      js.Dynamic.literal("message"->pushmsg.message, "users" -> pushmsg.users.toJSArray)
   }
}

@JSName("BaasBox")
object BaasBox extends js.Object {
   def setEndPoint(url:String): Unit = js.native
   var appcode : String = js.native 
   def login(name:String, password: String ) : Callback[LoginResponse]  = js.native     
   def signup( username: String, password: String, additionalFields:AdditionalFields= AdditionalFields() ):String = js.native     
               
               
   def fetchUsers(): Callback[Users] = js.native 
   def fetchCurrentUser(): Callback[User] = js.native
   
   def sendPushNotification(message:js.Object): Callback[IPushResponse]= js.native
   
   def createCollection(collectionName: String): Callback[GenericResponse] = js.native
   
   def deleteCollection(collectionName: String): Callback[IPushResponse] = js.native
}

case class PushMessage( val message:String, val users: List[String])


trait GenericResponse extends js.Object {
  val result: String= js.native;
  val data: String= js.native;
  val http_code: Int= js.native;
}

@JSExportAll
case class IPushResponse( val result: String, val data: String, val http_code: String, val bb_code: Option[String]);

@JSExportAll
case class AdditionalFields(visibleByTheUser: js.Object= new js.Object, 
    visibleByRegisteredUsers: js.Object= new js.Object, visibleByAnonymousUsers: js.Object= new js.Object  ) 

trait Callback[ReturType] extends js.Object{
  def done(f:js.Function1[ReturType, Unit]):Callback[ReturType]= js.native
  def fail(f:js.Function1[ReturType, Unit]):Callback[ReturType]= js.native  
}    
    
trait IUser extends js.Object {
  def done(f:js.Function1[OneUser, Unit]):IResponse= js.native
  def fail(f:js.Function1[OneUser, Unit]):IResponse= js.native
}    
    
trait IUsers extends js.Object {
  def done(f:js.Function1[Users, Unit]):IResponse= js.native
  def fail(f:js.Function1[Users, Unit]):IResponse= js.native
}

trait IResponse extends js.Object {
  def done(f:js.Function1[LoginResponse, Unit]):IResponse= js.native
  def fail(f:js.Function1[LoginResponse, Unit]):IResponse= js.native
}

trait OneUser extends js.Object {
  val data:UserData = js.native;
}

trait Users extends js.Object {
  val data:js.Array[UserData] = js.native;
}

trait UserData extends js.Object{
  @JSName("user")
  val infoUser: User = js.native;
  val signUpDate: String = js.native;
  val visibleByTheUser: js.Object = js.native; //ex: "email": "cesare@email.com"
  val visibleByFriends: js.Object = js.native; //ex: "phoneNumber": "+1123456"
}

trait User extends js.Object {
  val name: String =js.native;
  val status:String =js.native;
}

trait LoginResponse extends js.Object {
  val token: String= js.native
  val username: String= js.native 
  val visibleByAnonymousUsers: String= js.native
  val undefinedvisibleByFriends: String= js.native
  val undefinedvisibleByRegisteredUsers: String= js.native
  val undefinedvisibleByTheUser: String= js.native  
}
//case class LoginResponse( result:String, http_code: Int);// "data":{"user":{"name":"admin","roles":[{"name":"administrator","isrole":true}],"status":"ACTIVE"},"signUpDate":"2015-10-12T20:45:46.166-0300","X-BB-SESSION":"2e0f7c19-33b0-48d5-ad9b-a75e8021565e"},"http_code":200}


//object addingConversionJsonToLoginResponse {
//  implicit def addFunction(ourString:String)=new{ 
//    def asLoginResponse()={read[LoginResponse](ourString)} 
//  }
//}