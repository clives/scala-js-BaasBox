package baasBoxAPI
import scala.scalajs.js
import js.annotation._
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.annotation.meta.field
import scala.scalajs.js.JSON
import js.JSConverters._
import scala.concurrent.{Future, Promise}
import scala.util.{Try, Success, Failure}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import upickle._
import js.Dynamic.{ global => g }


object BaasBoxTools{
  
  //Coud not use PushMessage directly was giving me { message$x1 : "value" }
  implicit def PushMessageToJsObject( pushmsg: PushMessage):js.Object={    
      js.Dynamic.literal("message"->pushmsg.message, "users" -> pushmsg.users.toJSArray)
   }
     
  implicit def AdditionalFieldsToJsObject( fields: AdditionalFields):js.Object={    
      js.Dynamic.literal("visibleByTheUser"->fields.visibleByTheUser, "visibleByRegisteredUsers" -> fields.visibleByRegisteredUsers
          , "visibleByAnonymousUsers" -> fields.visibleByAnonymousUsers)
   }
  
  /*
   * Permits to change a Callback[A] to a Future[A] ( map/flatMap => permits the use of for....) 
   */
  implicit def callBackToFuture[A,B]( ourcallback: Callback[A, B]):Future[A] ={
    val promise =Promise[A];
    
    val a:Callback[A, B]=ourcallback.done( (event:A) => { promise.complete(Success(event)) }:Unit )
    
    val b:Callback[A, B]= ourcallback.fail( (event:B) => { promise.complete(Failure(new ThrowableWithErrorMsg[B](event))) }:Unit )
    promise.future
  }

  // add the fonction toFuture() =>  ourCallbacl.toFuture() to get our future
  implicit def addFunctionToFuture[A,B]( ourcallback: Callback[A, B]) = new{
    def toFuture():Future[A]={ ourcallback }
  }
  
  
  implicit def AuthenticationSocialNetworkToJs( auth: AuthenticationSocialNetwork): AuthenticationSocialNetworkJs={
    JSON.parse(write[AuthenticationSocialNetwork](auth)).asInstanceOf[AuthenticationSocialNetworkJs]   
  } 
  
  
  type DataCount = GenericResponse[CountResponse]
}


/*
 * Used for the future transformation of the done/fail.
 * insert the error msg into a Throwable
 */
case class ThrowableWithErrorMsg[ErrorType](val error: ErrorType) extends Throwable{}



/*
 * Main BaasBox object from the js library
 */
@JSName("BaasBox")
object BaasBox extends js.Object {
  
   //
   // Permission
   // 
   val READ_PERMISSION: String= js.native
   val UPDATE_PERMISSION: String = js.native
   val REGISTERED_ROLE: String = js.native  //default role for new user
   val ALL_PERMISSION: String =js.native
  
   
   //
   // Configuration
   // 
   
   def setEndPoint(url:String): Unit = js.native
   var appcode : String = js.native 
   
   
   //
   // Login / Signup
   //
   
   def login(name:String, password: String ) : Callback[LoginResponse, ErrorResponse]  = js.native
   
   def logout(): Callback[LogoutReponse, ErrorResponse] = js.native
   
   def signup( username: String, password: String, additionalFields:AdditionalFields= AdditionalFields() ):Callback[LoginResponse, ErrorResponse] = js.native     
                             
   def fetchUsers(): Callback[Users, ErrorResponse] = js.native 
   
   def fetchCurrentUser(): Callback[GenericResponse[UserData], ErrorResponse] = js.native
   
   def changePassword(oldpassword: String, newpassword: String) : Callback[GenericResponse[String], ErrorResponse] = js.native  
    
   def sendPushNotification(message:js.Object): Callback[IPushResponse, ErrorResponse]= js.native
   
   def updateUserProfile(additionalFields:js.Object):Callback[GenericResponse[UserData], ErrorResponse] =js.native
      
   def resetPassword():Callback[GenericResponse[String], ErrorResponse] =js.native 
   
      
   //
   // Collections
   //
   
   /*  The user calling this API must be the admin or belong to the admin role. */
   def createCollection(collectionName: String): Callback[GenericResponse[String], ErrorResponse] = js.native
   /*  The user calling this API must be the admin or belong to the admin role. */
   def deleteCollection(collectionName: String): Callback[IPushResponse, ErrorResponse] = js.native
   
   def loadCollection(collectionName: String): Callback[js.Array[js.Object], ErrorResponse]= js.native
   
   //
   // Documents
   //
   
   def save( document: js.Object, collectionName: String): Callback[SaveDocumentResponse, ErrorResponse]= js.native
      
   def updateObject( id: String ,  collectionName: String ,document: js.Object): Callback[SaveDocumentResponse, ErrorResponse]= js.native
   
   //event.data will contains the object
   def loadObject( collectionName: String, id: String): Callback[GenericResponse[js.Object], ErrorResponse]= js.native
   
   def fetchObjectsCount( collectionName: String ):  Callback[GenericResponse[CountResponse], ErrorResponse]= js.native
   
   def updateField( id: String, collectionName: String, fieldName: String, fieldValue: String): Callback[SaveDocumentResponse, ErrorResponse] = js.native
   
   def deleteObject(id:String, collectionName: String): Callback[SaveDocumentResponse, ErrorResponse] = js.native
   
   def grantRoleAccessToObject(collection: String, objectId:String, permission:String, role:String):Callback[GenericResponse[String], ErrorResponse]=js.native
   
