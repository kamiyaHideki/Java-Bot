package com.maks1mka.commands;

import com.maks1mka.commands.info.*;
import com.maks1mka.commands.user.*;
import com.maks1mka.commands.dev.*;
import com.maks1mka.commands.music.*;
import com.maks1mka.commands.fun.*;
import com.maks1mka.commands.economy.*;
import com.maks1mka.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class handler {
    static JDA jda;
    public static void preInit(JDABuilder api) {
        api.setActivity(Activity.playing(Command.getPrefix() + "help | " + Command.getVersion()));
        api.addEventListeners(
                new ReadyEvent(),
                new MessageCreate(),
                new MemberLeave()
        );
        System.out.println("______________________________________________________________________");
        System.out.println("Events loaded successfully");
    }
    public static void commands(JDABuilder api) {
        HelpCommand help = new HelpCommand();
        api.addEventListeners(help);
        api.addEventListeners(
                help.registerCommand(new AvatarCommand()),
           //     help.registerCommand(new PingCommand()),
                help.registerCommand(new EvalCommand()),
          //      help.registerCommand(new NsfwCommand()),
            //    help.registerCommand(new SfwCommand()),
             //   help.registerCommand(new ActionCommand()),
                help.registerCommand(new BannerCommand()),
                help.registerCommand(new UserInfoCommand()),
              //  help.registerCommand(new ServerInfoCommand()),
                help.registerCommand(new JoinCommand()),
                help.registerCommand(new LeaveCommand()),
                help.registerCommand(new PlayCommand()),
                help.registerCommand(new SearchCommand()),
                help.registerCommand(new SkipCommand()),
                help.registerCommand(new ListCommand()),
                help.registerCommand(new LoopCommand()),
                help.registerCommand(new NowPlayingCommand()),
                help.registerCommand(new PauseCommand()),
             //   help.registerCommand(new SayCommand()),
             //   help.registerCommand(new EconomyProfileCommand()),
           //     help.registerCommand(new UpdateUserAboutCommand())
                help.registerCommand(new MarryAddCommand()),
                help.registerCommand(new MarryDelCommand())
        );
        System.out.println("Commands loaded successfully");
    }

    public static void setJda(JDA jda) {
        handler.jda = jda;
    }

    public static JDA getJda() {
        return jda;
    }
}