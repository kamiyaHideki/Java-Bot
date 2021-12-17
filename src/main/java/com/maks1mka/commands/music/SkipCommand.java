package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class SkipCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage("Песня пропущена!").queue();
        PlayCommand.getMusicManager(e.getGuild()).scheduler.nextTrack();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("skip");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "SkipCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "skip";
    }
}
