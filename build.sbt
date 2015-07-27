// put this at the top of the file
import scalariform.formatter.preferences._

// Resolvers
resolvers ++= Seq(
  "scalaz bintray" at "https://dl.bintray.com/scalaz/releases",
  "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

// Dependencies

val testDependencies = Seq (
  "com.typesafe.slick" %% "slick-testkit"     % "3.0.0" % "test"
)

val rootDependencies = Seq(
  "com.chuusai"        %% "shapeless"         % "2.2.5",
  "com.h2database"     %  "h2"                % "1.4.180",
  "com.typesafe.slick" %% "slick"             % "3.0.0",
  "org.scalaz"         %% "scalaz-concurrent" % "7.1.3",
  "org.scalaz"         %% "scalaz-core"       % "7.1.3"
)

val dependencies =
  rootDependencies ++
  testDependencies

// Settings
//
val compileSettings = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  //"-language:existentials",
  //"-language:experimental.macros",
  //"-language:higherKinds",
  //"-language:implicitConversions",
  "-unchecked",
  //"-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  //"-Ywarn-all",
  //"-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

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
  net.virtualvoid.sbt.graph.Plugin.graphSettings ++
  scalariformSettings

val settings = Seq(
  name := "freez",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.7",
  libraryDependencies ++= dependencies,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption,
  scalacOptions := compileSettings,
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
