package forms

/**
  * Form containing data to add a category.
  *
  * @author Maximilian Öttl
  *
  * @param name   Name of the category.
  * @param unit   Unit of the category.

  */
case class CreateCategoryForm(name: String, unit: String)