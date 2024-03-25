package homiessecurity.controllers;

import homiessecurity.dtos.Categories.CategoryDto;
import homiessecurity.entities.ServiceCategory;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ServiceCategory>> getAllCategories(){
        List<ServiceCategory> categories = this.categoryService.getAllCategories();
        return new ResponseEntity<List<ServiceCategory>>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ServiceCategory> getCategoryDetails(@PathVariable Integer categoryId){
        ServiceCategory category = this.categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ServiceCategory> saveCategory(@Valid @ModelAttribute CategoryDto category,
                                                    @RequestParam(value = "categoryImage", required = false) MultipartFile file){
        if(file != null){
            category.setCategoryImage(file);
        }
        ServiceCategory savedCategory = this.categoryService.addCategory(category);
        return new ResponseEntity<ServiceCategory>(savedCategory, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ServiceCategory> updateCategory(@PathVariable Integer categoryId, @ModelAttribute CategoryDto category,
                                                          @RequestParam(value = "categoryImage", required = false) MultipartFile file){
        if(file != null){
            category.setCategoryImage(file);
        }

        ServiceCategory updatedCategory = this.categoryService.updateCategoryById(categoryId, category);
        return new ResponseEntity<ServiceCategory>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        this.categoryService.deleteCategoryById(categoryId);
        ApiResponse response = new ApiResponse( "Category Deleted Successfully",true);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<ServiceCategory> getCategoryByTitle(@RequestParam String title){
        ServiceCategory category = this.categoryService.getCategoryByTitle(title);
        return new ResponseEntity<ServiceCategory>(category, HttpStatus.OK);
    }



}
