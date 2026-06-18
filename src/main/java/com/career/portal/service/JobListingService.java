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

    public Page<JobListing> getJobListings(Pageable pageable) {
        return jobListingRepository.findAll(pageable);
    }

    public JobListing getJobListingById(Long id) {
        return jobListingRepository.findById(id).orElse(null);
    }

    public Page<JobListing> searchJobListings(String keyword, Pageable pageable) {
        return jobListingRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword, pageable);
    }

    public Page<JobListing> filterJobListings(String location, String industry, Pageable pageable) {
        return jobListingRepository.findByLocationAndIndustry(location, industry, pageable);
    }

    public void createJobListing(JobListing jobListing) {
        jobListingRepository.save(jobListing);
    }
}