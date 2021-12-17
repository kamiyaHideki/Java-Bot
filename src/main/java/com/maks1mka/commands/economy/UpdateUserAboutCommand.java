package com.maks1mka.commands.economy;

import com.maks1mka.commands.Command;
import com.maks1mka.db.UserCollection;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UpdateUserAboutCommand extends Command {
    UserCollection userCollection = new UserCollection();
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        String arg = String.join(" ", args);
        arg = arg.replace(args[0] + " ", "");
        if(arg.length() >= 200) {
            e.getChannel().sendMessage("Описание должно быть менее 200 символов!").queue();
            return;
        }
        userCollection.updateUserAbout(e.getAuthor().getIdLong(), arg);
        e.getChannel().sendMessage("Описание успешно обновлено!").queue();

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("about", "op");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "UpdateUserAboutCommand";
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
