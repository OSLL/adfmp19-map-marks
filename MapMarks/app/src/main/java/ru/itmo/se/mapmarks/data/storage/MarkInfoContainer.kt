package ru.itmo.se.mapmarks.data.storage

import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark

interface MarkInfoContainer {

    val allCategories: Iterable<Category>
    val allMarks: Iterable<Mark>

    fun getCategoryOfMark(markData: Mark): Category?

    fun getMarksForCategory(category: Category): Iterable<Mark>

    fun addCategory(category: Category): CategoryOpsResultHandler
    fun addMark(mark: Mark): MarkOpsResultHandler

    fun containsCategory(categoryName: String): Boolean
    fun containsCategory(category: Category): Boolean

    fun containsMark(markName: String): Boolean
    fun containsMark(mark: Mark): Boolean

    fun updateCategory(category: Category): CategoryOpsResultHandler
    fun updateMark(mark: Mark): MarkOpsResultHandler

    fun getCategoryByName(categoryName: String): Category
}

class CategoryOpsResultHandler(private val category: Category, private val needCallback: Boolean) {
    fun ifAlreadyExists(action: (Category) -> Unit): CategoryOpsResultHandler {
        if (needCallback) {
            action(category)
        }
        return this
    }
}

class MarkOpsResultHandler(private val mark: Mark, private val needCallback: Boolean) {
    fun ifCategoryNotExists(action: (Mark) -> Unit): MarkOpsResultHandler {
        if (needCallback) {
            action(mark)
        }
        return this
    }
    fun ifMarkAlreadyExists(action: (Mark) -> Unit): MarkOpsResultHandler {
        if (needCallback) {
            action(mark)
        }
        return this
    }
}