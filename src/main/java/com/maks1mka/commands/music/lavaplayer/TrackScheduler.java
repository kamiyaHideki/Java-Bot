package com.maks1mka.commands.music.lavaplayer;

import com.maks1mka.commands.music.PlayCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrackScheduler extends AudioEventAdapter {
    private boolean repeating = false;
    final AudioPlayer player;
    final Queue<AudioTrack> queue;
    private  long playingMessageid;
    AudioTrack lastTrack;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    public Queue<AudioTrack> getQueue() {
        return queue;
    }

    public void queue(AudioTrack track, boolean isPlaylist) {
        if (!player.startTrack(track, true)) {
            if(!isPlaylist) {
                Member currentAuthor = PlayCommand.currentAuthor;
                int position = queue.size() + 1;
                String duration = "__";
                if(!track.getInfo().isStream) duration = PlayCommand.getTimestamp(track.getDuration());
                MessageEmbed playingEmbed = new EmbedBuilder()
                        .setTitle("В очередь добавлено")
                        .setDescription("**" + track.getInfo().title + "**")
                        .addField("Позиция в очереди", "" + position, false)
                        .addField("Длительность", duration, true)
                        .addField("Ссылка", track.getInfo().uri, true)
                        .setColor(currentAuthor.getUser().retrieveProfile().complete().getAccentColor())
                        .setFooter( "Запросил " + (currentAuthor.getNickname() == null ? currentAuthor.getUser().getName() : currentAuthor.getNickname()), currentAuthor.getUser().getAvatarUrl())
                        .build();
                PlayCommand.currentChannel.sendMessageEmbeds(playingEmbed).queue();
            }
            queue.add(track);
        }

    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        Member currentAuthor = PlayCommand.currentAuthor;
        String duration = "__";
        if(!track.getInfo().isStream) duration = PlayCommand.getTimestamp(track.getDuration());
        MessageEmbed playingEmbed = new EmbedBuilder()
                .setTitle("Сейчас проигрывается")
                .setDescription("**" + track.getInfo().title + "**")
                .addField("Длительность", duration , true)
                .addField("Ссылка", track.getInfo().uri, true)
                .setColor(currentAuthor.getUser().retrieveProfile().complete().getAccentColor())
                .setFooter( "Запросил " + (currentAuthor.getNickname() == null ? currentAuthor.getUser().getName() : currentAuthor.getNickname()), currentAuthor.getUser().getAvatarUrl())
                .build();

        PlayCommand.currentChannel.sendMessageEmbeds(playingEmbed).queue((result) -> playingMessageid = result.getIdLong());
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason){
        this.lastTrack = track;
        if(queue.isEmpty()) {
            if(playingMessageid != 0) PlayCommand.currentChannel.deleteMessageById(playingMessageid).queue();
            PlayCommand.currentChannel.sendMessage("Очередь закончилась, выхожу из канала!").queue();
            GuildMusicManager manager = PlayCommand.getMusicManager(PlayCommand.currentGuild);
            PlayCommand.musicManagers.values().remove(manager);
            PlayCommand.currentGuild.getAudioManager().closeAudioConnection();
        }
        else if (endReason.mayStartNext) {
            if (repeating){
                if(playingMessageid != 0) PlayCommand.currentChannel.deleteMessageById(playingMessageid).queue();
                player.startTrack(lastTrack.makeClone(), false);
            }

            else {
                if(playingMessageid != 0) PlayCommand.currentChannel.deleteMessageById(playingMessageid).queue();
                nextTrack();
            }

        }
    }


    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }


     public void shuffle() {
        Collections.shuffle((List<?>) queue);
    }
}

