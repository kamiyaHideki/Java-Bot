package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import com.maks1mka.commands.music.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.maks1mka.commands.music.PlayCommand.getMusicManager;

public class SearchCommand extends Command {
    JoinCommand join = new JoinCommand();
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) throws IOException {
        GuildMusicManager manager = getMusicManager(e.getGuild());
        AudioManager audio = e.getGuild().getAudioManager();
        GuildVoiceState voiceState = Objects.requireNonNull(e.getMember()).getVoiceState();
        if(!voiceState.inVoiceChannel()){
            e.getChannel().sendMessage("Ты не подключен к голосовому каналу").queue();
            return;
        }
        if(args[1] == null) {
            e.getChannel().sendMessage("Ты не ввел запрос").queue();
            return;
        }
        if(!audio.isConnected() || audio.getConnectedChannel() != voiceState.getChannel()) {
            join.onCommand(e, args);
        }
        String arg = String.join(" ", args);
        arg = arg.replace(args[1] + " ", "");
        PlayCommand.loadAndPlay(manager, e.getChannel(), arg, true);

    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("search");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "SearchCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "search <title>";
    }
}

