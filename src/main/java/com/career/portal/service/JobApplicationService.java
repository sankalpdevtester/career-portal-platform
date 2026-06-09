package com.career.portal.service;

import com.career.portal.model.JobListing;
import com.career.portal.model.User;
import com.career.portal.model.JobApplication;
import com.career.portal.repository.JobApplicationRepository;
import com.career.portal.repository.JobListingRepository;
import com.career.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobListingRepository jobListingRepository;
    private final UserRepository userRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, 
                                   JobListingRepository jobListingRepository, 
                                   UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobListingRepository = jobListingRepository;
        this.userRepository = userRepository;
    }

    public JobApplication applyForJob(Long userId, Long jobListingId) {
        User user = userRepository.findById(userId).orElse(null);
        JobListing jobListing = jobListingRepository.findById(jobListingId).orElse(null);

        if (user == null || jobListing == null) {
            return null;
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJobListing(jobListing);
        jobApplication.setApplicationDate(new Date());

        return jobApplicationRepository.save(jobApplication);
    }

    public List<JobApplication> getJobApplicationsForUser(Long userId) {
        return jobApplicationRepository.findByUser_Id(userId);
    }

    public List<JobApplication> getJobApplicationsForJobListing(Long jobListingId) {
        return jobApplicationRepository.findByJobListing_Id(jobListingId);
    }
}
```

```java
// src/main/java/com/career/portal/repository/JobApplicationRepository.java
package com.career.portal.repository;

import com.career.portal.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUser_Id(Long userId);
    List<JobApplication> findByJobListing_Id(Long jobListingId);
}
```

```java
// src/main/java/com/career/portal/model/JobApplication.java
package com.career.portal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private JobListing jobListing;

    private Date applicationDate;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JobListing getJobListing() {
        return jobListing;
    }

    public void setJobListing(JobListing jobListing) {
        this.jobListing = jobListing;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }
}