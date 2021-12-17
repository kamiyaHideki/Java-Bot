package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import com.maks1mka.commands.music.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static com.maks1mka.commands.music.PlayCommand.getTimestamp;
import static com.maks1mka.commands.music.PlayCommand.getMusicManager;

public class ListCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args)  {
        GuildMusicManager manager = getMusicManager(e.getGuild());
        Queue<AudioTrack> queue = manager.scheduler.getQueue();
        synchronized (queue)
        {
            if (queue.isEmpty())
            {
                e.getChannel().sendMessage("Очередь пуста!").queue();
            }
            else
            {
                int trackCount = 0;
                long queueLength = 0;
                StringBuilder sb = new StringBuilder();
                sb.append("Первые 30 треков очереди").append("\n");
                for (AudioTrack track : queue)
                {
                    queueLength += track.getDuration();
                    if (trackCount < 30)
                    {
                        sb.append( "**" + (trackCount+1) + ") [" + track.getInfo().title + "](" + track.getInfo().uri + ")** (" + getTimestamp(track.getDuration()) + ")").append("\n");
                        trackCount++;
                    } else break;
                }
                sb.append("\n").append("Треков в очереди: ").append(queue.size()).append("\n");
                sb.append("Длительность очереди: ").append(getTimestamp(queueLength));
                Member currentAuthor = e.getMember();
                MessageEmbed listEmbed = new EmbedBuilder()
                        .setTitle("Очередь треков")
                        .setDescription(sb.toString())
                        .setColor(currentAuthor.getUser().retrieveProfile().complete().getAccentColor())
                        .setFooter( "Запросил " + (currentAuthor.getNickname() == null ? currentAuthor.getUser().getName() : currentAuthor.getNickname()), currentAuthor.getUser().getAvatarUrl())
                        .build();
                e.getChannel().sendMessageEmbeds(listEmbed).queue();
            }
        }

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("list");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "ListCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "list";
    }
}
