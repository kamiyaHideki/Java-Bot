package com.maks1mka.listeners;

import com.maks1mka.db.UserCollection;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberLeave extends ListenerAdapter {
    UserCollection userCollection = new UserCollection();
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        userCollection.delUser(event.getMember().getUser().getIdLong());
    }
}
