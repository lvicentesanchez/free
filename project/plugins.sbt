// Comment to get more information during initialization
//
logLevel := Level.Warn

// Resolvers
//

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Softprops Maven" at "http://dl.bintray.com/content/softprops/maven"

addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.5.6")
