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

public class SfwCommand extends Command {
    private final String[] sfwTags = {"art", "baka", "cuddle", "fox_girl", "glare", "highfive", "holo", "kemonomimi", "smug", "loli", "meow", "neko", "ngif", "nom",  "pressf", "waifu", "wallpaper", "wasted", "woof"};

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) throws IOException {
        String tag = args[0].toLowerCase();
        MessageChannel channel = event.getMessage().getChannel();
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
        return Arrays.asList(sfwTags);
    }

    @Override
    public String getDescription()
    {
        return "Арты не 18+";
    }

    @Override
    public String getName()
    {
        return "SfwCommand";
    }

    @Override
    public String getCategory(){
        return "Арты";
    }

    @Override
    public String getUsage() {
        return null;
    }
}

