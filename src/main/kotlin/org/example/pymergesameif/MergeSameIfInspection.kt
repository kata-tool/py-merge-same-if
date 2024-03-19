package org.example.pymergesameif

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiWhiteSpace
import com.intellij.refactoring.suggested.createSmartPointer
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyIfStatement

class MergeSameIfInspection : PyInspection() {
    override fun getDisplayName(): String {
        return "Merge same if"
    }

    override fun getShortName(): String {
        return "MergeSameIf"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PyElementVisitor() {
            override fun visitPyIfStatement(node: PyIfStatement) {
                val currentIfBody = node.ifPart.statementList.text
                val nextStatement = nextNonWhiteSpaceElement(node.nextSibling) as? PyIfStatement
                val nextIfBody = nextStatement?.ifPart?.statementList?.text
                if (currentIfBody == nextIfBody) {
                    holder.registerProblem(node, "Merge same if", MergeSameIfQuickFix(node.createSmartPointer(), checkNotNull(nextStatement).createSmartPointer()))
                }
                super.visitPyIfStatement(node)
            }
        }
    }

    private fun nextNonWhiteSpaceElement(node: PsiElement?): PsiElement? {
        if(node == null)
            return null
        if(node is PsiWhiteSpace)
            return nextNonWhiteSpaceElement(node.nextSibling)
        return node
    }
}
