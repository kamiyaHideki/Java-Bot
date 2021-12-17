package com.maks1mka.commands.user;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AvatarCommand extends Command{
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
        String size = "4096";
        String ava = user.getEffectiveAvatarUrl() + "?size=" + size;
        String nickName = Objects.requireNonNull(event.getMember()).getNickname();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Аватарка " + user.getName())
                .setImage(ava)
                .setColor(user.retrieveProfile().complete().getAccentColorRaw())
                //.setColor(0x7000cc)
                .setFooter("Для " + (nickName == null ? event.getAuthor().getName() : nickName), event.getAuthor().getAvatarUrl())
                .build();
        channel.sendMessageEmbeds(embed).queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ava", "avatar");
    }

    @Override
    public String getDescription() {
        return "Твоя аватарка";
    }

    @Override
    public String getName() {
        return "Avatar Command";
    }

    @Override
    public String getCategory() {
        return "Информация";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "avatar или " + getPrefix() + "avatar @человек";
    }

}
