package com.maks1mka.commands.economy;

import com.maks1mka.commands.Command;
import com.maks1mka.db.UserCollection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.maks1mka.commands.handler.getJda;

public class EconomyProfileCommand extends Command {
    UserCollection userCollection = new UserCollection();
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        Message message = e.getMessage();
        Member member;
        member = message.getMentionedUsers().size() == 0
                ? (args.length == 2
                ? message.getGuild().getMemberById(args[1])
                : message.getMember())
                : message.getMentionedMembers().get(0);
        Document memberInfo = userCollection.gerUserById(member.getUser().getIdLong());
        int xp = memberInfo.getInteger("xp");
        int lvl = memberInfo.getInteger("lvl");
        int haze = memberInfo.getInteger("currency");
        int msg = memberInfo.getInteger("msg");
        String about = memberInfo.getString("about") == null ? "_Пусто_" : memberInfo.getString("about");

        int maxXp = 5 * (lvl * lvl) + (50 * lvl) + 100;
        Emote lvlEmote = Objects.requireNonNull(getJda().getGuildById("877678864585850940")).getEmotesByName("lvl", false).get(0);
        Emote hazeEmote = Objects.requireNonNull(getJda().getGuildById("900129288366481439")).getEmotesByName("haze", false).get(0);
        EmbedBuilder profileEmbedBuilder = new EmbedBuilder()
                .setTitle("Профиль " + member.getUser().getName())
                .setThumbnail(member.getUser().getAvatarUrl())
                .addField("Опыт", "```" + xp + " / " + maxXp + " | " + (maxXp-xp) +  " осталось```", false)
                .addField("Уровень", lvl + " " + lvlEmote.getAsMention(), true)
                .addField("Валюта", haze + " " + hazeEmote.getAsMention(), true)
                .addField("Сообщения", msg + " :speech_balloon:", true)
                .addField("Обо мне", about, false);

        message.getChannel().sendMessageEmbeds(profileEmbedBuilder.build()).queue();

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("profile", "pr");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "EconomyProfileCommand";
    }

    @Override
    public String getCategory() {
        return "Экономика";
    }

    @Override
    public String getUsage() {
        return null;
    }
}
