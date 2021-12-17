package com.maks1mka.commands.fun;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ActionCommand extends Command {
    final String[] actionsNames = {"ask", "bite", "cry", "cum", "dance", "feed", "hug", "kiss", "lick", "pat", "poke", "punch", "slap", "tickle", "wink"};
    final TreeMap<String, String> actionsKeys;

    public ActionCommand() {
        actionsKeys = new TreeMap<>();
        actionsKeys.put("bite", "укусил(-а)");
        actionsKeys.put("hug", "обнял(-а)");
        actionsKeys.put("kiss", "поцеловал(-а)");
        actionsKeys.put("cry", "поплакал(-а) вместе с");
        actionsKeys.put("dance", "потанцевал(-а) вместе с");
        actionsKeys.put("pat", "погладил(-а)");
        actionsKeys.put("feed", "покормил(-а)");
        actionsKeys.put("lick", "лизнул(-а)");
        actionsKeys.put("poke", "тыкнул(-а)");
        actionsKeys.put("punch", "ударил(-а)");
        actionsKeys.put("slap", "дал(-а) пощечину");
        actionsKeys.put("tickle", "пощекотал(-а)");
        actionsKeys.put("wink", "подмигнул(-а)");
        actionsKeys.put("ask", "вопросительно посмотрел(-а) на");
        actionsKeys.put("cum", "кончил(-а) в");
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        Message message = e.getMessage();
        User user;
        user = message.getMentionedUsers().size() == 0
                ? (args.length == 2
                ? e.getJDA().retrieveUserById(args[1]).complete()
                : message.getAuthor())
                : message.getMentionedUsers().get(0);
        if(e.getAuthor().equals(user)) {
            e.getChannel().sendMessage("Ты не можешь это сделать на себе!");
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

        EmbedBuilder actionEmbedBuilder = new EmbedBuilder()
                .setDescription("**" + e.getAuthor().getAsMention() + " " + actionsKeys.get(tag) + " " + user.getAsMention() + "**")
                .setImage(imageUrl);

        e.getChannel().sendMessageEmbeds(actionEmbedBuilder.build()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(actionsNames);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "ActionCommand";
    }

    @Override
    public String getCategory() {
        return "Действия";
    }

    @Override
    public String getUsage() {
        return null;
    }
}
