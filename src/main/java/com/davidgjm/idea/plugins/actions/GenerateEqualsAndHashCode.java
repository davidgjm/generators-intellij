package com.davidgjm.idea.plugins.actions;

import com.davidgjm.idea.plugins.AbstractGeneratorAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenerateEqualsAndHashCode extends AbstractGeneratorAction {
    public GenerateEqualsAndHashCode() {
        super("Generate equals and hashCode", "Select fields for equals and hashCode");
    }

    public void generate(@NotNull final PsiClass psiClass, @NotNull final List<PsiField> fields) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
                generateEquals(psiClass, fields);
                generateHashCode(psiClass, fields);
            }
        }.execute();
    }

    private void generateEquals(PsiClass psiClass, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("@Override\n");
        builder.append("public boolean equals(Object o) { \n");
        builder.append("if (this == o) return true;\n");
        builder.append("if (o == null || getClass() != o.getClass()) return false;\n\n");
        builder.append(psiClass.getName()).append(" that = (").append(psiClass.getName()).append(") o;\n\n");

        builder.append("return ");
        for (int i = 0; i < fields.size(); i++) {
            PsiField field = fields.get(i);
            builder.append("java.util.Objects.equals(this.").append(field.getName()).append(", that.");
            builder.append(field.getName()).append(")");
            if (i < fields.size() - 1) {
                builder.append(" &&\n");
            }
        }
        builder.append(";\n}");
        setNewMethod(psiClass, builder.toString(), "equals");
    }

    private void generateHashCode(PsiClass psiClass, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("@Override\n");
        builder.append("public int hashCode() { \n");
        builder.append("return ")
                .append("java.util.Objects.hash(");
        for (int i = 0; i < fields.size(); i++) {
            PsiField field = fields.get(i);
            builder.append(field.getName());
            if (i < fields.size() - 1) {
                builder.append(",");
                if (fields.size() > 5 && i > 0 && i % 5 == 0) {
                    builder.append("\n");
                }
            }
        }
        builder.append(");\n}");
        setNewMethod(psiClass, builder.toString(), "hashCode");
    }
}
