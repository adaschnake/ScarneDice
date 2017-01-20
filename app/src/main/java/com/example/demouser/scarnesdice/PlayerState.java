package com.example.demouser.scarnesdice;

/**
 * Created by demouser on 1/18/17.
 */

public class PlayerState {

    private String id;
    private String email;
    private PlayerStatus status;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    private String gameId;

    public PlayerState()
    {

    }

    public PlayerState(String email) {
        this.id = sanitizeEmail(email);
        this.email = email;
        status = PlayerStatus.READY;
    }

    public static String sanitizeEmail(String email)
    {
        return email.replace('.','-').replace('#','-').replace('$','-').replace(']','-').replace('[','-');
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    enum PlayerStatus
    {
        READY, IN_GAME
    }

}
