package com.career.portal.utils;

import com.career.portal.model.JobListing;
import com.career.portal.repository.JobListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Component
public class JobListingCacheUtil {

    private final ConcurrentMap<String, JobListing> cache = new ConcurrentHashMap<>();
    private final JobListingRepository jobListingRepository;

    @Autowired
    public JobListingCacheUtil(JobListingRepository jobListingRepository) {
        this.jobListingRepository = jobListingRepository;
    }

    public JobListing getJobListingFromCache(String id) {
        return cache.get(id);
    }

    public void putJobListingInCache(String id, JobListing jobListing) {
        cache.put(id, jobListing);
    }

    public void removeJobListingFromCache(String id) {
        cache.remove(id);
    }

    public void invalidateCache() {
        cache.clear();
    }

    public JobListing getJobListingWithCache(String id) {
        JobListing jobListing = getJobListingFromCache(id);
        if (jobListing != null) {
            return jobListing;
        } else {
            jobListing = jobListingRepository.findById(id).orElse(null);
            if (jobListing != null) {
                putJobListingInCache(id, jobListing);
            }
            return jobListing;
        }
    }

    public void scheduleCacheInvalidation(String id, long ttl, TimeUnit timeUnit) {
        new Thread(() -> {
            try {
                timeUnit.sleep(ttl);
                removeJobListingFromCache(id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public void cacheJobListing(JobListing jobListing) {
        putJobListingInCache(jobListing.getId(), jobListing);
        scheduleCacheInvalidation(jobListing.getId(), 30, TimeUnit.MINUTES);
    }

    public void cacheJobListings(Iterable<JobListing> jobListings) {
        jobListings.forEach(this::cacheJobListing);
    }
}