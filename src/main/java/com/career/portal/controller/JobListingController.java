package com.career.portal.controller;

import com.career.portal.model.JobListing;
import com.career.portal.model.User;
import com.career.portal.service.JobListingService;
import com.career.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class JobListingController {

    @Autowired
    private JobListingService jobListingService;

    @Autowired
    private UserService userService;

    @GetMapping("/job-listings")
    public String getJobListings(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobListing> jobListings = jobListingService.getJobListings(pageable);
        model.addAttribute("jobListings", jobListings);
        return "job-listings";
    }

    @GetMapping("/job-listings/{id}")
    public String getJobListingById(@PathVariable Long id, Model model) {
        JobListing jobListing = jobListingService.getJobListingById(id);
        model.addAttribute("jobListing", jobListing);
        return "job-listing-details";
    }

    @GetMapping("/job-listings/search")
    public String searchJobListings(@RequestParam(name = "keyword") String keyword, Model model,
                                     @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                     @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobListing> jobListings = jobListingService.searchJobListings(keyword, pageable);
        model.addAttribute("jobListings", jobListings);
        return "job-listings";
    }

    @GetMapping("/job-listings/filter")
    public String filterJobListings(@RequestParam(name = "location") String location,
                                    @RequestParam(name = "industry") String industry, Model model,
                                    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobListing> jobListings = jobListingService.filterJobListings(location, industry, pageable);
        model.addAttribute("jobListings", jobListings);
        return "job-listings";
    }

    @PostMapping("/job-listings")
    public String createJobListing(@ModelAttribute JobListing jobListing) {
        jobListingService.createJobListing(jobListing);
        return "redirect:/job-listings";
    }
}