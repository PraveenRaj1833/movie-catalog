package com.example.services;

import com.example.models.Rating;
import com.example.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfoService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallBackUserRatingItem",
                    commandProperties = {
                        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
                    }
    )
    public UserRating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratings/users/" + userId, UserRating.class);
    }

    public UserRating getFallBackUserRatingItem(String userId) {
        UserRating userRating = new UserRating();
        userRating.setRatings(Arrays.asList(new Rating("0",0)));
        return userRating;
    }
}
