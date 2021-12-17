package com.maks1mka.listeners;

import com.mongodb.client.FindIterable;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.maks1mka.db.UserCollection;
import org.bson.Document;

import java.util.Objects;

import static com.maks1mka.commands.Command.getPrefix;
import static com.maks1mka.commands.handler.getJda;

public class MessageCreate extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getContentDisplay().startsWith(getPrefix())) return;
        UserCollection userCollection = new UserCollection();
        Document user = userCollection.gerUserById(event.getAuthor().getIdLong());
        Long xp = user.getLong("xp");
        Long lvl = user.getLong("lvl");
        Long msg = user.getLong("msg");
        Long currency = user.getLong("currency");
        Long formula = 5 * (lvl * lvl) + (50 * lvl) + 100;
        if(xp >= formula){
            Long randomCurrency = (long)(Math.random() * ((100 - 10) + 1)) + 10;
            userCollection.updateUserInfo(event.getAuthor().getIdLong(), "currency", currency+randomCurrency);
            userCollection.updateUserInfo(event.getAuthor().getIdLong(), "lvl", lvl++);
            userCollection.updateUserInfo(event.getAuthor().getIdLong(), "xp", 0);
            Emote haze = Objects.requireNonNull(getJda().getGuildById(900129288366481439L)).getEmotesByName("haze", false).get(0);
            event.getJDA().getTextChannelById(909814237923254342L).sendMessage("Ты поднял свой уровень! Вот держи - " + randomCurrency + " " + haze.getAsMention()).queue();
        } else {
            userCollection.updateUserInfo(event.getAuthor().getIdLong(), "xp", xp+(long)(Math.random() * ((7 - 1) + 1)) + 1);
            userCollection.updateUserInfo(event.getAuthor().getIdLong(), "currency", currency+(long)(Math.random() * ((10 - 1) + 1)) + 1);
        }
        userCollection.updateUserInfo(event.getAuthor().getIdLong(), "msg", msg+1);
    }
}
