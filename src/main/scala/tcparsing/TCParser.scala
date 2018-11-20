package tcparsing

import collection.mutable.ListBuffer
import java.io.{PrintWriter, File}

// Idea: use a Settings class instead of using hardcoded default values
class TCParser(val input                   : List[String],
               inputName               : String, 
               testPackageName         : String, 
               codePackageName         : String, 
               useReturnType           : Boolean       = false,
               language                : String        = "scala",
               includeProblemStatement : Boolean       = false,
               testClassPrefix         : String        = "",
               testClassPostfix        : String        = "_TEST"
              ) {
  /*private*/ val inputNoBlankLines = input.filterNot{_.matches("""[\s]*""")}
  // Pairs up input lines, for easy searching on heading -> value in next line
  private val pairs = inputNoBlankLines.sliding(2).toList
  // Utility method to return the String value of the line following the heading passed
  private def getVal(s: String) = pairs.find(_.head == s).get(1)
  private val lineEnd = "\r\n"

  // The metadata of the problem class retrieved from the input (should perhaps have this as a separate class?)
  val method = getVal("Method:")
  // The parameter types and names
  val (argTypes, argNames) = {
    // Splits on ", " or " " or "(" or ")", drops the type and method name, groups the param type and name, and extracts lists of param types and names
    val List(t, n) = getVal("Method signature:").split(", |[ ()]").drop(2).grouped(2).toList.transpose
    (t map translateType, n)
  }
  val returnType = translateType(getVal("Returns:"))
  // The examples section, dropping the title and the last line (copyright message)
  private val xs = inputNoBlankLines.dropWhile(_!="Examples").drop(1).dropRight(1)
  // Split the examples where line is a number followed by ")"
  val examples = groupPrefix(xs)(_ matches """\d+\)""")
  // Finds a tolerance, if there is one
  val tolerance = """\d[Ee]-\d""".r findFirstIn inputNoBlankLines.mkString
  // If we're using Java, we need to create an instance "I" in our test script. Can't use a static method, because this is what TopCoder expects.
  // (Might be better to change this to put an "import <instance>.methodName" in the script?)
  // However in Scala we put our code in an object, so we just import the method and don't need an instance
  val instance = if (language == "java") "I." else ""

  //  Utility method used to split examples, preserving first line (not considering it a separator) - otherwise like split
  private def groupPrefix[T](xs: List[T])(p: T => Boolean): List[List[T]] = xs match {
   case List() => List()
   case x :: xs1 =>
     val (ys, zs) = xs1 span (!p(_))
     (x :: ys) :: groupPrefix(zs)(p)
  }
  
  // Add method to String to convert Java-style {...} into Scala-style Array(...)
  implicit class strTo(s: String) {
    def withScalaArrays = s.replaceAll("\\{", "Array\\(").replaceAll("}", ")")
  }

  def testCases = for {
    x <- examples
    n = x.head.init.toInt
    args = parseArgs(x)
    result = parseResults(x)
    optionalNewLine = if(args.contains("Array(")) lineEnd + "    " else ""
    (eqText, tolText) = if(tolerance.isEmpty) ("equal", "")
    else ("be", " plusOrMinus " + tolerance.get)
  } yield s"""  test(\"Case $n\") { $instance$method($args$optionalNewLine) should $eqText ($result$tolText) }$lineEnd"""

  // Construct the test script
  def composeTests = {
    val lb = new ListBuffer[String]
    // The
    val testClassName = testClassPrefix + inputName + testClassPostfix

    lb += "package " + testPackageName
    lb += ""
    lb += "import org.scalatest.FunSuite"
    lb += "import org.scalatest.matchers.ShouldMatchers"
    lb += s"import $codePackageName.$inputName${if(language == "scala") "." + method else ""}"
    lb += ""
    lb += s"class $testClassName extends FunSuite with ShouldMatchers {"
    if (language == "java"){
      lb += ""
      lb += "  val I = new " + inputName
    }
    lb += ""
    lb ++= testCases
    lb += "}"

    lb.mkString(lineEnd)
  }
  // parse results after "Returns"
  def parseResults(x: List[String]) = {
    
    val res = x.filter(_.startsWith("Returns: ")).head.substring(9).withScalaArrays
    if (! res.isEmpty) {
      if (returnType == "Long" || returnType == "Array[Long]") longsAddL(res)
      else res
    }
    // for cases where result is an Array displayed over multiple subsequent lines:
    else {
      val (a,b) = x.dropWhile(! _.startsWith("Returns: ")).drop(1).span(!_.endsWith("}"))
      val arrayResult = s"$lineEnd    ${(a :+ b.head).mkString(s"$lineEnd         ")}".withScalaArrays
      if (returnType == "Array[Long]") longsAddL(arrayResult)
      else arrayResult
    }
  }
  
  // reads a single "example" and returns the text to pass to method on test script
  def parseArgs(x: List[String]): String = {
    
    val argLines = x.drop(1).takeWhile(!_.startsWith("Returns:")).map(_.withScalaArrays)

    def join(y: List[String], argNumber: Int, acc: String = ""): String = {
      if (y == Nil) acc
      // Arrays go on new line
      else {
        val isLongType = Seq("Array[Long]", "Long") contains argTypes(argNumber)
        if (y.head startsWith "Array(") {
          val(s1, s2) = y span {! _.endsWith(")")}
          val pre = if(acc == "") "\r\n    " else ",\r\n    "
          val t = (s1 :+ s2.head).mkString("\r\n         ")
          val tL = if (isLongType) longsAddL(t) else t
          join(s2.tail, argNumber + 1, acc + pre + tL)
        }
        // Next arg is not array: continue on same line
        else {
          val t = (if (acc == "") "" else ", ") + y.head
          val tL = if (isLongType) longsAddL(t) else t
          join(y.tail, argNumber + 1, acc + tL)
        }
      }
    }
    join(argLines, 0)
  }
  
  def composeCodeTemplate = language match {
    case "java" => composeCodeTemplateJava
    case "scala" => composeCodeTemplateScala
    case _ => ""
  }  
  
  // Code file Scala
  def composeCodeTemplateScala = {
    val lb = new ListBuffer[String]
    val returnTypeString = if (useReturnType) ": " + returnType else ""

    lb += "package " + codePackageName
    lb += ""
    lb += "object " + inputName + " {"
    lb += "  def " + method + ((argNames, argTypes).zipped.map(_ + ": " + _).
          mkString("(", ", ", ")")) + returnTypeString + " = {"
    lb += "    "
    lb += "  }"
    lb += "}"
    if (includeProblemStatement) {
      lb += ""
      lb += "/*"
      lb ++= input
      lb += "*/"
    }
    
    lb.mkString("\r\n")
  }
  
  def composeCodeTemplateJava = {
    val lb = new ListBuffer[String]

    lb += "package " + codePackageName + ";"
    lb += ""
    lb += "public class " + inputName + " {"
    lb += "    public " + getVal("Method signature:") + " {"
    lb += "        "
//    lb += "        return ;"
    lb += "    }"
    lb += "}"
    if (includeProblemStatement) {
      lb += ""
      lb += "/*"
      lb ++= input
      lb += "*/"
    }
    
    lb.mkString("\r\n")    
  }

  // Recursively get the Scala version of Array notation, and translate int to Int etc.
  def translateType(s: String): String = {
    if (s.endsWith("[]")) "Array[" + translateType(s.dropRight(2)) + "]"
    else s.capitalize
  }

  def longsAddL(s: String) = "[0-9]{10,}".r.replaceAllIn(s, _.matched + 'L')
  
  def writeTestsFile(f: File) {
    val p = new PrintWriter(f)
    try { p.print(composeTests) } finally { p.close() }
  }
  
  def writeCodeTemplateFile(f: File) {
    val p = new PrintWriter(f)
    try { p.print(composeCodeTemplate) } finally { p.close() }
  }  

}

object TCParser {
  val defaultPath = "src/test/resources/"
 
  def fromFile(inputName: String, 
               path: String            = defaultPath, 
               testPackageName: String = "topcoder.test", 
               codePackageName: String = "topcoder",
               language: String        = "scala"
              ): TCParser = {
    // Load input and filter out hidden non-ASCII characters, as the TopCoder Java app has a tendency to insert them
    val input = io.Source.fromFile(path + inputName + ".txt")(io.Codec("UTF-8")).getLines()
      .map(_.replaceAll("""^[^\p{ASCII}]*$""", "")).toList
    new TCParser(input, inputName, testPackageName, codePackageName, false, language)
  }

}