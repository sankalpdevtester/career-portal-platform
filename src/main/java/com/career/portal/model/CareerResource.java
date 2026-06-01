// src/main/java/com/career/portal/model/CareerResource.java
package com.career.portal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class CareerResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String url;
    private Date createdAt;
    private Date updatedAt;

    public CareerResource() {}

    public CareerResource(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
```

```java
// src/main/java/com/career/portal/repository/CareerResourceRepository.java
package com.career.portal.repository;

import com.career.portal.model.CareerResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerResourceRepository extends JpaRepository<CareerResource, Long> {
}
```

```java
// src/main/java/com/career/portal/service/CareerResourceService.java
package com.career.portal.service;

import com.career.portal.model.CareerResource;
import com.career.portal.repository.CareerResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerResourceService {
    private final CareerResourceRepository careerResourceRepository;

    @Autowired
    public CareerResourceService(CareerResourceRepository careerResourceRepository) {
        this.careerResourceRepository = careerResourceRepository;
    }

    public List<CareerResource> getAllCareerResources() {
        return careerResourceRepository.findAll();
    }

    public CareerResource getCareerResourceById(Long id) {
        return careerResourceRepository.findById(id).orElse(null);
    }

    public CareerResource createCareerResource(CareerResource careerResource) {
        return careerResourceRepository.save(careerResource);
    }

    public CareerResource updateCareerResource(CareerResource careerResource) {
        CareerResource existingCareerResource = getCareerResourceById(careerResource.getId());
        if (existingCareerResource != null) {
            existingCareerResource.setTitle(careerResource.getTitle());
            existingCareerResource.setDescription(careerResource.getDescription());
            existingCareerResource.setUrl(careerResource.getUrl());
            existingCareerResource.setUpdatedAt(new Date());
            return careerResourceRepository.save(existingCareerResource);
        } else {
            return null;
        }
    }

    public void deleteCareerResource(Long id) {
        careerResourceRepository.deleteById(id);
    }
}
```

```java
// src/main/java/com/career/portal/controller/CareerResourceController.java
package com.career.portal.controller;

import com.career.portal.model.CareerResource;
import com.career.portal.service.CareerResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CareerResourceController {
    private final CareerResourceService careerResourceService;

    @Autowired
    public CareerResourceController(CareerResourceService careerResourceService) {
        this.careerResourceService = careerResourceService;
    }

    @GetMapping("/career-resources")
    public String getAllCareerResources(Model model) {
        List<CareerResource> careerResources = careerResourceService.getAllCareerResources();
        model.addAttribute("careerResources", careerResources);
        return "career-resources";
    }

    @GetMapping("/career-resources/{id}")
    public String getCareerResourceById(@PathVariable Long id, Model model) {
        CareerResource careerResource = careerResourceService.getCareerResourceById(id);
        model.addAttribute("careerResource", careerResource);
        return "career-resource";
    }

    @GetMapping("/create-career-resource")
    public String createCareerResourceForm(Model model) {
        model.addAttribute("careerResource", new CareerResource());
        return "create-career-resource";
    }

    @PostMapping("/create-career-resource")
    public String createCareerResource(@ModelAttribute CareerResource careerResource) {
        careerResourceService.createCareerResource(careerResource);
        return "redirect:/career-resources";
    }

    @GetMapping("/update-career-resource/{id}")
    public String updateCareerResourceForm(@PathVariable Long id, Model model) {
        CareerResource careerResource = careerResourceService.getCareerResourceById(id);
        model.addAttribute("careerResource", careerResource);
        return "update-career-resource";
    }

    @PostMapping("/update-career-resource/{id}")
    public String updateCareerResource(@PathVariable Long id, @ModelAttribute CareerResource careerResource) {
        careerResource.setId(id);
        careerResourceService.updateCareerResource(careerResource);
        return "redirect:/career-resources";
    }

    @GetMapping("/delete-career-resource/{id}")
    public String deleteCareerResource(@PathVariable Long id) {
        careerResourceService.deleteCareerResource(id);
        return "redirect:/career-resources";
    }
}