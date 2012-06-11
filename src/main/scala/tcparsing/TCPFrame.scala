package tcparsing

import javax.swing._
import event.{DocumentEvent, DocumentListener}
import text.DefaultCaret
import collection.immutable.Map
import swing.Dialog
import java.io._
import java.awt.event._
import java.awt.datatransfer.{StringSelection, DataFlavor}
import java.awt.{GraphicsEnvironment, Toolkit, Font}

object TCPFrame {
  def main(args: Array[String]) {
    new TCPFrame().setVisible(true)
  }
}
/**************************************
 * The TopCoder Parser user interface *
 **************************************/
class TCPFrame extends javax.swing.JFrame {
  
  /* UI elements */
  private val buttonParse = new JButton
  private val buttonClear = new JButton
  private val buttonSaveCode = new JButton
  private val buttonSaveTest = new JButton
  private val buttonToggle = new JButton
  private val checkBoxReturnType = new JCheckBox
  private val comboBoxLanguage = new JComboBox // needs [Object] for Java 7
  private val labelCodePath = new JLabel
  private val labelInput = new JLabel
  private val labelOutput = new JLabel
  private val labelProblem = new JLabel
  private val labelTestPath = new JLabel
  private val scrollPaneInput = new JScrollPane
  private val scrollPaneOutput = new JScrollPane
  private val splitPane = new JSplitPane
  private val textAreaInput = new JTextArea
  private val textAreaOutput = new JTextArea
  private val textFieldCodePath = new JTextField
  private val textFieldProblem = new JTextField
  private val textFieldTestPath = new JTextField
  
  /** The current parser instance, returned from TCParser class */
  var tcp: Option[TCParser] = None 
  /** Initial settings as loaded from text file */
  var settings: Map[String, String] = loadSettings
  /** Whether we are showing (0) Code or (1) Test output */
  var toggleState = 0
  /** Indication of whether the Problem Name has been updated by the user */
  var textFieldProblemIsDirty = false

//  /**
//   * Settings enumeration
//   */
//  abstract class Setting[T](nameInSettingsFile: String, var value: T) {  
//    override def toString = value.toString
//  }
//  case object projectName       extends Setting("projectName", "")
//  case object codePackageName   extends Setting("codePackageName", "")
//  case object testPackageName   extends Setting("testPackageName", "")
//  case object projectsRoot      extends Setting("projectsRoot", "")
//  case object testClassPrefix   extends Setting("testClassPrefix", "")
//  case object testClassPostfix  extends Setting("testClassPostfix", "")
//  case object problemName       extends Setting("problemName", "")
//  case object includeReturnType extends Setting("includeReturnType", false)
  
//  // Settings
//  var projectName = ""
//  var codePackageName = ""
//  var testPackageName = ""
//  var projectsRoot = ""
//  var testClassPrefix = ""
//  var testClassPostfix = ""
//  var problemName = ""
//  var includeReturnType = false  
  
  /** Initialize GUI components - method automatically created in NetBeans */
  initComponents()
  
  /** Return initial settings from text file */
  def loadSettings: Map[String, String] = {
    val settingsDir = System.getProperty("user.home") + "/.TCParser"
    val settingsFile = new File(settingsDir + "/settings.txt")
    if ( ! settingsFile.exists) {
      new File(settingsDir).mkdir()
      copyFile("/settings.txt", settingsFile)
    }
    var s = io.Source.fromFile( settingsFile ).getLines()
       .filter(! _.matches("""\s*//.*"""))  // filter out comment lines
       .map(_.split(":", 2))                // split on first colon only
       .filter(_.size == 2)                 // only use lines which were split
       .map(i => i(0).trim -> i(1).trim)    // turn each Array into a Tuple
       .toMap

    s += "codePackageName" -> s("codePackageName" + s("language").capitalize)
    s += "testPackageName" -> s("testPackageName" + s("language").capitalize)   
    s += getCodePath(s)    
    s += getTestPath(s)
    if (!s.contains("includeProblemStatement")) s += "includeProblemStatement" -> "false"
    s
  }
  
  def getCodePath(s: Map[String, String]) = ("codePath" -> (s("projectsRoot") + s("projectName") + """/src/main/""" + s("language")  
          + "/" + s("codePackageName").replace(".","/") + "/"))
  def getTestPath(s: Map[String, String]) = ("testPath" -> (s("projectsRoot") + s("projectName") + """/src/test/""" + "scala"  // test files are in Scala
          + "/" + s("testPackageName").replace(".","/") + "/"))
  
  /** Copy a file from this Jar or directory to a local File */
  def copyFile(srcName: String, dest: File) {
    val bs = io.Source.fromInputStream(this.getClass.getResourceAsStream(srcName))
    val p = new PrintWriter(dest)
    p.write(bs.mkString)
    p.close()
  }
  
