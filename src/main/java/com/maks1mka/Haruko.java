package com.maks1mka;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import com.maks1mka.commands.Handler;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


public class Haruko {

    public static void main(String[] args) throws Exception {
        String token = "token";

        JDABuilder api = JDABuilder.createDefault(token);
        api.enableIntents(
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_WEBHOOKS

        );
        api.enableCache(
                CacheFlag.ONLINE_STATUS,
                CacheFlag.ACTIVITY,
                CacheFlag.MEMBER_OVERRIDES,
                CacheFlag.CLIENT_STATUS
        );
        Handler.preInit(api);
        Handler.commands(api);

        //handler commandHandler = new handler()

        JDA jda = api.build();
        Handler.setJda(jda);
        jda.awaitReady();

    }


}
