package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import com.maks1mka.commands.music.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.maks1mka.commands.music.PlayCommand.getMusicManager;

public class PauseCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        GuildMusicManager manager = getMusicManager(e.getGuild());
        if(!manager.player.isPaused()) {
            manager.player.setPaused(true);
            e.getChannel().sendMessage("Трек приостановлен!").queue();
        } else {
            manager.player.setPaused(false);
            e.getChannel().sendMessage("Трек вновь проигрывается!").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("pause");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "PauseCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return null;
    }
}
