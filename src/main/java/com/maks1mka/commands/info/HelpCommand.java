package com.maks1mka.commands.info;

import com.maks1mka.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class HelpCommand extends Command {

    private final TreeMap<String, Command> commands;

    public HelpCommand() {
        commands = new TreeMap<>();
        System.out.println("CommandsMap created successfully");
    }
    public Command registerCommand(Command command)
    {
        commands.put(command.getAliases().get(0), command);
        return command;
    }


    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        if(args.length == 1){
            ArrayList<String> categories = new ArrayList<>(1);

            for (Command c : commands.values()) {
                String category = c.getCategory();
                category = (category == null || category.isEmpty()) ? "NO_CATEGORY" : category;
                if (!categories.contains(category)) categories.add(category);

            }
            EmbedBuilder helpEmbedBuilder = new EmbedBuilder()
                    .setTitle("Помощь по командам!")
                    .setDescription("Мой префикс `" + getPrefix()  + "`\n" + "Справка по конкретной команде: `" + getPrefix()  + "help [command]" + "`\n");
            for (String category : categories) {
                StringBuilder commandsCategory = new StringBuilder();
                int commandsCount = 0;
                for (Command cmd : commands.values()) {
                    if (cmd.getCategory().equals(category)) {
                        if (cmd.getName() == "NsfwCommand" || cmd.getName() == "SfwCommand" || cmd.getName() == "ActionCommand" ) {
                            commandsCount = cmd.getAliases().size();
                            for (String alias : cmd.getAliases()) {
                                commandsCategory.append("`").append(alias).append("`, ");
                            }
                        } else {
                            commandsCount += 1;
                            commandsCategory.append("`").append(cmd.getAliases().get(0)).append("`, ");
                        }
                    }

                }
                commandsCategory = new StringBuilder(commandsCategory.substring(0, commandsCategory.length() - 2));
                helpEmbedBuilder.addField(category + " (" + commandsCount + ")", commandsCategory.toString(), false);
            }
            String nickName = Objects.requireNonNull(event.getMember()).getNickname();
            MessageEmbed helpEmbed = helpEmbedBuilder
                    .setFooter("Для " + (nickName == null ? event.getAuthor().getName() : nickName), event.getAuthor().getAvatarUrl())
                    .build();
            event.getChannel().sendMessageEmbeds(helpEmbed).queue();

            return;
        }
        for(Command cmd: commands.values()) {
            if(cmd.getAliases().contains(args[1])){
                String usage = cmd.getUsage() == null ? "Пусто" : cmd.getUsage();
                String desc = cmd.getDescription() == null ? "Пусто" : cmd.getDescription();
                String aliases = args[1];
                if(!cmd.getName().equals("SfwCommand") || !cmd.getName().equals("NsfwCommand") || !cmd.getName().equals("ActionCommand")) {
                    aliases = String.join(", ", cmd.getAliases());
                }
                String nickName = Objects.requireNonNull(event.getMember()).getNickname();
                EmbedBuilder helpCommandEmbed = new EmbedBuilder()
                        .setTitle("Справка о команде " + cmd.getAliases().get(0))
                        .setDescription("**Описание команды:** " + desc + "\n**Использование:** `" + usage + "`\n**Псевдонимы:** `" + aliases + "`")
                        .setFooter("Для " + (nickName == null ? event.getAuthor().getName() : nickName), event.getAuthor().getAvatarUrl());
                event.getChannel().sendMessageEmbeds(helpCommandEmbed.build()).queue();
            }
        }


    }
    @Override
    public List<String> getAliases() {
        return Arrays.asList("help", "h");
    }
    @Override
    public String getName() {
        return "Help Command";
    }

    @Override
    public String getDescription() {
        return "Помощь по командам";
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
