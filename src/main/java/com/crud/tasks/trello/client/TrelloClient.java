package com.crud.tasks.trello.client;

import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
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

    @Autowired
    private TrelloConfig trelloConfig;

    private List<TrelloBoardDto> trelloBoard;

    private URI uriUrl() {

        return UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint() + trelloConfig.getTrelloAppUsername())
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloAppToken())
                .queryParam("fields", "name,id")
                .queryParam("lists", "all")
                .build().encode().toUri();
    }

    public List<TrelloBoardDto> getTrelloBoards() {

        try {
            System.out.println(uriUrl());
            TrelloBoardDto[] boardsResponse = restTemplate.getForObject(uriUrl(), TrelloBoardDto[].class);
            return Optional
                    .ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(new ArrayList<>());
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    public CreatedTrelloCard createNewCard(TrelloCardDto trelloCardDto) {

        URI url = UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint()
                + "/cards")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloAppToken())
                .queryParam("name", trelloCardDto.getName() )
                .queryParam("desc", trelloCardDto.getDescription())
                .queryParam("pos", trelloCardDto.getPos())
                .queryParam("idList", trelloCardDto.getListId())
                .queryParam("badges", trelloCardDto.getBadges())
                .build().encode().toUri();

        return restTemplate.postForObject(url, null, CreatedTrelloCard.class);
    }


}
