package pack.currency.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CacheHelper {
    private CacheManager cacheManager;
    private Cache<String, BigDecimal> dateToUsdPriceCache;


    public CacheHelper() {
        cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder().build();
        cacheManager.init();

        dateToUsdPriceCache = cacheManager
                .createCache("dateToUsdPriceCache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(
                                String.class, BigDecimal.class,
                                ResourcePoolsBuilder.heap(10)));
    }

    public Cache<String, BigDecimal> getDateToUsdPriceCacheFromCacheManager() {
        return cacheManager.getCache("dateToUsdPriceCache", String.class, BigDecimal.class);
    }

    public Cache<String, BigDecimal> getDateToUsdPriceCache() {
        return dateToUsdPriceCache;
    }
}
