package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class JoinCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        GuildVoiceState voiceState = e.getMember().getVoiceState();
        AudioManager audio = e.getGuild().getAudioManager();

        if (!voiceState.inVoiceChannel()) {
            e.getChannel().sendMessage("Ты не подключен к голосовому каналу!").queue();
            return;
        }
        audio.openAudioConnection(e.getMember().getVoiceState().getChannel());
        e.getChannel().sendMessage("Я подключилась к " + voiceState.getChannel().getName()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("join", "connect");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "MusicCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "join";
    }
}
