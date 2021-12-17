package com.maks1mka.listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyEvent extends ListenerAdapter {
    @Override
    public void onReady(net.dv8tion.jda.api.events.ReadyEvent event) {
        System.out.println("______________________________________________________________________");
        System.out.println("Current guilds count: " + event.getGuildTotalCount());
        System.out.println("Logged in successfully");
    }
}
