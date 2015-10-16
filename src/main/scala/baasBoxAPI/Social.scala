package baasBoxAPI
import scala.scalajs.js

/*
 * define access to FB sdk from scalajs (Once logged in FB)
 */


object FB extends js.Object{
   def getAuthResponse(): FBAuthResponse =js.native
}

trait FBAuthResponse extends js.Object {
  val accessToken: String = js.native
}