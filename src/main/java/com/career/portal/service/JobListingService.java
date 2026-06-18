package com.career.portal.service;

import com.career.portal.model.JobListing;
import com.career.portal.repository.JobListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobListingService {

    @Autowired
    private JobListingRepository jobListingRepository;

    public Page<JobListing> getJobListings(Pageable pageable, String search, String location, String category) {
        if (search != null && !search.isEmpty()) {
            return jobListingRepository.findByTitleContainingOrDescriptionContainingOrLocationContaining(search, search, search, pageable);
        } else if (location != null && !location.isEmpty()) {
            return jobListingRepository.findByLocationContaining(location, pageable);
        } else if (category != null && !category.isEmpty()) {
            return jobListingRepository.findByCategoryContaining(category, pageable);
        } else {
            return jobListingRepository.findAll(pageable);
        }
    }

    public JobListing getJobListingById(Long id) {
        return jobListingRepository.findById(id).orElse(null);
    }

    public void createJobListing(JobListing jobListing) {
        jobListingRepository.save(jobListing);
    }

    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }
}