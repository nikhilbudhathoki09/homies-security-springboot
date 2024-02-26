package homiessecurity.service;


import homiessecurity.dtos.Categories.CategoryDto;
import homiessecurity.entities.ServiceCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    ServiceCategory addCategory(CategoryDto category);

    ServiceCategory getCategoryById(Integer categoryId);

    ServiceCategory getCategoryByTitle(String title);

    ServiceCategory updateCategoryById(Integer categoryId, CategoryDto category);

    List<ServiceCategory> getAllCategories();

    void deleteCategoryById(Integer categoryId);

    ServiceCategory getCategoryByName(String name);

    void updateServices(ServiceCategory category);


    
    
}
