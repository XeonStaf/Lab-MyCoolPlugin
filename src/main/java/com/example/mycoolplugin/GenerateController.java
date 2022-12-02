package com.example.mycoolplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.util.TextRange;
import jnr.ffi.annotations.In;

public class GenerateController extends EditorAction {

    public GenerateController() {
        this(new MyHandler());
    }

    protected GenerateController(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    private static class MyHandler extends EditorWriteActionHandler {
        private MyHandler() {
        }

        @Override
        public void executeWriteAction(Editor editor, DataContext dataContext) {
            SelectionModel selectionModel = editor.getSelectionModel();
            var text = editor.getDocument().getText();
            var requestMapping = "RequestMapping";
            var requestMappingIndex = text.indexOf(requestMapping);
            var classIndex = text.lastIndexOf("class");
            var startApi = "";
            if (classIndex > requestMappingIndex) {
                startApi = text.substring(requestMappingIndex + requestMapping.length() + 2, requestMappingIndex + 200);
                startApi = startApi.substring(0, startApi.indexOf(')') - 1);
            }
            InputData dialog = new InputData(startApi);
            dialog.pack();
            dialog.setVisible(true);
            if (dialog.isOk()) {
                var start = selectionModel.getSelectionStart();
                var code = dialog.GenerateCode();
                editor.getDocument().replaceString(start, selectionModel.getSelectionEnd(), code);
                selectionModel.setSelection(start, code.length() + 22);
            }


        }
    }


}

