package com.career.portal.repository;

import com.career.portal.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {

    @Query("SELECT j FROM JobListing j WHERE j.title LIKE %:keyword% OR j.description LIKE %:keyword%")
    List<JobListing> findByTitleContainingOrDescriptionContaining(@Param("keyword") String keyword);

    @Query("SELECT j FROM JobListing j WHERE j.location = :location AND j.industry = :industry")
    List<JobListing> findByLocationAndIndustry(@Param("location") String location, @Param("industry") String industry);

    @Query("SELECT j FROM JobListing j WHERE j.title LIKE %:keyword% OR j.description LIKE %:keyword%")
    Page<JobListing> findByTitleContainingOrDescriptionContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT j FROM JobListing j WHERE j.location = :location AND j.industry = :industry")
    Page<JobListing> findByLocationAndIndustry(@Param("location") String location, @Param("industry") String industry, Pageable pageable);
}