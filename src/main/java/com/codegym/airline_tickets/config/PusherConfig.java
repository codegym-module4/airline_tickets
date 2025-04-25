package com.codegym.airline_tickets.config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {

    @Bean
    public Pusher pusher() {
        Pusher pusher = new Pusher("1978711", "d53316a74ae925d2b200", "580bb0a64fa09b70de9d");
        pusher.setCluster("ap1");
        pusher.setEncrypted(true);

        return pusher;
    }
}
