package tcp;

public class TCPFrame extends javax.swing.JFrame {

    public TCPFrame() {
        initComponents();

    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        labelInput = new javax.swing.JLabel();
        splitPane = new javax.swing.JSplitPane();
        scrollPaneInput = new javax.swing.JScrollPane();
        textAreaInput = new javax.swing.JTextArea();
        scrollPaneOutput = new javax.swing.JScrollPane();
        textAreaOutput = new javax.swing.JTextArea();
        labelOutput = new javax.swing.JLabel();
        textFieldProblem = new javax.swing.JTextField();
        labelProblem = new javax.swing.JLabel();
        labelTestPath = new javax.swing.JLabel();
        textFieldTestPath = new javax.swing.JTextField();
        textFieldCodePath = new javax.swing.JTextField();
        labelCodePath = new javax.swing.JLabel();
        buttonSaveTest = new javax.swing.JButton();
        buttonSaveCode = new javax.swing.JButton();
        buttonToggle = new javax.swing.JButton();
        buttonParse = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        checkBoxReturnType = new javax.swing.JCheckBox();
        comboBoxLanguage = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TC Scala Parser");

        labelInput.setText("Input");
        labelInput.setName(""); // NOI18N

        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(10);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        textAreaInput.setColumns(20);
        textAreaInput.setRows(5);
        scrollPaneInput.setViewportView(textAreaInput);

        splitPane.setTopComponent(scrollPaneInput);

        textAreaOutput.setColumns(20);
        textAreaOutput.setRows(5);
        scrollPaneOutput.setViewportView(textAreaOutput);

        splitPane.setRightComponent(scrollPaneOutput);

        labelOutput.setText("Output: Test");

        textFieldProblem.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        textFieldProblem.setMinimumSize(new java.awt.Dimension(120, 28));

        labelProblem.setText("Problem name:");

        labelTestPath.setText("Test path:");

        labelCodePath.setText("Code path:");

        buttonSaveTest.setText("Save test");

        buttonSaveCode.setText("Save code");

        buttonToggle.setText("Toggle code / test");

        buttonParse.setText("Parse!");

        buttonClear.setText("Clear");

        checkBoxReturnType.setText("Include return type");

        comboBoxLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Scala", "Java" }));
        comboBoxLanguage.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(splitPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                    .addComponent(labelInput, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelCodePath)
                                    .addComponent(labelTestPath))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFieldCodePath, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
                                    .addComponent(textFieldTestPath, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buttonToggle)
                                    .addComponent(checkBoxReturnType))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(buttonClear, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                    .addComponent(comboBoxLanguage, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(labelProblem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldProblem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(buttonParse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonSaveTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonSaveCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonToggle)
                            .addComponent(buttonClear)
                            .addComponent(labelProblem)
                            .addComponent(labelOutput))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkBoxReturnType)
                            .addComponent(comboBoxLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(buttonParse, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldProblem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCodePath)
                    .addComponent(textFieldCodePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSaveCode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTestPath)
                    .addComponent(textFieldTestPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSaveTest))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                new TCPFrame().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonParse;
    private javax.swing.JButton buttonSaveCode;
    private javax.swing.JButton buttonSaveTest;
    private javax.swing.JButton buttonToggle;
    private javax.swing.JCheckBox checkBoxReturnType;
    private javax.swing.JComboBox comboBoxLanguage;
    private javax.swing.JLabel labelCodePath;
    private javax.swing.JLabel labelInput;
    private javax.swing.JLabel labelOutput;
    private javax.swing.JLabel labelProblem;
    private javax.swing.JLabel labelTestPath;
    private javax.swing.JScrollPane scrollPaneInput;
    private javax.swing.JScrollPane scrollPaneOutput;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextArea textAreaInput;
    private javax.swing.JTextArea textAreaOutput;
    private javax.swing.JTextField textFieldCodePath;
    private javax.swing.JTextField textFieldProblem;
    private javax.swing.JTextField textFieldTestPath;
    // End of variables declaration
}
