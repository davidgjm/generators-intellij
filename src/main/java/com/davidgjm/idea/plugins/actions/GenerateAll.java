package com.davidgjm.idea.plugins.actions;

import com.davidgjm.idea.plugins.AbstractGeneratorAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenerateAll extends AbstractGeneratorAction {
    private GenerateEqualsAndHashCode equalsAndHashCodeGenerator;
    private GenerateToString toStringGenerator;

    public GenerateAll() {
        super("Generate equals, hashCode and toString", "Select fields");
        equalsAndHashCodeGenerator=new GenerateEqualsAndHashCode();
        toStringGenerator = new GenerateToString();
    }

    @Override
    public void generate(@NotNull PsiClass psiClass, @NotNull List<PsiField> fields) {
        equalsAndHashCodeGenerator.generate(psiClass, fields);
        toStringGenerator.generate(psiClass, fields);
    }
}
