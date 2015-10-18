scala-js-BaasBox
==============

Scala.js typed facade for [baasbox.js](http://www.baasbox.com/). 

Built for Scala.js 0.6.x and Scala 2.11. 

See `index.html` for example use (you'll need to checkout and run `fullOptJS` to build the example project first. Note that the example project uses a library-, rather than local project-, dependency).


All the function defined in the js library are covered.

**Extra**    
contains function to login using facebook / google.

```
   def loginGoogle( ..)  
   def loginFacebook( ..)
```    


### Return of methods:    
You can use the default done(), fail() or importing BaasBoxTools._ using an implicit conversion to Future.
like:
```
BaasBox.log("username", "password".map{
  _ => "logged"
}
```


##DAO
 use js.object, you can use this template to save and load object:
 ```
case class email( email: String);
  
implicit def writerEmailToJsObject( ourinstance: email): js.Object ={
   JSON.parse(write[email](ourinstance)).asInstanceOf[js.Object]
}

implicit def readerEmailToJsObject( ourjs:  GenericResponse[js.Object]): email ={
   read[email](JSON.stringify(ourjs.data))
}
 ```  
 
## Commands:

compile : ```sbt fastOptJS```   
test: ```sbt test```    
publish: ```sbt publish-local```   , generate dependency: ```"baasboxapi" %%% "baasboxapi" % "0.1-SNAPSHOT" ```



