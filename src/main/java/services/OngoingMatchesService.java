package services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.entities.Match;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OngoingMatchesService {

    @Getter
    private static final ConcurrentHashMap<UUID, Match> ongoingMatches =  new ConcurrentHashMap<>();

    public static void addNewMatch(UUID uuid, Match match) {
        ongoingMatches.put(uuid, match);
    }
}
