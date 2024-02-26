package homiessecurity.service.impl;

import homiessecurity.dtos.Categories.CategoryDto;
import homiessecurity.entities.ServiceCategory;

import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.CategoryRepository;
import homiessecurity.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;

    private final CloudinaryServiceImpl cloudinary;


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepo, ModelMapper modelMapper, CloudinaryServiceImpl cloudinary){
        this.categoryRepo = categoryRepo;
        this.modelMapper = new ModelMapper();
        this.cloudinary = cloudinary;
    }


    @Override
    public ServiceCategory addCategory(CategoryDto category) {
        String imageUrl = null;
        if(category.getCategoryImage() != null){
             imageUrl = this.cloudinary.uploadImage(category.getCategoryImage(), "Categories");
        }
        System.out.println("Image URL: " + imageUrl);
        ServiceCategory serviceCategory = ServiceCategory.builder().
                title(category.getTitle()).
                description(category.getDescription()).
                categoryImage(imageUrl).
                build();
        ServiceCategory categoryEntity = categoryRepo.save(serviceCategory);
        return categoryEntity;
    }

    @Override
    public ServiceCategory getCategoryById(Integer categoryId) {
        ServiceCategory category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        return category;
    }

    @Override
    public ServiceCategory getCategoryByTitle(String title) {
        ServiceCategory category = categoryRepo.findByTitle(title).orElseThrow(() ->
                new ResourceNotFoundException("Category", "title", title));
        return category;      
    }

    @Override
    public ServiceCategory updateCategoryById(Integer categoryId, CategoryDto category) {
        ServiceCategory existingCategory = categoryRepo.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category", "categoryId", categoryId));

        if(category.getCategoryImage() !=null){
            String imageUrl = this.cloudinary.uploadImage(category.getCategoryImage(), "Categories");
            existingCategory.setCategoryImage(imageUrl);
        }
        if(category.getTitle() != null){
            existingCategory.setTitle(category.getTitle());
        }
        if(category.getDescription() != null){
            existingCategory.setDescription(category.getDescription());
        }

        return categoryRepo.save(existingCategory);
    }

    @Override
    public List<ServiceCategory> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public void deleteCategoryById(Integer categoryId) {
        ServiceCategory category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepo.delete(category);
    }

    @Override
    public ServiceCategory getCategoryByName(String name) {
        return categoryRepo.findByTitle(name).orElseThrow(() ->
                new ResourceNotFoundException("Category", "name", name));
    }

    @Override
    public void updateServices(ServiceCategory category) {
        categoryRepo.save(category);
    }


}
