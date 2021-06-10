package com.example.videogame.model.joined;

import com.example.videogame.model.Player;
import com.example.videogame.model.ScoreEvent;

public class ScoreWithPlayer {

    private ScoreEvent eventScore;
    private Player player;

    public ScoreWithPlayer(ScoreEvent eventScore, Player player) {
        this.eventScore = eventScore;
        this.player = player;
    }

    public ScoreEvent getEventScore() {
        return eventScore;
    }

    public void setEventScore(ScoreEvent eventScore) {
        this.eventScore = eventScore;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String toString() {
        return "{" + " scoreEvent='" + getEventScore() + "'" + ", player='" + getPlayer() + "'" + "}";
    }

}
