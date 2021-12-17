package com.maks1mka.commands.economy;

import com.maks1mka.commands.Command;
import com.maks1mka.db.UserCollection;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MarryDelCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        UserCollection userCollection = new UserCollection();
        Message message = e.getMessage();
        Member member;
        long memberId = userCollection.gerUserById(e.getAuthor().getIdLong()).getLong("marry");
        if(memberId == 1) {
            message.getChannel().sendMessage("Ты не состоишь в браке!").queue();
            return;
        }
        member = e.getGuild().getMemberById(memberId);
        userCollection.updateUserInfo(e.getAuthor().getIdLong(), "marry", 1);
        userCollection.updateUserInfo(member.getUser().getIdLong(), "marry", 1);
        message.getChannel().sendMessage("Печаль! " + e.getAuthor().getAsMention() + " и " + member.getAsMention() + " развелись").queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("divorce");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "MarryDelCommand";
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