  /** Parse the input text */
  def parse() {
    val lst = textAreaInput.getText.lines.toList
    tcp = trycatch (new TCParser( lst, 
                                  textFieldProblem.getText, 
                                  settings("testPackageName"), 
                                  settings("codePackageName"), 
                                  checkBoxReturnType.isSelected, 
                                  settings("language"),
                                  settings("includeProblemStatement") == "true"
                   ))
    updateOutput()
    textAreaOutput.setCaretPosition(0)
  }
  
  /* Generic utility method to attempt to retrieve a value; if it fails it goes to None 
   * Could be replaced by scala.util.control.Exception.allCatch#opt */
  def trycatch[A](f: => A): Option[A] = {
    try { Some(f) }
    catch { case _ => None }
  }

  /** 
   * Takes a function of TCParser and File, e.g. for when Save buttons pressed 
   * Useful so that we can save the output of different methods on the parser object 
   * */
  def save(filename: String, f:(TCParser, File) => Unit) {
    tcp match {
      case Some(p) => {
        val file = new File(filename)
        if (! file.exists || 
          Dialog.showOptions (message = "File already exists: Overwrite?", 
                              entries = Seq("Do it", "No thx"), initial = 1,
                              messageType = Dialog.Message.Warning) ==  Dialog.Result.Yes)  {
          try f(p, file) 
          catch { case e => 
            Dialog.showMessage(message = "Could not save file. Check the file path is valid.",
                               messageType = Dialog.Message.Warning)
          }
        }
      }
      case None => textAreaOutput.setText("Cannot save: no valid results available")
    }
  }

  /**
   * Update the Code path and Test path text boxes to reflect changes in Problem name field
   */
  def updatePaths() {
    settings += ( "problemName" -> textFieldProblem.getText )
    textFieldCodePath.setText(textFieldCodePath.getText.
        replaceAll("""[\\/][^\\/]*\.""", "/" + settings("problemName") + ".") )
    textFieldTestPath.setText(textFieldTestPath.getText.replaceAll("""[\\/][^\\/]*\.""", "/" + 
        settings("testClassPrefix") + settings("problemName") + settings("testClassPostfix") + "."))   
  }
  
  /** Update toggleState (Code / Test) variable */ 
  def toggle() {
    toggleState = (toggleState + 1) % 2
    labelOutput.setText("Output: " + (if(toggleState == 0) "Test" else "Code"))
  }
  
  /** Update Output (lower) text field with data from parser */
  def updateOutput() {
    val t = tcp match {
      case Some(p) => toggleState match {
        case 0 => p.composeTests
        case 1 => p.composeCodeTemplate
      }
      case None => "No valid results available"
    }
    textAreaOutput.setText(t)
  }
  
