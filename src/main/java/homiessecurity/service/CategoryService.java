package homiessecurity.service;


import homiessecurity.dtos.Categories.CategoryDto;
import homiessecurity.entities.ServiceCategory;
import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto category);

    ServiceCategory getCategoryById(Integer categoryId);

    ServiceCategory getCategoryByTitle(String title);

    ServiceCategory updateCategoryById(Integer categoryId, CategoryDto category);

    List<ServiceCategory> getAllCategories();

    void deleteCategoryById(Integer categoryId);


    
    
}
