package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import com.maks1mka.commands.music.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

public class PlayCommand extends Command {

    JoinCommand join = new JoinCommand();
    public  static Map<String, GuildMusicManager> musicManagers;
    private static AudioPlayerManager playerManager;
    public static MessageChannel currentChannel;
    public static Member currentAuthor;
    public static Guild currentGuild;
    public static final int DEFAULT_VOLUME = 35; //(0 - 150, where 100 is default max volume)
    public PlayCommand() {
        java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

        playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        musicManagers = new HashMap<>();
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        GuildVoiceState voiceState = e.getMember().getVoiceState();
        AudioManager audio = e.getGuild().getAudioManager();
        GuildMusicManager manager = getMusicManager(e.getGuild());
        currentChannel = e.getChannel();
        currentAuthor = e.getMember();
        currentGuild = e.getGuild();
        if(!voiceState.inVoiceChannel()) {
            e.getChannel().sendMessage("Ты не подключен к голосовому каналу!").queue();
            return;
        }
        if (args.length == 1) {
            e.getChannel().sendMessage("Ты не указал какую песню играть!").queue();
            return;
        }
        if(!audio.isConnected() || audio.getConnectedChannel() != voiceState.getChannel()) {
            join.onCommand(e, null);
        }
        String arg = String.join(" ", args);
        arg = arg.replace(args[0] + " ", "");
        System.out.println(arg);
        audio.setSendingHandler(manager.sendHandler);
        loadAndPlay(manager, e.getChannel(), arg, false);

    }

    public static void loadAndPlay(GuildMusicManager manager, final  MessageChannel channel, String url, final boolean isSearchCommand) {
        final  String trackUrl;
        if(!isSearchCommand) {
            if (url.startsWith("<") && url.endsWith(">"))  url = url.substring(1, url.length() - 1);
            if(!url.startsWith("https://") && !url.startsWith("http://")) trackUrl = "ytsearch:" + url;
            else trackUrl = url;

        }
        else {
            trackUrl = "ytsearch:" + url;
        }
        playerManager.loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                manager.scheduler.queue(track, false);
            }
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if(!playlist.isSearchResult()){
                    AudioTrack selectedTrackTrack = playlist.getSelectedTrack();
                    List<AudioTrack> tracks = playlist.getTracks();
                    AtomicLong durationPlaylist = new AtomicLong();
                    if(selectedTrackTrack != null) {
                        manager.scheduler.queue(selectedTrackTrack, false);
                        return;
                    }
                    if(tracks.size() <= 100) {
                        tracks.forEach((AudioTrack track) -> durationPlaylist.addAndGet(track.getDuration()));
                        MessageEmbed playlistAddEmbed = new EmbedBuilder()
                                .setTitle("В очередь добавлено")
                                .addField("Количество треков", "" + tracks.size(), false)
                                .addField("Длительность плейлиста", ""+getTimestamp(durationPlaylist.get()), false)
                                .setColor(currentAuthor.getUser().retrieveProfile().complete().getAccentColor())
                                .setFooter( "Запросил " + (currentAuthor.getNickname() == null ? currentAuthor.getUser().getName() : currentAuthor.getNickname()), currentAuthor.getUser().getAvatarUrl())
                                .build();
                        channel.sendMessageEmbeds(playlistAddEmbed).queue();
                        tracks.forEach((AudioTrack track) -> manager.scheduler.queue(track, true));
                    }
                    else {
                        channel.sendMessage("Невозможно добавить в очередь! В этом плейлисте более 100 треков!").queue();
                    }
                } else {
                    if(!isSearchCommand) {
                        AudioTrack track = playlist.getTracks().get(0);
                        manager.scheduler.queue(track, false);
                    }
                    else {

                        AtomicLong counter = new AtomicLong();
                        EmbedBuilder searchEmbed = new EmbedBuilder()
                                .setTitle("Результаты поиска");
                        counter.addAndGet(1);
                        List<AudioTrack> searchResult = playlist.getTracks().subList(0, 5);
                        Map<Long, Button> buttonMap;
                        buttonMap = new HashMap<>();
                        searchResult.forEach(track -> {
                            searchEmbed.addField(counter + ")" + track.getInfo().title + " [" + getTimestamp(track.getDuration()) + "]", track.getInfo().uri, false);
                            buttonMap.put(counter.get(), Button.primary(track.getInfo().uri,counter.get()+""));
                            counter.addAndGet(1);
                        });

                        channel.sendMessageEmbeds(searchEmbed.build()).setActionRow(buttonMap.values()).queue();

                    }

                }

            }
            @Override
            public void noMatches()
            {

                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        AudioManager audio = event.getGuild().getAudioManager();
        GuildMusicManager manager = getMusicManager(event.getGuild());
        audio.setSendingHandler(manager.sendHandler);
        currentChannel = event.getChannel();
        currentAuthor = event.getMember();
        loadAndPlay(manager, currentChannel, event.getComponentId(), false);
        event.getMessage().delete().queue();
        return;
    }
    public static GuildMusicManager getMusicManager(Guild guild) {

        String guildId = guild.getId();
        GuildMusicManager manager = musicManagers.get(guildId);
        if (manager == null /*|| manager.scheduler.checkQueue() || manager.player.getPlayingTrack() == null*/) {

            synchronized (musicManagers) {

                manager = musicManagers.get(guildId);
                if (manager == null /*|| manager.scheduler.checkQueue()  || manager.player.getPlayingTrack() == null*/) {

                    manager = new GuildMusicManager(playerManager);
                    manager.player.setVolume(DEFAULT_VOLUME);
                    musicManagers.put(guildId, manager);
                }
            }
        }
        return manager;
    }

    public static String getTimestamp(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }
    @Override
    public List<String> getAliases() {
        return Arrays.asList("play", "p");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "PlayCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "play <link> или <title>";
    }
}
