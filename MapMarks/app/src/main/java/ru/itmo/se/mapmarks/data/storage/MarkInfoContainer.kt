package ru.itmo.se.mapmarks.data.storage

import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark

interface MarkInfoContainer {

    val allCategories: Iterable<Category>
    val allMarks: Iterable<Mark>

    fun getCategoryOfMark(markData: Mark): Category?

    fun getMarksForCategory(category: Category): Iterable<Mark>

    fun addCategory(category: Category): Boolean
    fun addMark(mark: Mark): Boolean

    fun containsCategory(categoryName: String): Boolean
    fun containsCategory(category: Category): Boolean

    fun containsMark(markName: String): Boolean
    fun containsMark(mark: Mark): Boolean

    fun updateCategory(category: Category): Boolean
    fun updateMark(mark: Mark): Boolean

    fun getCategoryByName(categoryName: String): Category
}