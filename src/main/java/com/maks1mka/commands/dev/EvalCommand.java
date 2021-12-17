package com.maks1mka.commands.dev;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collections;
import java.util.List;

public class EvalCommand extends Command {
    private final ScriptEngine engine;

    public EvalCommand()
    {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        try
        {
            engine.eval("var imports = new JavaImporter(" +
                    "java.io," +
                    "java.lang," +
                    "java.util," +
                    "Packages.net.dv8tion.jda.api," +
                    "Packages.net.dv8tion.jda.api.entities," +
                    "Packages.net.dv8tion.jda.api.entities.impl," +
                    "Packages.net.dv8tion.jda.api.managers," +
                    "Packages.net.dv8tion.jda.api.managers.impl," +
                    "Packages.net.dv8tion.jda.api.utils);");
        }
        catch (ScriptException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args)
    {

        if (!e.getAuthor().getId().equals("598151504867753996"))
            return;


        try
        {
            engine.put("event", e.getGuild());
            engine.put("message", e.getMessage());
            engine.put("channel", e.getChannel());
            engine.put("args", args);
            engine.put("api", e.getJDA());
            if (e.isFromType(ChannelType.TEXT))
            {
                engine.put("guild", e.getGuild());
                engine.put("member", e.getMember());
            }

            Object out = engine.eval(
                    "(function() {" +
                            "with (imports) {" +
                            e.getMessage().getContentDisplay().substring((Command.getPrefix() + args[0]).length()) +
                            "}" +
                            "})();");
            e.getChannel().sendMessage(out == null ? "Выполнено без ошибок." : out.toString()).queue();
        }
        catch (Exception e1)
        {
            e.getChannel().sendMessage(e1.getMessage()).queue();
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList("eval");
    }

    @Override
    public String getDescription()
    {
        return "Takes Java and executes it.";
    }

    @Override
    public String getName()
    {
        return "Evaluate";
    }

    @Override
    public String getCategory() {
        return "Разработчик";
    }

    @Override
    public String getUsage() {
        return getPrefix() + "eval <code>";
    }


}
