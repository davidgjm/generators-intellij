package com.davidgjm.idea.plugins.ui;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GenerateDialog extends DialogWrapper {
    private final List<PsiField> fields = new ArrayList<PsiField>();
    private final LabeledComponent<JPanel> contentPane;

    public GenerateDialog(PsiClass psiClass, String dialogTitle) {
        super(psiClass.getProject());
        setTitle(dialogTitle);

        JPanel panel = createFieldListPanel(psiClass);
        contentPane = LabeledComponent.create(panel, dialogTitle + " (Warning: existing method(s) will be replaced):");
        init();
    }

    private void initPsiFields(@NotNull PsiClass psiClass) {
        if (!fields.isEmpty()) return;
        fields.addAll(Arrays.asList(psiClass.getAllFields()));
        fields.sort(new Comparator<PsiField>() {
            @Override
            public int compare(PsiField o1, PsiField o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private JPanel createFieldListPanel(PsiClass psiClass) {
        //collecting fields from the incoming class
        initPsiFields(psiClass);

        JList<PsiField> fieldList = new JList<PsiField>(new CollectionListModel<PsiField>(fields));
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        return decorator.createPanel();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    public List<PsiField> getFields() {
        return fields;
    }
}
