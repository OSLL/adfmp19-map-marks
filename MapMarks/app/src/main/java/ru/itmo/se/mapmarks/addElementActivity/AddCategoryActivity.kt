package ru.itmo.se.mapmarks.addElementActivity

class AddCategoryActivity : ManipulateCategoryActivity() {

    override fun createAndAddCategory(name: String, description: String) {
        val newCategory = createCategory(name, description)
        markInfoContainer.addCategory(newCategory)
    }
}