   def revokeRoleAccessToObject(collection: String, objectId:String, permission:String, role:String):Callback[GenericResponse[String], ErrorResponse]=js.native
   
   //
   // Social
   //
   
   def loginGoogle( userid: AuthenticationSocialNetworkJs ):Callback[GenericResponse[UserData], ErrorResponse] =js.native 
  
   def loginFacebook( userid: AuthenticationSocialNetworkJs ):Callback[GenericResponse[UserData], ErrorResponse] =js.native 
   
   //
   // Friendship and Social API
   //
   
   def followUser(username: String):Callback[GenericResponse[UserData], ErrorResponse] =js.native
   
   def fetchFollowers(username: String):  Callback[GenericResponse[js.Array[UserData]], ErrorResponse]= js.native
   
   def unfollowUser(username: String):Callback[GenericResponse[String], ErrorResponse] =js.native
   
   def fetchFollowing(username: String):  Callback[GenericResponse[js.Array[UserData]], ErrorResponse]= js.native
   
   //
   // Files
   //
   
   //return json (String) 
   def uploadFile( dataa: org.scalajs.dom.FormData): Callback[String, ErrorResponse] = js.native

   def deleteFile(id:String): Callback[GenericResponse[String], ErrorResponse] = js.native
   
   def fetchFile(id:String) : Callback[String, ErrorResponse] = js.native
   
   def fetchFileDetails(id:String ) : Callback[GenericResponse[SaveDocumentResponse], ErrorResponse] = js.native
   
   def grantUserAccessToFile(fileId:String, permission:String, username:String):Callback[GenericResponse[String], ErrorResponse]=js.native
   
   def revokeUserAccessToFile(fileId:String, permission:String, username:String):Callback[GenericResponse[String], ErrorResponse]=js.native
   
   def grantRoleAccessToFile(fileId:String, permission:String, role:String):Callback[GenericResponse[String], ErrorResponse]=js.native
   
   def revokeRoleAccessToFile(fileId:String, permission:String, role:String):Callback[GenericResponse[String], ErrorResponse]=js.native
   
}


//js version of AuthenticationSocialNetwork
trait AuthenticationSocialNetworkJs extends js.Object {
    val oauth_token:String= js.native
    val oauth_secret:String= js.native
}

case class AuthenticationSocialNetwork( val oauth_token : String, val oauth_secret : String);

case class PushMessage( val message:String, val users: List[String])

// Main trait to receive response from the server as { data : xxxx , ...}
// response where the data field if of type "DataType". can be  String o more complexe
// like js.Object as for Documents
trait GenericResponse[DataType] extends js.Object {
  val result: String= js.native;
  val data: DataType= js.native;
  val http_code: Int= js.native;
}

trait CountResponse extends js.Object{
  val count: Int = js.native;
}

trait SaveDocumentResponse extends js.Object{
     val `@rid` : String= js.native;
     val `@version`: Int= js.native;
     val `@class`: String= js.native;
     val id:String= js.native;
     val _creation_date: String= js.native;
     val `_author`: String= js.native;     
}

@JSExportAll
case class IPushResponse( val result: String, val data: String, val http_code: String, val bb_code: Option[String]);

@JSExportAll
case class AdditionalFields(visibleByTheUser: js.Object= new js.Object, 
    visibleByRegisteredUsers: js.Object= new js.Object, visibleByAnonymousUsers: js.Object= new js.Object  ) 
    
trait AdditionalFieldsJs extends js.Object{
    val visibleByTheUser: js.Object = js.native;
    val visibleByRegisteredUsers: js.Object = js.native;
    val visibleByAnonymousUsers: js.Object =js.native;   
}

trait Callback[ReturType, ErrorType] extends js.Object{
  def done(f:js.Function1[ReturType, Unit]):Callback[ReturType, ErrorType]= js.native
  def fail(f:js.Function1[ErrorType, Unit]):Callback[ReturType, ErrorType]= js.native  
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
  val visibleByAnonymousUsers: js.Object = js.native
  val visibleByRegisteredUsers: js.Object = js.native
  val visibleByTheUser: js.Object = js.native; //ex: "email": "cesare@email.com"
  val visibleByFriends: js.Object = js.native; //ex: "phoneNumber": "+1123456"
}

trait User extends js.Object {
  val name: String =js.native;
  val status:String =js.native;
  val roles: js.Array[Name] =js.native;
}

trait Name extends js.Object{
  val name: String = js.native
}

trait LoginResponse extends js.Object {
  val token: String= js.native
  val username: String= js.native 
  val visibleByAnonymousUsers: String= js.native
  val undefinedvisibleByFriends: String= js.native
  val undefinedvisibleByRegisteredUsers: String= js.native
  val undefinedvisibleByTheUser: String= js.native  
}

trait LogoutReponse extends js.Object{
  val data : String= js.native
  val message: String = js.native
}

/*
 * {
    "result": "error",
    "bb_code": <custom error code, if necessary>,
    "message": "a message explaining the problem in plain English",
    "resource": "the REST API called",
    "method": "the HTTP method used",
    "request_header": { ... the headers received by the server... },
    "API_version": "...the BaasBox API version..."
}
 */
trait ErrorResponse extends js.Object {
  val readyState:Int= js.native
  val responseText:String= js.native
  val status: Int = js.native
  val statusText: String = js.native  
}