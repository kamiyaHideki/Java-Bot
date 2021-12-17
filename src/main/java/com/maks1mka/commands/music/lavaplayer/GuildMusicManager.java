package com.maks1mka.commands.music.lavaplayer;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {

    public final AudioPlayer player;

    public final TrackScheduler scheduler;

    public final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager manager)
    {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        sendHandler = new AudioPlayerSendHandler(player);
        player.addListener(scheduler);
    }

}