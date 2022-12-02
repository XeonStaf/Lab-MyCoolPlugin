package com.example.mycoolplugin;

import javax.swing.*;
import java.awt.event.*;

public class InputData extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel FunctionName;
    private JTextField FunctionNameData;
    private JTextField PathData;
    private JTextField DescriptionData;
    private JCheckBox needAuthorizeCheckBox;
    private JComboBox HtmlTypes;
    private JList ReturnCodsData;
    private JCheckBox onlyForSwaggerCheckBox;
    private JLabel PathBefore;

    private Boolean isOkPressed = false;

    public InputData(String startApi) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        PathBefore.setText(startApi);
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        isOkPressed = true;
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        InputData dialog = new InputData("/api/v1");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public String GenerateCode() {
        var result = "@Operation(summary = \"" + this.DescriptionData.getText() +"\")\n";

        StringBuilder resultCodes = new StringBuilder("\t@ApiResponses(value = {");

        var selectedCodes = this.ReturnCodsData.getSelectedValuesList();
        for (Object selectedCode : selectedCodes) {
            var elem = "\t\t@ApiResponse(responseCode = \"" + selectedCode + "\", description = \" /*todo: describe */ \",\n" +
                    "\t\t\tcontent = { @Content(mediaType = \"application/json\",\n" +
                    "\t\t\tschema = @Schema(implementation = /*todo: Error Class.class */)) })";
            resultCodes.append("\n").append(elem).append(",\n");
        }
        resultCodes.append("\t})\n");
        result += resultCodes.toString();
        if (onlyForSwaggerCheckBox.isSelected())
            return result;
        String annotation = "";
        switch(this.HtmlTypes.getSelectedIndex()) {
            case 0: annotation = "@GetMapping(";
                break;
            case 1: annotation = "@PostMapping(";
                break;
            case 2: annotation = "@PutMapping(";
                break;
            case 3: annotation = "@DeleteMapping(";
                break;
            case 4: annotation = "@PatchMapping(";
                break;
        };

        annotation += "\"" + this.PathData.getText() + "\") \n";

        result +=  "\t" + annotation +
                   "\t@ResponseBody\n";

        if (this.needAuthorizeCheckBox.isSelected())
            result += "\tpublic ResponseEntity<?> " + this.FunctionNameData.getText() + " ( Authentication authentication /*, todo: PARAMS*/) {";
        else
            result += "\tpublic ResponseEntity<?> " + this.FunctionNameData.getText() + " ( /*todo: PARAMS*/) {";

        result += "\n\t\t//todo: your logic here\n" +
                "\t\treturn ResponseEntity.ok().body(); \n" +
                "\t}";

        return result;
    }

    public Boolean isOk() {
        return this.isOkPressed;
    }

}
