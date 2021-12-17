package com.maks1mka.commands.dev;


import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PingCommand extends Command {
        @Override
        public void onCommand(MessageReceivedEvent event, String[] args) {
            Message message = event.getMessage();
            MessageChannel channel = message.getChannel();
            String ping = event.getJDA().getGatewayPing() + " ms";
            MessageEmbed embed = new EmbedBuilder()
                    .setDescription("**Задержка**\n`" + ping + "`")
                    .setColor(Color.MAGENTA)
                    .build();
            channel.sendMessageEmbeds(embed).queue();

        }
        @Override
        public List<String> getAliases()
        {
            return Arrays.asList("ping", "pg");
        }

        @Override
        public String getDescription()
        {
            return "Проверить задержку";
        }

        @Override
        public String getName()
        {
            return "PingCommand";
        }

        @Override
        public String getCategory() {
            return "Разработчик";
        }

    @Override
    public String getUsage() {
        return getPrefix() + "ping";
    }

}

