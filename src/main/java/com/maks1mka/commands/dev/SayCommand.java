package com.maks1mka.commands.dev;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import com.maks1mka.db.UserCollection;
public class SayCommand extends Command {
    UserCollection userCollection = new UserCollection();
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        String arg = String.join(" ", args);
        arg = arg.replace(args[0], "");
        Object top = userCollection.getTopUsers("msg");
        e.getChannel().sendMessage(top.toString()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("say");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "SayCommand";
    }

    @Override
    public String getCategory() {
        return "Разработчик";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "say <words>";
    }
}
