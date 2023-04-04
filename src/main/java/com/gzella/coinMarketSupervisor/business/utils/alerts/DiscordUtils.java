//package com.gzella.coinMarketSupervisor.business.utils.alerts;
//
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.JDABuilder;
//import net.dv8tion.jda.api.entities.Activity;
//import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
//import org.springframework.stereotype.Component;
//
////  DISCORD ALERT TO DO
//
//@Component
//public class DiscordUtils {
//
//    private final JDA bot = JDABuilder.createDefault("*****")
//            .setActivity(Activity.watching("for price changes"))
//            .build();
//
//    public void makeNotification() {
//        TextChannel channel = bot.getChannelById(TextChannel.class,"*****");
//        channel.sendMessage("this is my first message").queue();
//    }
//}
