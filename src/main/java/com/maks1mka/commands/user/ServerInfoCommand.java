package com.maks1mka.commands.user;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerInfoCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        Guild guild = e.getGuild();
        String nickName = Objects.requireNonNull(e.getMember()).getNickname();
        EmbedBuilder serverEmbed = new EmbedBuilder()
                .setTitle("Информация о сервере " + guild.getName())
                .setFooter("Для " + (nickName == null ? e.getAuthor().getName() : nickName), e.getAuthor().getAvatarUrl());
        e.getChannel().sendMessageEmbeds(serverEmbed.build()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("server", "serverinfo");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "ServerCommand";
    }

    @Override
    public String getCategory() {
        return "Информация";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "server";
    }
}
