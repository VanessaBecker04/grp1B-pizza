package forms

/**
  * Form containing data to edit a category.
  *
  * @author Hasibullah Faroq
  *
  * @param oldCategory Old Category.
  * @param newCategory New Category.
  */
case class EditCategoryForm(oldCategory: String, newCategory: String)