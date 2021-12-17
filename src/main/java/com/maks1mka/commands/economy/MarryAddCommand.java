package com.maks1mka.commands.economy;

import com.maks1mka.commands.Command;
import com.maks1mka.db.UserCollection;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MarryAddCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        UserCollection userCollection = new UserCollection();
        Message message = e.getMessage();
        Member member;
        if(args.length == 1) {
            message.getChannel().sendMessage("Ты не указал человека").queue();
            return;
        }
        if (e.getMessage().getMentionedRoles().size() != 0) {
            message.getChannel().sendMessage("Ты упомянуль роль, а не пользователя!").queue();
            return;
        }
        member = message.getMentionedUsers().size() == 0 ?
                e.getMessage().getGuild().getMemberById(args[1]) :
                message.getMentionedMembers().get(0);
        if(member.getUser() == e.getAuthor()) {
            message.getChannel().sendMessage("Нельзя жениться на себе!").queue();
            return;
        }
        if(member.getUser().isBot()) {
            message.getChannel().sendMessage("Нельзя жениться на боте!").queue();
            return;
        }
        if(userCollection.gerUserById(member.getUser().getIdLong()).getLong("marry") != 1L || userCollection.gerUserById(e.getAuthor().getIdLong()).getLong("marry") != 1L) {
            /**
                if(userCollection.gerUserById(member.getUser().getIdLong()).getLong("marry") == e.getAuthor().getIdLong()) {
                    message.getChannel().sendMessage("Этот пользователь уже состоит в браке с тобой!").queue();
                    return;
                }
            **/
            message.getChannel().sendMessage("Ты или этот пользователь уже состоят в браке!").queue();
            return;
        }
        userCollection.updateUserInfo(e.getAuthor().getIdLong(), "marry", member.getUser().getIdLong());
        userCollection.updateUserInfo(member.getUser().getIdLong(), "marry", e.getAuthor().getIdLong());
        message.getChannel().sendMessage("Ура! " + e.getAuthor().getAsMention() + " и " + member.getAsMention() + " теперь в браке!").queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("marry");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "MarryAddCommand";
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
