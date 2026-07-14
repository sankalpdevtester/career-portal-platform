import com.career.portal.model.JobListing;
import com.career.portal.utils.DateUtil;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for caching job listings with a time-to-live (TTL) to reduce database queries.
 */
@Component
public class JobListingCacheUtil {

    // Cache to store job listings with TTL
    private ConcurrentHashMap<Long, JobListing> cache = new ConcurrentHashMap<>();

    /**
     * Retrieves a job listing from the cache if it exists and is not expired.
     *
     * @param id Job listing ID
     * @return Job listing if cached, otherwise null
     */
    public JobListing getJobListingFromCache(Long id) {
        if (cache.containsKey(id)) {
            JobListing jobListing = cache.get(id);
            if (DateUtil.isWithinTTL(jobListing.getCacheTimestamp(), 30, TimeUnit.MINUTES)) {
                return jobListing;
            } else {
                // Remove expired job listing from cache
                cache.remove(id);
            }
        }
        return null;
    }

    /**
     * Adds a job listing to the cache with a TTL of 30 minutes.
     *
     * @param jobListing Job listing to cache
     */
    public void addJobListingToCache(JobListing jobListing) {
        jobListing.setCacheTimestamp(DateUtil.getCurrentTimestamp());
        cache.put(jobListing.getId(), jobListing);
    }

    /**
     * Removes a job listing from the cache.
     *
     * @param id Job listing ID
     */
    public void removeJobListingFromCache(Long id) {
        cache.remove(id);
    }

    /**
     * Clears the entire cache.
     */
    public void clearCache() {
        cache.clear();
    }
}
``}