  /** Copy text from system clipboard into Input window */
  def pasteDataToInput() {
    val to = Option(Toolkit.getDefaultToolkit.getSystemClipboard.getContents(null))
    to match {
      case Some(t) if t.isDataFlavorSupported(DataFlavor.stringFlavor) => {
        textAreaInput.setText(t.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String])
        textAreaInput.setCaretPosition(0)
      }
      case None => 
    }    
  }
  
  /** Test command to be copied to clipboard for use in SBT */
  def sbtTestString = "~test-only " + settings("testPackageName")+ "." + settings("testClassPrefix") + 
        settings("problemName") + settings("testClassPostfix")

  /**
   * Update output paths itinitally, and when language changes
   */
  def updateOutputPaths() {
    settings += "codePackageName" -> settings("codePackageName" + settings("language").capitalize) //update codePackageName setting
    settings += "testPackageName" -> settings("testPackageName" + settings("language").capitalize) //update testPackageName setting    
    settings += getCodePath(settings) //update codePath setting
    settings += getTestPath(settings) //update testPath setting
    textFieldCodePath.setText(settings("codePath") + settings("problemName") + "." + settings("language"))
    textFieldTestPath.setText(settings("testPath") + settings("testClassPrefix") + 
       settings("problemName") + settings("testClassPostfix") + ".scala") //.scala for both, since test file is in Scala
  }
  
  
  /**
   * Initialize GUI components. Modified from automated translation from Java code auto-generated
   * in NetBeans GUI builder
   */
  private def initComponents() {
    val monoFont = if (GraphicsEnvironment.getLocalGraphicsEnvironment.getAvailableFontFamilyNames
                      .contains("Consolas")) "Consolas" else "Monospaced"

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    
    setTitle("TC Scala Parser")
    labelInput.setText("Input")
    
    splitPane.setDividerLocation(250)
    splitPane.setDividerSize(10)
    splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT)
    splitPane.setResizeWeight(0.5)
    
    textAreaInput.setLineWrap(true);
    textAreaInput.setWrapStyleWord(true);
    textAreaInput.setText("Right-click to paste text...")
    textAreaInput.addMouseListener(new MouseAdapter(){
      override def mouseClicked(e: MouseEvent) {
        if(e.getButton == MouseEvent.BUTTON3) {
          pasteDataToInput()
        }
      }
    })
    
    val textAreaFont = new Font(monoFont, Font.PLAIN, 12)

    List(textAreaInput, textAreaOutput) foreach { ta =>
      // Stop scrolling when text inserted
      //ta.getCaret.asInstanceOf[DefaultCaret].setUpdatePolicy(DefaultCaret.NEVER_UPDATE) 
      //Set font
      ta.setFont(textAreaFont)
    }
    
    scrollPaneInput.setViewportView(textAreaInput)
    splitPane.setTopComponent(scrollPaneInput)

    scrollPaneOutput.setViewportView(textAreaOutput)
    splitPane.setRightComponent(scrollPaneOutput)

    val textFieldFont = new Font(monoFont, Font.PLAIN, 12)
    textFieldCodePath.setFont(textFieldFont)
    textFieldTestPath.setFont(textFieldFont)
    
    textFieldProblem.setText(settings("problemName"))
    textFieldProblem.setFont(new Font(monoFont, Font.BOLD, 18))
    textFieldProblem.setMinimumSize(new java.awt.Dimension(120, 28))
    // Listener that marks Problem field as dirty if text altered
    textFieldProblem.getDocument.addDocumentListener(new DocumentListener {
      def removeUpdate(e: DocumentEvent) { updatePaths(); textFieldProblemIsDirty = true }
      def insertUpdate(e: DocumentEvent) { updatePaths(); textFieldProblemIsDirty = true }
      def changedUpdate(e: DocumentEvent) {}
    })
    
    // Set initial directory path text
    updateOutputPaths()
    
    labelOutput.setText("Output: Test")
    labelProblem.setText("Problem name:")
    labelTestPath.setText("Test path:")
    labelCodePath.setText("Code path:")
    
    buttonSaveTest.setText("Save test")
    buttonSaveTest.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        // sends to the `save` method the Test Path text and an action carry out on the parser
        save(textFieldTestPath.getText, (p, file) => p.writeTestsFile(file))
        // copy SBT test string to clipboard
        Toolkit.getDefaultToolkit.getSystemClipboard.setContents(
          new StringSelection(sbtTestString), null)
      }
    })
    
    buttonSaveCode.setText("Save code")
    buttonSaveCode.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        save(textFieldCodePath.getText, (p, file) => p.writeCodeTemplateFile(file))
      }
    })
    
    buttonToggle.setText("Toggle code / test")
    buttonToggle.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        toggle()
        updateOutput()
      }
    })
    
    buttonParse.setText("Parse!")
    buttonParse.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        if (textFieldProblemIsDirty ||
          Dialog.showOptions(message = "Problem name has not changed.\nParse anyway?", 
                             entries = Seq("Yes", "No"), initial = 1) == Dialog.Result.Yes) { 
          parse()
          textFieldProblemIsDirty = false
        }
      }
    })
    
    buttonClear.setText("Clear")
    buttonClear.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        textAreaInput.setText("")
      }
    })
    checkBoxReturnType.setText("Include return type")
    checkBoxReturnType.setSelected(settings.getOrElse("includeReturnType", "false").toLowerCase == "true")
    
    comboBoxLanguage.setModel(new javax.swing.DefaultComboBoxModel(Array[Object]("Scala", "Java")))
    comboBoxLanguage.setToolTipText("")
    comboBoxLanguage.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        settings += ("language" -> comboBoxLanguage.getSelectedItem.toString.toLowerCase)
        settings += getCodePath(settings)
        settings += getTestPath(settings)
        updateOutputPaths()
//        println("Debug: language changed to: " + settings("language"))
      }
    })
    
    // Layout code generated in NetBeans
    val layout: GroupLayout = new GroupLayout(getContentPane)

    getContentPane.setLayout(layout)
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup.addContainerGap.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(splitPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MaxValue).addComponent(labelInput, javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(labelCodePath).addComponent(labelTestPath)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(textFieldCodePath, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MaxValue).addComponent(textFieldTestPath, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MaxValue))).addGroup(layout.createSequentialGroup.addComponent(labelOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(buttonToggle).addComponent(checkBoxReturnType)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(buttonClear, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MaxValue).addComponent(comboBoxLanguage, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MaxValue)).addGap(18, 18, 18).addComponent(labelProblem).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(textFieldProblem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MaxValue))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(buttonParse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MaxValue).addComponent(buttonSaveTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MaxValue).addComponent(buttonSaveCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MaxValue)))).addContainerGap))
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup.addContainerGap.addComponent(labelInput).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MaxValue).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(buttonToggle).addComponent(buttonClear).addComponent(labelProblem).addComponent(labelOutput)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(checkBoxReturnType).addComponent(comboBoxLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(buttonParse, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(textFieldProblem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(3, 3, 3).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(labelCodePath).addComponent(textFieldCodePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(buttonSaveCode)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(labelTestPath).addComponent(textFieldTestPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(buttonSaveTest)).addContainerGap))
    pack()
  }

}