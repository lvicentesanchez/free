import sbtassembly.Plugin.AssemblyKeys._

// put this at the top of the file
import scalariform.formatter.preferences._

// Resolvers
resolvers ++= Seq(
  "scalaz bintray" at "https://dl.bintray.com/scalaz/releases",
  "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

// Dependencies

val testDependencies = Seq (
  "com.typesafe.slick" %% "slick-testkit" % "2.1.0-RC2" % "test"
)

val rootDependencies = Seq(
  "com.chuusai"        %% "shapeless"   % "2.0.0",
  "com.h2database"     %  "h2"          % "1.4.180",
  "com.typesafe.slick" %% "slick"       % "2.1.0-RC2",
  "org.scalaz"         %% "scalaz-core" % "7.1.0-RC2"
)

val dependencies =
  rootDependencies ++
  testDependencies

// Settings
//
val forkedJvmOption = Seq(
  "-server",
  "-Dfile.encoding=UTF8",
  "-Duser.timezone=GMT",
  "-Xss1m",
  "-Xms2048m",
  "-Xmx2048m",
  "-XX:+CMSClassUnloadingEnabled",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+DoEscapeAnalysis",
  "-XX:+UseConcMarkSweepGC",
  "-XX:+UseParNewGC",
  "-XX:+UseCodeCacheFlushing",
  "-XX:+UseCompressedOops"
)

val formatting =
  FormattingPreferences()
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, false)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, false)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, false)
    .setPreference(PreserveDanglingCloseParenthesis, true)
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesWithinPatternBinders, true)

val pluginsSettings =
  assemblySettings ++
  net.virtualvoid.sbt.graph.Plugin.graphSettings ++
  org.scalastyle.sbt.ScalastylePlugin.Settings ++
  scalariformSettings

val settings = Seq(
  name := "freez",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.2",
  libraryDependencies ++= dependencies,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption,
  scalacOptions ++= Seq("-target:jvm-1.6", "-Xlog-reflective-calls", "-Ywarn-adapted-args", "-Yresolve-term-conflict:package", "-feature"),
  // formatting
  //
  ScalariformKeys.preferences := formatting
)

lazy val main =
  project
    .in(file("."))
    .settings(
      pluginsSettings ++ settings:_*
    )
