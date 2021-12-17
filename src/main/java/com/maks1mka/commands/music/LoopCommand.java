package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import com.maks1mka.commands.music.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.maks1mka.commands.music.PlayCommand.getMusicManager;

public class LoopCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        GuildMusicManager manager = getMusicManager(e.getGuild());
        manager.scheduler.setRepeating(!manager.scheduler.isRepeating());
        e.getChannel().sendMessage("Повторение трека **" + (manager.scheduler.isRepeating() ? "включено" : "выключено") + "**").queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("loop");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "LoopCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "loop";
    }
}
