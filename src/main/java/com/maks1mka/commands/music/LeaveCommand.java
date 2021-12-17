package com.maks1mka.commands.music;

import com.maks1mka.commands.Command;
import com.maks1mka.commands.music.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;


import java.util.Arrays;
import java.util.List;

public class LeaveCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        GuildVoiceState voiceState = e.getMember().getVoiceState();
        AudioManager audio = e.getGuild().getAudioManager();
        if (!voiceState.inVoiceChannel()) {
            e.getChannel().sendMessage("Ты не подключен к голосовому каналу!").queue();
            return;
        }
      if(!audio.isConnected()) {
            e.getChannel().sendMessage("Я не подключена к голосовому каналу!").queue();
          return;
        }
      GuildMusicManager manager = PlayCommand.getMusicManager(e.getGuild());
      PlayCommand.musicManagers.values().remove(manager);
      audio.closeAudioConnection();

      return;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("leave");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "LeaveCommand";
    }

    @Override
    public String getCategory() {
        return "Музыка";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "leave";
    }
}
