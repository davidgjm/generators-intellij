package com.davidgjm.idea.plugins.actions;

import com.davidgjm.idea.plugins.AbstractGeneratorAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenerateToString extends AbstractGeneratorAction {
    public GenerateToString() {
        super("Generate Java8 toString", "Select fields for toString");
    }

    public void generate(@NotNull final PsiClass psiClass, @NotNull final List<PsiField> fields) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
                generateToString(psiClass, fields);
            }
        }.execute();
    }

    private void generateToString(PsiClass psiClass, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("@Override\n");
        builder.append("public String toString() { \n");
        builder.append("return ")
                .append("new java.util.StringJoiner(\", \", " +
                        "this.getClass().getSimpleName() + \"[\"," +
                        "\"]\")\n");
/*        String fieldValues = fields.stream()
                .map(f -> String.format(".add(\"%s = \" + %s)", f.getName(), f.getName()))
                .collect(Collectors.joining("\n"));*/

        for (PsiField f : fields) {
            builder.append(String.format(".add(\"%s = \" + %s)\n", f.getName(), f.getName()));
        }

        builder.append(".toString();\n}");
        setNewMethod(psiClass, builder.toString(), "toString");
    }
}
