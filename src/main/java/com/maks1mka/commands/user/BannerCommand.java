package com.maks1mka.commands.user;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BannerCommand extends Command{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        Message message = event.getMessage();
        MessageChannel channel = message.getChannel();

        User user;
        user = message.getMentionedUsers().size() == 0
                ? (args.length == 2
                ? event.getJDA().retrieveUserById(args[1]).complete()
                : message.getAuthor())
                : message.getMentionedUsers().get(0);
        if(user.retrieveProfile().complete().getBannerId() == null) {
            MessageEmbed errorEmbed = new EmbedBuilder()
                    .setDescription("**Ошибка!**\nУ пользователя **" + user.getAsTag() + "** нет баннера")
                    .setColor(Color.RED)
                    .build();
            channel.sendMessageEmbeds(errorEmbed).queue();
            return;
        }
        String size = "4096";
        String banner = user.retrieveProfile().complete().getBannerUrl() + "?size=" + size;
        String nickName = Objects.requireNonNull(event.getMember()).getNickname();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Баннер " + user.getName())
                .setImage(banner)
                .setColor(user.retrieveProfile().complete().getAccentColor())
                .setFooter("Для " + (nickName == null ? event.getAuthor().getName() : nickName), event.getAuthor().getAvatarUrl())
                .build();
        channel.sendMessageEmbeds(embed).queue();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("banner");
    }

    @Override
    public String getDescription() {
        return "Баннер";
    }

    @Override
    public String getName() {
        return "Banner Command";
    }

    @Override
    public String getCategory() {
        return "Информация";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "banner или " + getPrefix() + "banner <@user>";
    }
}
