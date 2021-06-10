package com.example.videogame;

import com.example.videogame.model.joined.Enriched;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class HighScores {
    private final TreeSet<Enriched> highScores = new TreeSet<>();

    public HighScores add(final Enriched enriched) {
        highScores.add(enriched);

        if(highScores.size() > 3) {
            highScores.remove(highScores.last());
        }

        return this;
    }

    public List<Enriched> toList() {
        List<Enriched> playerScores = new ArrayList<>();
        Iterator<Enriched> iterator = highScores.iterator();

        while(iterator.hasNext()) {
            playerScores.add(iterator.next());
        }
        return  playerScores;
    }
}
