package com.maks1mka.commands.user;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.maks1mka.commands.handler.getJda;
import static com.maks1mka.commands.music.PlayCommand.getTimestamp;

public class UserInfoCommand extends Command  {

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        Message message = event.getMessage();
        Member member;
        if (event.getMessage().getMentionedRoles().size() != 0) {
            event.getChannel().sendMessage("Ты упомянуль роль, а не пользователя!").queue();
            return;
        }
        member = message.getMentionedUsers().size() == 0
                ? (args.length == 2
                ? event.getMessage().getGuild().getMemberById(args[1])
                : message.getMember())
                : message.getMentionedMembers().get(0);
        String userStatus = "";
        if(member.getOnlineStatus() != null) userStatus = member.getOnlineStatus().name();
        switch (userStatus) {
            case "DO_NOT_DISTURB":
                Emote dnd = getJda().getGuildById("900129288366481439").getEmotesByName("dnd", false).get(0);
                userStatus = dnd.getAsMention() + "Не беспокоить";
                break;
            case "OFFLINE":
                Emote off = getJda().getGuildById("900129288366481439").getEmotesByName("off", false).get(0);
                userStatus = off.getAsMention() + "Не в сети";
                break;
            case "ONLINE":
                Emote on = getJda().getGuildById("900129288366481439").getEmotesByName("on", false).get(0);
                userStatus = on.getAsMention() + "В сети";
                break;
            case "IDLE":
                Emote idle = getJda().getGuildById("900129288366481439").getEmotesByName("idle", false).get(0);
                userStatus = idle.getAsMention() + "Не активен";
                break;
        }
        String userActivityString = "_Активности нет_";
        if (member.getActivities().size() != 0) {
            Activity userActivity = member.getActivities().get(0);
            String typeActivity = userActivity.getType().name();
            switch (typeActivity){
                case "WATCHING":
                    typeActivity = "Смотрит ";
                    break;
                case "LISTENING":
                    typeActivity = "Слушает ";
                    break;
                case "STREAMING":
                    typeActivity = "Стримит ";
                    break;
                case "DEFAULT":
                    typeActivity = "Играет в ";
                    break;
                case "CUSTOM_STATUS":
                    typeActivity = (userActivity.getEmoji() != null ? userActivity.getEmoji().getAsMention() : "") + " ";
                    break;
            }
            String nameActivity = typeActivity + userActivity.getName();
            if (userActivity.getTimestamps() != null) {
                long startTime = userActivity.getTimestamps().getStart();
                long currentTime = new Date().getTime();
                userActivityString = nameActivity + "\nПрошло: " + (getTimestamp(currentTime - startTime));
            } else {
                userActivityString = nameActivity;
            }
        }



        MessageEmbed userEmbed = new EmbedBuilder()
                .setTitle("Информация о " + member.getUser().getAsTag())
                .setColor(member.getUser().retrieveProfile().complete().getAccentColor())
                .setThumbnail(member.getUser().getAvatarUrl())
                .addField("Зарегистрировался", "<t:" + member.getTimeCreated().toZonedDateTime().toEpochSecond()+ ":F>", false)
                .addField("Вошел на сервер", "<t:" + member.getTimeJoined().toZonedDateTime().toEpochSecond() + ":F>", false)
                .addField("Статус", userStatus, true)
                .addField("Активность", userActivityString, true)
                //.addField("Роли", String.join(", ", ), false)
                .build();
        message.getChannel().sendMessageEmbeds(userEmbed).queue();
    }

    @Override
    public String getName() {
        return "UserInfoCommand";
    }

    @Override
    public String getCategory() {
        return "Информация";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "user или " + getPrefix() + "user <@user>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("user", "userinfo");
    }

    @Override
    public String getDescription() {
        return "Информация";
    }



}
