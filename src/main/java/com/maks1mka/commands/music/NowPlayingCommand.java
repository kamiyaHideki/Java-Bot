package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import com.maks1mka.commands.music.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.maks1mka.commands.music.PlayCommand.getMusicManager;
import static com.maks1mka.commands.music.PlayCommand.getTimestamp;

public class NowPlayingCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        GuildMusicManager manager = getMusicManager(e.getGuild());
        AudioTrack currentTrack = manager.player.getPlayingTrack();
        if (currentTrack != null)
        {
            Member currentAuthor = e.getMember();
            String title = currentTrack.getInfo().title;
            String uri = currentTrack.getInfo().uri;
            String position = getTimestamp(currentTrack.getPosition());
            String duration = getTimestamp(currentTrack.getDuration());
            EmbedBuilder npEmbed = new EmbedBuilder()
                    .setTitle("Проигрывается")
                    .setDescription("**" + title + "**")
                    .addField("Время", position + "/" + duration, true)
                    .addField("Ссылка", uri, true)
                    .setColor(currentAuthor.getUser().retrieveProfile().complete().getAccentColor())
                    .setFooter( "Запросил " + (currentAuthor.getNickname() == null ? currentAuthor.getUser().getName() : currentAuthor.getNickname()), currentAuthor.getUser().getAvatarUrl());

            e.getChannel().sendMessageEmbeds(npEmbed.build()).queue();
        }
        else {
            String notNp = "Сейчас ничего не проигрывается!";
            e.getChannel().sendMessage(notNp).queue();
        }

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("np");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "NowPlayingCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "np";
    }
}
