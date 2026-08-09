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

    private final ConcurrentHashMap<String, JobListing> cache;
    private final ConcurrentHashMap<String, Long> cacheExpiration;

    public JobListingCacheUtil() {
        this.cache = new ConcurrentHashMap<>();
        this.cacheExpiration = new ConcurrentHashMap<>();
    }

    /**
     * Adds a job listing to the cache with a TTL of 30 minutes.
     *
     * @param jobListing the job listing to cache
     */
    public void addJobListingToCache(JobListing jobListing) {
        String jobId = String.valueOf(jobListing.getId());
        cache.put(jobId, jobListing);
        cacheExpiration.put(jobId, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30));
    }

    /**
     * Retrieves a job listing from the cache if it exists and has not expired.
     *
     * @param jobId the ID of the job listing to retrieve
     * @return the cached job listing, or null if it does not exist or has expired
     */
    public JobListing getJobListingFromCache(String jobId) {
        if (cache.containsKey(jobId)) {
            if (System.currentTimeMillis() < cacheExpiration.get(jobId)) {
                return cache.get(jobId);
            } else {
                // Cache has expired, remove it
                cache.remove(jobId);
                cacheExpiration.remove(jobId);
            }
        }
        return null;
    }

    /**
     * Removes a job listing from the cache.
     *
     * @param jobId the ID of the job listing to remove
     */
    public void removeJobListingFromCache(String jobId) {
        cache.remove(jobId);
        cacheExpiration.remove(jobId);
    }

    /**
     * Clears the entire cache.
     */
    public void clearCache() {
        cache.clear();
        cacheExpiration.clear();
    }

    public static void main(String[] args) {
        JobListingCacheUtil cacheUtil = new JobListingCacheUtil();
        JobListing jobListing = new JobListing();
        jobListing.setId(1L);
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop software applications");

        cacheUtil.addJobListingToCache(jobListing);

        JobListing cachedJobListing = cacheUtil.getJobListingFromCache(String.valueOf(jobListing.getId()));
        System.out.println("Cached Job Listing: " + cachedJobListing);

        cacheUtil.removeJobListingFromCache(String.valueOf(jobListing.getId()));
        cachedJobListing = cacheUtil.getJobListingFromCache(String.valueOf(jobListing.getId()));
        System.out.println("Cached Job Listing after removal: " + cachedJobListing);

        cacheUtil.clearCache();
    }
}