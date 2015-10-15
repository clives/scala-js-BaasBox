package baasBoxAPI
import scala.scalajs.js
import js.annotation._
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.annotation.meta.field
import scala.scalajs.js
import scala.scalajs.js.JSON
import js.JSConverters._
import scala.concurrent.{Future, Promise}
import scala.util.{Try, Success, Failure}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


object BaasBoxTools{
  //Coud not use PushMessage directly was giving me { message$x1 : "value" }
  implicit def PushMessageToString( pushmsg: PushMessage):js.Object={    
      js.Dynamic.literal("message"->pushmsg.message, "users" -> pushmsg.users.toJSArray)
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
  
  
  type DataCount = GenericResponse[CountResponse]
}

/*
 * Used for the future transformation of the done/fail.
 * insert the error msg into a Throwable
 */
case class ThrowableWithErrorMsg[ErrorType](val error: ErrorType) extends Throwable{
  
}


@JSName("BaasBox")
object BaasBox extends js.Object {
  
   //
   // Permission
   // 
   val READ_PERMISSION: String= js.native
   val UPDATE_PERMISSION: String = js.native
   val REGISTERED_ROLE: String = js.native
  
   def setEndPoint(url:String): Unit = js.native
   var appcode : String = js.native 
   def login(name:String, password: String ) : Callback[LoginResponse, ErrorResponse]  = js.native     
   def signup( username: String, password: String, additionalFields:AdditionalFields= AdditionalFields() ):Callback[LoginResponse, ErrorResponse] = js.native     
               
               
   def fetchUsers(): Callback[Users, ErrorResponse] = js.native 
   def fetchCurrentUser(): Callback[User, ErrorResponse] = js.native
   
   def sendPushNotification(message:js.Object): Callback[IPushResponse, ErrorResponse]= js.native
   
   
   //
   // Collections
   //
   
   /*  The user calling this API must be the admin or belong to the admin role. */
   def createCollection(collectionName: String): Callback[GenericResponse[String], ErrorResponse] = js.native
   /*  The user calling this API must be the admin or belong to the admin role. */
   def deleteCollection(collectionName: String): Callback[IPushResponse, ErrorResponse] = js.native
   
   
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


case class PushMessage( val message:String, val users: List[String])


trait CountResponse extends js.Object{
  val count: Int = js.native;
}

// response where the data field if of type "DataType". can be  String o more complexe
// like js.Object as for Documents
trait GenericResponse[DataType] extends js.Object {
  val result: String= js.native;
  val data: DataType= js.native;
  val http_code: Int= js.native;
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

trait Callback[ReturType, ErrorType] extends js.Object{
  def done(f:js.Function1[ReturType, Unit]):Callback[ReturType, ErrorType]= js.native
  def fail(f:js.Function1[ErrorType, Unit]):Callback[ReturType, ErrorType]= js.native  
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
//case class LoginResponse( result:String, http_code: Int);// "data":{"user":{"name":"admin","roles":[{"name":"administrator","isrole":true}],"status":"ACTIVE"},"signUpDate":"2015-10-12T20:45:46.166-0300","X-BB-SESSION":"2e0f7c19-33b0-48d5-ad9b-a75e8021565e"},"http_code":200}


//object addingConversionJsonToLoginResponse {
//  implicit def addFunction(ourString:String)=new{ 
//    def asLoginResponse()={read[LoginResponse](ourString)} 
//  }
//}