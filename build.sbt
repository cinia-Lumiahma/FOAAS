import java.io.File

import com.typesafe.config.ConfigFactory

name := "FOAAS"
 
version := "1.0"

val initConfig = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()

lazy val `foaas` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Flyway" at "https://flywaydb.org/repo"

scalaVersion := "2.12.2"

unmanagedResourceDirectories in Test +=  baseDirectory ( _ /"target/web/public/test" ).value

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-target:jvm-1.8",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import",
  "-Xlint"
)

scalacOptions ++= Seq("-Xmax-classfile-name","100")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

val databaseDeps = Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.play" %% "play-slick" % "3.0.2",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.3.0",
  "mysql" % "mysql-connector-java" % "8.0.8-dmr",
  "org.flywaydb" %% "flyway-play" % "4.0.0"
)

libraryDependencies ++=
  databaseDeps ++
    Seq(
      ws,
      jdbc,
      ehcache,
      specs2 % Test,
      guice
      //"io.jsonwebtoken" % "jjwt" % "0.6.0"
    )

parallelExecution in Test := false

/*flywayLocations               := Seq("filesystem:conf/db/migration/default")
flywayUrl                     := initConfig.getString("slick.dbs.default.db.url")
flywayUser                    := initConfig.getString("slick.dbs.default.db.user")
flywayPassword                := initConfig.getString("slick.dbs.default.db.password")*/

// Migrate "identity_default" db by calling a callback function after the first migration
//flywayCallbacks               := Seq("tools.SecondMigration")