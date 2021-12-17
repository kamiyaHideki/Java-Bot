package com.maks1mka.commands;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.List;

public abstract class Command extends ListenerAdapter {
    public abstract void onCommand(MessageReceivedEvent e, String[] args) throws IOException;
    public abstract List<String> getAliases();
    public abstract String getDescription();
    public abstract String getName();
    public abstract String getCategory();
    public abstract String getUsage();
    public static String getPrefix() {
        return "!";
    }

    public static String getVersion() {
        return "v0.1";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (!e.getMessage().getContentDisplay().startsWith(getPrefix())) return;
        String cmdName = e.getMessage().getContentDisplay().replace(getPrefix() , "").split(" ")[0];
        String[] cmdArgs = e.getMessage().getContentDisplay().replace(getPrefix() , "").split(" ");
        if (containsCommand(cmdName)) {
            try {
                onCommand(e, cmdArgs);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    protected boolean containsCommand(String cmdName) {
        return getAliases().contains(cmdName);
    }



    protected String toFirstUpperCase(String word){
        if(word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}