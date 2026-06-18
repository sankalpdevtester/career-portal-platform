package com.career.portal.repository;

import com.career.portal.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {

    @Query("SELECT j FROM JobListing j WHERE j.title LIKE %:search% OR j.description LIKE %:search% OR j.location LIKE %:search%")
    Page<JobListing> findByTitleContainingOrDescriptionContainingOrLocationContaining(@Param("search") String search, Pageable pageable);

    @Query("SELECT j FROM JobListing j WHERE j.location LIKE %:location%")
    Page<JobListing> findByLocationContaining(@Param("location") String location, Pageable pageable);

    @Query("SELECT j FROM JobListing j WHERE j.category LIKE %:category%")
    Page<JobListing> findByCategoryContaining(@Param("category") String category, Pageable pageable);
}