package com.example.videogame.model.joined;

import com.example.videogame.model.Product;

public class Enriched implements Comparable<Enriched>{
    private Long playerId;
    private Long productId;
    private String playerName;
    private String gameName;
    private Double score;

    public Enriched(ScoreWithPlayer scoreWithPlayer, Product product) {
        this.playerId = scoreWithPlayer.getPlayer().getId();
        this.productId = product.getId();
        this.playerName = scoreWithPlayer.getPlayer().getName();
        this.gameName = product.getName();
        this.score = scoreWithPlayer.getEventScore().getScore();
    }

    public String toString() {
        return "{"
                + " playerId='"
                + getPlayerId()
                + "'"
                + ", playerName='"
                + getPlayerName()
                + "'"
                + ", gameName='"
                + getGameName()
                + "'"
                + ", score='"
                + getScore()
                + "'"
                + "}";
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Enriched o) {
        return Double.compare(o.score, score);
    }
}
