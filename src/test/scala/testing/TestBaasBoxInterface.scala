package testing
import utest._
import scala.concurrent.Future
import scala.concurrent.duration.Deadline
import scala.util.Random
import utest.asserts.{RetryInterval, RetryMax}
import utest.ExecutionContext.RunNow
import baasBoxAPI._
import baasBoxAPI.BaasBoxTools._
import scala.util.{Success, Failure}
import scala.scalajs.js
import js.Dynamic.{ global => g }
import scala.concurrent.{Future, Promise}
import org.scalajs.jquery.jQuery
import org.scalajs.dom.raw.HTMLFormElement

class nonError extends Throwable;
/*
 * Note: the future have to be the last command in "block"
 */
object Parallel extends TestSuite{
  
  def uniqueName( actualValue: List[String]):String={
    val newname = s"user_test_${Random.alphanumeric take 10 mkString("")}"
    if( ! actualValue.contains(newname) ) newname
    else uniqueName(actualValue);
  }  
  
val tests = TestSuite {
  
  'Loggin {
    BaasBox.setEndPoint("http://localhost:9000")
    BaasBox.appcode = "1234567890";
    
    val loginFuture=BaasBox.login("admin", "admin").toFuture()

    //file with Id: 07dcf634-68df-4969-afa5-a5b932ecc5fd
    //have to be present on the baasbox
    'FetchFiles{
      BaasBox.fetchFile("07dcf634-68df-4969-afa5-a5b932ecc5fd").map { filecontent =>
        println(s"file: <<${filecontent.take(20)} >>")
      }
    }
    
    'fetchFileDetails{
      BaasBox.fetchFileDetails("07dcf634-68df-4969-afa5-a5b932ecc5fd").map { infofile =>
        println(s"creation date: <<${infofile.data._creation_date} >>")
      }
    }
    
    'CreateCollection{
      loginFuture.flatMap{ _ =>
        BaasBox.createCollection("collection_"+System.currentTimeMillis())
      }
    }
    
    /*
     * could fail, BaasBox error:
     * OTimeoutException: Timeout on acquiring exclusive lock against
     */
//    'CreateAndDeleteCollection{
//      loginFuture.flatMap{ _ =>
//        val name="collection_del_"+System.currentTimeMillis()
//        BaasBox.createCollection(name).flatMap{ _ => BaasBox.fetchCurrentUser()}flatMap { _=>
//          
//          val test= Promise[Int]
//          g.setTimeout({ println("end");test.complete(Success(1)) }:Unit , 60000)
//
//          val response=test.future.flatMap { _ => BaasBox.deleteCollection(name)}
//          response.onFailure{ 
//            case ThrowableWithErrorMsg(data) => println("Failure --CreateAndDeleteCollection-"+data.asInstanceOf[ErrorResponse].responseText)
//            assert(true)
//          }
//          response
//        }
//      }
//    }
    
    'SignupFail{
     BaasBox.signup("admin", "admin").map{ x => throw new nonError()}.recover{ 
       case x => if(x.isInstanceOf[nonError] ) assert(false) else println("error:"+x);"KO"
       
     }
    }
    
    'Signup {
      
      BaasBox.fetchUsers().flatMap{ users=>
        val f=BaasBox.signup(uniqueName(users.data.toList.map(_.infoUser.name)), "test").toFuture()
  
        f.onFailure{
          case ThrowableWithErrorMsg(data) => println("Failure"+data.asInstanceOf[ErrorResponse].responseText)
          assert(false)
        }
        f
      }
    }
    
    loginFuture
  }
  
  "testLogin" - {
    Future {
      assert(true)
    }
  }
  "testFail" - {
    Future {
      assert(true)
    }
  }
  "normalSuccess" - {
    assert(true)
  }
  "normalFail" - {
    assert(true)
  }
}

tests.runAsync().map {    results =>
  assert(results.toSeq(0).value.isFailure) // root
  assert(results.toSeq(1).value.isFailure) // testSuccess
  assert(results.toSeq(2).value.isFailure) // testFail
  assert(results.toSeq(3).value.isFailure)// normalSuccess
}

}
