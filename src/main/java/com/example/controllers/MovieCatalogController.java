package com.example.controllers;

import com.example.models.CatalogItem;
import com.example.models.UserRating;
import com.example.services.MovieInfoService;
import com.example.services.UserRatingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private WebClient.Builder webclientBuilder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieInfoService movieInfoService;

    @Autowired
    private UserRatingInfoService userRatingInfoService;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        // System.out.println("********"+apiKey+"**********");  // works
        // get ratings from ratings-data
        UserRating userRating = userRatingInfoService.getUserRating(userId);
        // get movie info for each movie
        return userRating.getRatings().stream().map(rating -> movieInfoService.getCatalogItem(rating) )
                .collect(Collectors.toList());
    }
}

//    Movie movie = webclientBuilder.build()
//            .get()
//            .uri("http://localhost:8083/movies/" + rating.getMovieId())
//            .retrieve()
//            .bodyToMono(Movie.class)
//            .block();