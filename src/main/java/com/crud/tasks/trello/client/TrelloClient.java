package com.crud.tasks.trello.client;

import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.trello.config.TrelloConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TrelloClient {

    @Autowired
    private RestTemplate restTemplate;
    private TrelloConfig trelloConfig;
    private List<TrelloBoardDto> trelloBoard;

    private URI uriUrl() {

        URI url = UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint() + "/members/aleksanderlockhart/boards")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloAppToken())
                .queryParam("fields", "name,id")
                .build().encode().toUri();

        return url;
    }

    public List<TrelloBoardDto> getTrelloBoards() {

        try{
            TrelloBoardDto[] boardsResponse = restTemplate.getForObject(uriUrl(), TrelloBoardDto[].class);
            return Optional.ofNullable(boardsResponse).map(Arrays::asList).orElse(new ArrayList<>());
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

}
