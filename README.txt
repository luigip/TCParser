Summary: 
A parser for taking Topcoder problem statements and producing test scripts in Scala, based on the examples in the question. It effectively turns the vast repository of Topcoder problems into useful problem set for learning to program Scala, and is also useful as an automation of the test process for use in actual competitions (using Java). The appliction operates via a Swing GUI and can produce test scripts and code templates for both Scala and Java code.

Setup:
Start by creating a new project in sbt for your solutions. You must include a dependency on ScalaTest, currently version 1.x.x (version 2 not supported since the Matchers class has changed). [A shell project will be uploaded to GitHub at some point, to make this easier.]

The first time the application runs, it should add a file ~/.TCParser/settings.txt. Edit this file with your project name, directory, desired destination packages, and other preferences if desired. (Lines beginning "//" are ignored; as is whitespace: the GUI looks for lines containing a key and value separated by a colon.)

The application does not currently create destination folders, so ensure folders for the package names you give exist.

The appliction can be run from sbt with "sbt run".

I normally run it from a jar-file produced in sbt with "publish-local". You can make a desktop shortcut to the jar following this example:
"C:\Program Files\Java\jre1.8.0_91\bin\javaw.exe" -cp "C:\Program Files\scala-2.10.3\lib\*";"C:/Users/Rhys/.ivy2/local/default/tcparser_2.10\0.1-SNAPSHOT\jars\tcparser_2.10.jar" tcparsing.TCPFrame

Usage:
From the Topcoder Arena, enter a practice area.
Enter a Problem name in the TCParser GUI. The suggested convention is, for example, for SRM 144 Div 1 problem 2: SRM144_1_2, i.e. acronym, number, underscore, division, underscore, problem number. Alternatively, the class name given in the problem statement can be used, but this make it more difficult to identify problems already completed.

Select a problem from the drop-down list. The problem statement window will be shown. Ensure the Java language is selected.
Click on the text, select it using Ctrl-A, and copy to the clipboard using Ctrl-C.
Switch to the TCParser GUI and right-click in the Input window. This will paste all text from the clipboard.
Click "Parse!"
Click "Save code"
Click "Save test"
An sbt command such as "~test-only topcoder.test.scala.SRM144_1_2_TEST" will be automatically copied to the clipboard. Switch to sbt, paste and run this command, to continuously run the tests. Clearly, it should not pass initially.
Open the saved code template, and code your solution. Save, and check in sbt whether they have passed.

Comments:
I currently use this with IntelliJ IDEA, with the sbt plugin. The project tree on the left hand side of the window makes it easy to quickly open the generated code template.

The parser has been extremely reliable so far and I have not come across any examples that do not work.

This has not been tested on *nix systems, although it should work.