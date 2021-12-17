package com.maks1mka.commands.fun;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class NsfwCommand extends Command {
    private final String[] nsfwTags = {"anal", "bj", "blowjob", "boobs", "ego", "ero", "erofeet", "erok", "erokemo", "eroyuri", "feet", "feetg", "futanari", "hentai_gif", "holoero", "hololewd", "kuni", "les", "lewd", "pussy", "pwankg", "sex", "solo", "tits", "trap", "yuri"};
    
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) throws IOException {
        MessageChannel channel = event.getChannel();
        if (!event.getTextChannel().isNSFW()) {
            String text = "У этого канала нет пометки `fun`!";
            channel.sendMessage(text).queue();
            return;
        }
        String tag = args[0].toLowerCase();
        String coreUrl = "http://discord-holo-api.ml/api/";
        URL url = new URL(coreUrl + tag);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        StringBuilder content_url;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            content_url = new StringBuilder();
            while ((line = input.readLine()) != null) {
                content_url.append(line);
                content_url.append(System.lineSeparator());
            }
        } finally {
            connection.disconnect();
        }
        String getFileName = content_url.toString().split("/")[content_url.toString().split("/").length-1].replace("\"}", "");
        String imageUrl = coreUrl + tag + "/" + getFileName;
        String nickName = Objects.requireNonNull(event.getMember()).getNickname();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(toFirstUpperCase(tag))
                .setImage(imageUrl)
                .setColor(Color.MAGENTA)
                .setFooter("Для " + (nickName == null ? event.getAuthor().getName() : nickName), event.getMember().getAvatarUrl())
                .build();
        channel.sendMessageEmbeds(embed).queue();
    }
    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(nsfwTags);
    }

    @Override
    public String getDescription()
    {
        return "Арты 18+";
    }

    @Override
    public String getName()
    {
        return "NsfwCommand";
    }

    @Override
    public String getCategory() {
        return "Арты 18+";
    }

    @Override
    public String getUsage() {
        return null;
    }
}
