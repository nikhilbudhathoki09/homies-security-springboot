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
    public CategoryDto addCategory(CategoryDto category) {

        String imageUrl = this.cloudinary.uploadImage(category.getCategoryImage(), "Categories");
        ServiceCategory serviceCategory = ServiceCategory.builder().
                title(category.getTitle()).
                description(category.getDescription()).
                categoryImage(imageUrl).
                build();
        ServiceCategory categoryEntity = categoryRepo.save(serviceCategory);
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }

    @Override
    public ServiceCategory getCategoryById(Integer categoryId) {
        ServiceCategory category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        return category;
    }

    @Override
    public ServiceCategory getCategoryByTitle(String title) {
        ServiceCategory category = categoryRepo.findByTitle(title).orElseThrow(() -> new ResourceNotFoundException("Category", "title", title));
        return category;      
    }

    @Override
    public ServiceCategory updateCategoryById(Integer categoryId, ServiceCategory category) {
        ServiceCategory existingCategory = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        existingCategory.setTitle(category.getTitle());
        existingCategory.setDescription(category.getDescription());
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


}
