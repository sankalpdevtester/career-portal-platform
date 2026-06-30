package com.career.portal.utils;

import com.career.portal.model.JobListing;
import com.career.portal.repository.JobListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for caching job listings with a time-to-live (TTL) of 30 minutes.
 */
@Component
public class JobListingCacheUtil {

    private final ConcurrentHashMap<String, JobListing> cache;
    private final JobListingRepository jobListingRepository;

    @Autowired
    public JobListingCacheUtil(JobListingRepository jobListingRepository) {
        this.cache = new ConcurrentHashMap<>();
        this.jobListingRepository = jobListingRepository;
    }

    /**
     * Retrieves a job listing from the cache or database.
     *
     * @param id the ID of the job listing
     * @return the job listing
     */
    public JobListing getJobListing(String id) {
        return cache.computeIfAbsent(id, this::loadJobListingFromDatabase);
    }

    /**
     * Loads a job listing from the database and caches it with a TTL of 30 minutes.
     *
     * @param id the ID of the job listing
     * @return the job listing
     */
    private JobListing loadJobListingFromDatabase(String id) {
        JobListing jobListing = jobListingRepository.findById(id).orElse(null);
        if (jobListing != null) {
            cache.put(id, jobListing);
            // Set TTL to 30 minutes
            scheduleCacheEviction(id, 30, TimeUnit.MINUTES);
        }
        return jobListing;
    }

    /**
     * Schedules the eviction of a job listing from the cache after a specified delay.
     *
     * @param id     the ID of the job listing
     * @param delay  the delay before eviction
     * @param unit   the time unit of the delay
     */
    private void scheduleCacheEviction(String id, long delay, TimeUnit unit) {
        // Use a ScheduledExecutorService to schedule the eviction
        // For simplicity, we'll use a simple Thread here
        Thread thread = new Thread(() -> {
            try {
                unit.sleep(delay);
                cache.remove(id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.setDaemon(true); // Allow the JVM to exit even if the thread is still running
        thread.start();
    }

    /**
     * Clears the cache.
     */
    public void clearCache() {
        cache.clear();
    }
}
```
```java
// Example usage in JobListingController
package com.career.portal.controller;

import com.career.portal.utils.JobListingCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobListingController {

    private final JobListingCacheUtil jobListingCacheUtil;

    @Autowired
    public JobListingController(JobListingCacheUtil jobListingCacheUtil) {
        this.jobListingCacheUtil = jobListingCacheUtil;
    }

    @GetMapping("/job-listings/{id}")
    public JobListing getJobListing(@PathVariable String id) {
        return jobListingCacheUtil.getJobListing(id);
    }
}