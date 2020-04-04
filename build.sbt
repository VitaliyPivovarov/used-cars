name := "used_cars"
 
version := "1.0" 
      
lazy val `used_cars` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( javaJdbc , cache , javaWs )

libraryDependencies += "com.h2database" % "h2" % "1.4.200"
libraryDependencies += "org.mybatis" % "mybatis" % "3.5.4"
libraryDependencies += "org.mybatis" % "mybatis-guice" % "3.10"
libraryDependencies += "org.modelmapper" % "modelmapper" % "2.3.7"

libraryDependencies ++= Seq(
  jdbc,
  evolutions,
  "org.postgresql" % "postgresql" % "42.1.2"
//"org.projectlombok" % "lombok" % "1.12.6"
)
unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      