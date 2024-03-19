package org.example.pymergesameif

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import com.jetbrains.python.psi.*

class MergeSameIfQuickFix(val first: SmartPsiElementPointer<PyIfStatement>, val second: SmartPsiElementPointer<PyIfStatement>) : LocalQuickFix {
    override fun getFamilyName(): String {
        return "Merge same if"
    }

    override fun applyFix(
        p0: Project,
        p1: ProblemDescriptor,
    ) {
        val newCondition =
            PyElementGenerator.getInstance(p0).createExpressionFromText(
                LanguageLevel.forElement(checkNotNull(first.element)),
                "${first.element?.ifPart?.condition?.text} or ${second.element?.ifPart?.condition?.text}",
            )
        first.element?.ifPart?.condition?.replace(newCondition)
        second.element?.delete()
    }
}
