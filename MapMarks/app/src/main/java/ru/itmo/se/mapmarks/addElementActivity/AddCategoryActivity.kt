package ru.itmo.se.mapmarks.addElementActivity

import android.view.MenuItem
import ru.itmo.se.mapmarks.data.mark.Mark

class AddCategoryActivity : ManipulateCategoryActivity() {

    override fun createAndAddCategory(name: String, description: String) {
        val newCategory = createCategory(name, description)
        markInfoContainer.addCategory(newCategory)
    }
}
