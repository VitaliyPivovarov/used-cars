name := "used_cars"
 
version := "1.0" 
      
lazy val `used_cars` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( javaJdbc , cache , javaWs )

libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies ++= Seq(evolutions, jdbc)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      