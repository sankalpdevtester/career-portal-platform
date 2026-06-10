package com.career.portal.service;

import com.career.portal.model.JobListing;
import com.career.portal.model.User;
import com.career.portal.model.JobApplication;
import com.career.portal.repository.JobApplicationRepository;
import com.career.portal.repository.JobListingRepository;
import com.career.portal.repository.UserRepository;
import com.career.portal.utils.DateUtil;
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

    public JobApplication applyForJob(Long userId, Long jobId) {
        User user = userRepository.findById(userId).orElse(null);
        JobListing jobListing = jobListingRepository.findById(jobId).orElse(null);

        if (user != null && jobListing != null) {
            JobApplication jobApplication = new JobApplication();
            jobApplication.setUser(user);
            jobApplication.setJobListing(jobListing);
            jobApplication.setApplicationDate(new Date());
            jobApplication.setApplicationStatus("Pending");

            return jobApplicationRepository.save(jobApplication);
        } else {
            return null;
        }
    }

    public List<JobApplication> getApplicationHistory(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            return jobApplicationRepository.findByUser(user);
        } else {
            return null;
        }
    }

    public JobApplication getJobApplication(Long applicationId) {
        return jobApplicationRepository.findById(applicationId).orElse(null);
    }

    public JobApplication updateApplicationStatus(Long applicationId, String status) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId).orElse(null);

        if (jobApplication != null) {
            jobApplication.setApplicationStatus(status);
            jobApplication.setUpdatedDate(new Date());

            return jobApplicationRepository.save(jobApplication);
        } else {
            return null;
        }
    }
}