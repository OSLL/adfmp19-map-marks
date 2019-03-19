package ru.itmo.se.mapmarks.data.storage

import android.util.Log
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark
import java.io.IOException

open class ListMarkInfoContainer : MarkInfoContainer {
    private val categories = mutableListOf<Category>()
    private val marks = mutableListOf<Mark>()

    override val allCategories: Iterable<Category>
        get() = categories.toList()

    override val allMarks: Iterable<Mark>
        get() = marks.toList()

    override fun getCategoryOfMark(markData: Mark) = marks.find { it == markData }?.category

    override fun getCategoryByName(categoryName: String): Category = allCategories.first { it.name == categoryName }

    override fun getMarkByName(markName: String): Mark {
        return allMarks.first { it.name == markName }
    }

    override fun getMarksForCategory(category: Category): Iterable<Mark> = marks.filter { it.category == category }

    override fun addCategory(category: Category): Boolean {
        if (!containsCategory(category)) {
            categories.add(0, category)
            return true
        }
        throw IOException("Категория с таким названием уже существует")
    }

    override fun addMark(mark: Mark): Boolean {
        val categoryNotExists = mark.category !in categories
        val markExists = mark in marks
        val markCannotBeAdded = categoryNotExists || markExists
        if (!markCannotBeAdded) {
            marks.add(0, mark)
            return true
        }
        throw IOException("Метка с таким названием уже существует")
    }

    override fun containsCategory(categoryName: String) = categories.find { it.name == categoryName } != null

    override fun containsCategory(category: Category) = category in categories

    override fun containsMark(markName: String) = marks.find { it.name == markName } != null

    override fun containsMark(mark: Mark) = mark in marks

    override fun updateCategory(category: Category): Boolean {
        val categoryIndex = categories.indexOf(category)
        val categoryDoesNotExist = categoryIndex == -1
        if (!categoryDoesNotExist) {
            categories[categoryIndex] = category
            return true
        }
        throw IOException("Категория с таким названием уже существует")
    }

    override fun updateMark(oldMark: Mark, newMark: Mark): Boolean {
        val markIndex = marks.indexOf(oldMark)
        val markDoesNotExists = markIndex == -1
        if (!markDoesNotExists) {
            newMark.attach = oldMark
            marks[markIndex] = newMark
            return true
        }
        return false
    }
}