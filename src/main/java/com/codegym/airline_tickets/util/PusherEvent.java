package com.codegym.airline_tickets.util;

import com.codegym.airline_tickets.entity.FlightSeat;
import com.google.gson.Gson;
import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class PusherEvent {

    @Autowired
    private Pusher pusher;

    public void pusherTrigger (String channel, String event, FlightSeat seat) {
//        FlightSeat data = seat;
        String data = JsonUtil.toJson(seat);
        pusher.trigger(channel, event, data);
    }
}
